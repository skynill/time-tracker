package com.skynill.timetracker.cucumber.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor
public class CommonSteps extends AbstractSteps {

    private final ApplicationContext appContext;

    @Given("the time tracker is initialized")
    public void timeServiceIsInitialized() {
        Assertions.assertNotNull(appContext);
    }

    @And("{int} seconds passed")
    public void secondsPassed(int sec) {
        sleep(sec * 1000L);
    }

}
