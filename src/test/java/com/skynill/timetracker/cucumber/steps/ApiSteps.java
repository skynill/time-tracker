package com.skynill.timetracker.cucumber.steps;

import com.skynill.timetracker.api.TimeController;
import com.skynill.timetracker.api.dto.TimeDto;
import com.skynill.timetracker.service.TimeGeneratorService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.google.common.collect.Comparators;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ApiSteps extends AbstractSteps {

    private final TestRestTemplate testRestTemplate;
    private final TimeGeneratorService generatorService;

    @When("user requests all time entries")
    public void userRequestsAllTimeEntries() {
        ResponseEntity<List<TimeDto>> response = testRestTemplate.exchange(
                TimeController.BASE_PATH + TimeController.TIME_ENTRIES,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertNotNull(response.getBody());
        state().setResponse(response);
    }

    @And("response code is {int}")
    public void responseCodeIs(int statusCode) {
        HttpStatusCode expected = HttpStatusCode.valueOf(statusCode);
        Assertions.assertEquals(expected, state().getResponse().getStatusCode());
    }

    @Then("response contains {long} records in asc order")
    public void responseContainsRecordsInAscOrder(long count) {
        List<TimeDto> list = state().getResponseBody(List.class);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(count, list.size());
        boolean sorted = Comparators.isInOrder(list, Comparator.comparing(TimeDto::timestamp));
        Assertions.assertTrue(sorted);
    }


    @Then("response contains all records generated records in asc order")
    public void responseContainsAllRecordsGeneratedRecordsInAscOrder() {
        List<TimeDto> list = state().getResponseBody(List.class);
        Assertions.assertNotNull(list);
        long generatedCount = generatorService.getGeneratedCount();
        Assertions.assertEquals(generatedCount, list.size());
        boolean sorted = Comparators.isInOrder(list, Comparator.comparing(TimeDto::timestamp));
        Assertions.assertTrue(sorted);
    }
}
