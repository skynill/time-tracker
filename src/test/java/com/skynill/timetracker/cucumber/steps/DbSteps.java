package com.skynill.timetracker.cucumber.steps;

import com.skynill.timetracker.domain.timeentry.TimeEntry;
import com.skynill.timetracker.domain.timeentry.TimeEntryRepository;
import com.skynill.timetracker.domain.timeentry.TimeService;
import com.skynill.timetracker.service.TimeGeneratorService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.google.common.collect.Comparators;

import java.util.Comparator;

@Slf4j
@RequiredArgsConstructor
public class DbSteps extends AbstractSteps {

    private final TimeEntryRepository repository;
    private final TimeService timeService;
    private final TimeGeneratorService generatorService;

    @And("db contains {long} records in asc order")
    public void dbContainsRecordsInAscOrder(long count) {
        var list = repository.findAll();
        Assertions.assertEquals(count, list.size());
        boolean sorted = Comparators.isInOrder(list, Comparator.comparing(TimeEntry::getTimestamp));
        Assertions.assertTrue(sorted);
    }

    @And("db is empty")
    public void dbIsEmpty() {
        var list = repository.findAll();
        Assertions.assertEquals(0, list.size());
    }

    @And("db contains all records generated records in asc order")
    public void dbContainsAllRecordsGeneratedRecordsInAscOrder() {
        var list = repository.findAll();
        long generatedCount = generatorService.getGeneratedCount();
        Assertions.assertEquals(generatedCount, list.size());
        boolean sorted = Comparators.isInOrder(list, Comparator.comparing(TimeEntry::getTimestamp));
        Assertions.assertTrue(sorted);
    }

    @And("database is artificially slowed by {int} ms")
    public void databaseResponseTimeIsArtificiallySlowedByMs(int timeOutMs) {
        TimeEntryRepository realRepositary = (TimeEntryRepository) ReflectionTestUtils.getField(timeService, "repository");
        state().set("TimeEntryRepository", realRepositary);
        TimeEntryRepository mockRepository = Mockito.mock(TimeEntryRepository.class);
        Mockito.when(mockRepository.save(Mockito.any())).thenAnswer(invocation -> {
            sleep(timeOutMs);
            Assertions.assertNotNull(realRepositary);
            return realRepositary.save(invocation.getArgument(0, TimeEntry.class));
        });
        ReflectionTestUtils.setField(timeService, "repository", mockRepository);
    }


    @When("database is down")
    public void theDatabaseBecomesTemporarilyUnavailable() {
        state().set("TimeEntryRepository", (TimeEntryRepository) ReflectionTestUtils.getField(timeService, "repository"));
        TimeEntryRepository mockRepository = Mockito.mock(TimeEntryRepository.class);
        Mockito.when(mockRepository.save(Mockito.any(TimeEntry.class))).thenThrow(new CannotGetJdbcConnectionException("dd"));
        ReflectionTestUtils.setField(timeService, "repository", mockRepository);
        log.info("Database configured to be temporarily unavailable");
    }

    @Then("database again is in normal mode")
    public void callCenterDatabaseIsUpAgain() {
        ReflectionTestUtils.setField(timeService, "repository", state().get("TimeEntryRepository"));
    }

}
