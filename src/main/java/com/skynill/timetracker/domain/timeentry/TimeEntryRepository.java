package com.skynill.timetracker.domain.timeentry;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeEntryRepository extends CrudRepository<TimeEntry, Long> {

    @NotNull
    List<TimeEntry> findAll();

}
