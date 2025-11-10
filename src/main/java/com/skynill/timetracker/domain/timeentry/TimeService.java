package com.skynill.timetracker.domain.timeentry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TimeService {

    private final TimeEntryRepository repository;

    public TimeService(TimeEntryRepository repository) {

        this.repository = repository;
    }

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 2000)
    )
    @Transactional
    public void save(TimeEntry entry) {
        repository.save(entry);
    }

    public List<TimeEntry> getAllTimeEntries() {
        return repository.findAll();
    }

}
