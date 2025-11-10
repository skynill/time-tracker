package com.skynill.timetracker.cucumber.steps;

import com.skynill.timetracker.service.TimeGeneratorService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

@Slf4j
@RequiredArgsConstructor
public class TimeGeneratorSteps extends AbstractSteps {

    private final TimeGeneratorService timeGeneratorService;

    @Given("time generator is running during {int} sec")
    public void timeGeneratorIsRunningDuringSec(int sec) {
        timeGeneratorService.startAsync();
        sleep(sec * 1000L);
        timeGeneratorService.stopAsync();
    }

    @Given("time generator is running")
    public void timeGeneratorIsRunning() {
        timeGeneratorService.startAsync();
    }

    @And("time generator is stopped")
    public void timeGeneratorIsStoped() {
        timeGeneratorService.stopAsync();
    }

    @Then("time generator generated {long} time entries")
    public void generatorGeneratedTimeEntries(long count) {
        Assertions.assertEquals(count, timeGeneratorService.getGeneratedCount());
    }
}
