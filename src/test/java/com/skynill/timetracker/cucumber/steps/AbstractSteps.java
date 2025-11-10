package com.skynill.timetracker.cucumber.steps;

import com.skynill.timetracker.cucumber.ScenarioState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSteps {

    protected ScenarioState state() {
        return ScenarioState.STATE;
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Test thread interrupted");
        }
    }

}
