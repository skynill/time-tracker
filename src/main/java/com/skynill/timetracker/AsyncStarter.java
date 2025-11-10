package com.skynill.timetracker;

import com.skynill.timetracker.utils.AsyncRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("!test")
public class AsyncStarter implements CommandLineRunner {

    private final List<AsyncRunnable> asyncRunnables;

    public AsyncStarter(List<AsyncRunnable> asyncRunnables) {
        this.asyncRunnables = asyncRunnables;
    }

    @Override
    public void run(String... args) {
        log.info("Starting async components");
        asyncRunnables.forEach(AsyncRunnable::startAsync);
    }
}
