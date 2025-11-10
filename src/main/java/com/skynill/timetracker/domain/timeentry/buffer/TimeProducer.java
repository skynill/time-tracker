package com.skynill.timetracker.domain.timeentry.buffer;

import com.skynill.timetracker.domain.timeentry.TimeEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class TimeProducer {

    private final TimeQueue timeQueue;
    private final AtomicBoolean lastResult = new AtomicBoolean(false);

    public TimeProducer(TimeQueue timeQueue) {
        this.timeQueue = timeQueue;
    }

    public void produce(TimeEntry entry) {
        lastResult.set(timeQueue.offer(entry));
        log.debug("Time entry: {} was pushed to queue with result {}", entry.getTimestamp(), lastResult.get());
    }

    public boolean getLastResult() {
        return lastResult.get();
    }

}
