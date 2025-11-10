package com.skynill.timetracker.domain.timeentry.buffer;

import com.skynill.timetracker.domain.timeentry.TimeEntry;
import com.skynill.timetracker.domain.timeentry.TimeService;
import com.skynill.timetracker.utils.AsyncOrchestrator;
import com.skynill.timetracker.utils.AsyncRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TimeConsumer implements AsyncRunnable {

    public static final String SERVICE_NAME = "TimeConsumer";

    private final TimeQueue buffer;
    private final TimeService timeService;

    private final ReentrantLock lock = new ReentrantLock();
    private ExecutorService executor;

    public TimeConsumer(TimeQueue buffer, TimeService timeService) {
        this.buffer = buffer;
        this.timeService = timeService;
    }

    @Override
    public void startAsync() {
        executor = AsyncOrchestrator.start(lock, executor, this::consume, SERVICE_NAME);
    }

    @Override
    public void stopAsync() {
        AsyncOrchestrator.stop(lock, executor, SERVICE_NAME);
    }

    private void consume() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TimeEntry entry = buffer.take();
                log.debug("Time entry: {} was polled from buffer ", entry.getTimestamp());
                timeService.save(entry);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Thread interrupted");
            }

        }
    }

}
