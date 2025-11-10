package com.skynill.timetracker.service;

import com.skynill.timetracker.domain.timeentry.TimeEntry;
import com.skynill.timetracker.domain.timeentry.buffer.TimeProducer;
import com.skynill.timetracker.utils.AsyncOrchestrator;
import com.skynill.timetracker.utils.AsyncRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class TimeGeneratorService implements AsyncRunnable {

    public static final String SERVICE_NAME = "TimeGenerator";

    private final TimeProducer timeProducer;

    private final AtomicLong generatedCount = new AtomicLong(0);

    private volatile ExecutorService executor;


    public TimeGeneratorService(TimeProducer timeProducer) {
        this.timeProducer = timeProducer;
    }

    @Override
    public synchronized void startAsync() {
        executor = AsyncOrchestrator.startWithPreStartTask(executor, this::generateLoop, this::cleanGeneratedCount, SERVICE_NAME);
    }

    @Override
    public synchronized void stopAsync() {
        AsyncOrchestrator.stop(executor, SERVICE_NAME);
    }

    public long getGeneratedCount() {
        return generatedCount.get();
    }

    public void cleanGeneratedCount() {
        generatedCount.set(0);
    }

    private void generateLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            generateTimeEntry();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("GenerateLoop thread interrupted");
            }
        }
    }

    private void generateTimeEntry() {
        Instant now = Instant.now();
        generatedCount.incrementAndGet();
        TimeEntry entry = TimeEntry.builder().timestamp(now).build();
        log.info("Time was generated: {}", now);
        timeProducer.produce(entry);
    }

}
