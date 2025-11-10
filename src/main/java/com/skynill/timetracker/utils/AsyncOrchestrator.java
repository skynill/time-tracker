package com.skynill.timetracker.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class AsyncOrchestrator {

    private AsyncOrchestrator() {
    }

    public static ExecutorService start(ExecutorService executor, Runnable task, String serviceName) {
        return startWithPreStartTask(executor, task, () -> {
        }, serviceName);
    }

    public static void stop(ExecutorService executor, String serviceName) {
        stopWithPostStopTask(executor, () -> {
        }, serviceName);
    }

    public static ExecutorService startWithPreStartTask(ExecutorService executor, Runnable task, Runnable preStartTask, String serviceName) {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newVirtualThreadPerTaskExecutor();
            preStartTask.run();
            executor.execute(task);
            log.info("{} started", serviceName);
            return executor;
        } else {
            log.info("{} already started", serviceName);
            return executor;
        }

    }

    public static void stopWithPostStopTask(ExecutorService executor, Runnable postStopTask, String serviceName) {
        if (executor != null) {
            executor.shutdownNow();
            postStopTask.run();
            log.info("{} stopped", serviceName);
        } else {
            log.info("{} already stopped", serviceName);
        }
    }

}
