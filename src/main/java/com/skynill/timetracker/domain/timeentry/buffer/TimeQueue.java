package com.skynill.timetracker.domain.timeentry.buffer;

import com.skynill.timetracker.domain.timeentry.TimeEntry;
import com.skynill.timetracker.utils.AsyncOrchestrator;
import com.skynill.timetracker.utils.AsyncRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TimeQueue implements AsyncRunnable {

    public static final String SERVICE_NAME_FOR_ASYNC = "Buffer utilization scaner";
    private static final int DEFAULT_CAPACITY = 20;

    private final BlockingQueue<TimeEntry> queue;
    private final AtomicLong droppedCount = new AtomicLong(0);
    private final AtomicLong offerCount = new AtomicLong(0);

    private final ReentrantLock lock = new ReentrantLock();
    private ExecutorService executor;

    public TimeQueue(@Value("${app.time.buffer.capacity}") int capacity) {
        int cap = capacity > 0 ? capacity : DEFAULT_CAPACITY;
        queue = new LinkedBlockingQueue<>(cap);
        log.info("Time buffer initialized with capacity: {}", cap);
    }

    @Override
    public void startAsync() {
        executor = AsyncOrchestrator.start(lock, executor, this::bufferUtilization, SERVICE_NAME_FOR_ASYNC);
    }

    @Override
    public void stopAsync() {
        AsyncOrchestrator.stop(lock, executor, SERVICE_NAME_FOR_ASYNC);
    }

    public void bufferClean() {
        queue.clear();
        droppedCount.set(0);
        offerCount.set(0);
        log.info("Time buffer is reset");
    }

    private void bufferUtilization() {
        while (!Thread.currentThread().isInterrupted()) {
            int fullSize = queue.size() + queue.remainingCapacity();
            double percentage = ((double) queue.size() / fullSize) * 100;
            if (percentage > 50) {
                log.warn("Time buffer is more than {} percent full! CHECK DB CONNECTION!!!", String.format("%.2f%%", percentage));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Time buffer utilization thread interrupted");
            }
        }
    }

    boolean offer(TimeEntry entry) {
        var success = queue.offer(entry);
        if (success) {
            offerCount.incrementAndGet();
            log.debug("Added to queue: {}", entry.getTimestamp());
        } else {
            log.warn("Buffer full! Dropped time entry: {}, dropped count: {}", entry.getTimestamp(), droppedCount.incrementAndGet());
        }
        sizeLog();
        return success;
    }

    TimeEntry take() throws InterruptedException {
        TimeEntry entry = queue.take();
        log.debug("Taken from queue: {})", entry.getTimestamp());
        return entry;
    }

    private void sizeLog() {
        log.debug("Current time buffer size: {}", queue.size());
    }

    public long getOfferCount() {
        return offerCount.get();
    }

    public long getDroppedCount() {
        return droppedCount.get();
    }

    public long getQueueSize() {
        return queue.size();
    }

    public long getQueueRemainingCapacity() {
        return queue.remainingCapacity();
    }

}
