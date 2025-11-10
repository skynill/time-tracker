package com.skynill.timetracker.cucumber;

import com.skynill.timetracker.domain.timeentry.buffer.TimeQueue;
import com.skynill.timetracker.utils.AsyncRunnable;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringBootGlue {

    private final RestTemplateBuilder restTemplateBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final List<AsyncRunnable> asyncServices;
    private final TimeQueue buffer;

    public CucumberSpringBootGlue(RestTemplateBuilder restTemplateBuilder, JdbcTemplate jdbcTemplate, List<AsyncRunnable> asyncServices, TimeQueue buffer) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.asyncServices = asyncServices;
        this.buffer = buffer;
    }

    @Bean
    private TestRestTemplate testRestTemplate() {
        return new TestRestTemplate(restTemplateBuilder);
    }

    @Before(order = 1)
    public void cleanup() {
        stopAsyncFunctionality();
        cleanDb();
        cleanState();
        cleanBuffer();
    }

    private void cleanBuffer() {
        buffer.bufferClean();
    }

    private void stopAsyncFunctionality() {
        asyncServices.forEach(AsyncRunnable::stopAsync);
    }

    private void cleanState() {
        log.info("clean state");
        ScenarioState.STATE.reset();
    }

    private void cleanDb() {
        log.info("cleaning data in db");
        log.debug("start clean table time_entries");
        jdbcTemplate.update("DELETE FROM time_entries; COMMIT;");
        log.debug("finish clean table time_entries");
    }
}
