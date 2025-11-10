package com.skynill.timetracker.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AsyncOrchestrator {

    private AsyncOrchestrator() {}

    public static ExecutorService start(ReentrantLock lock, ExecutorService executor, Runnable task, String serviceName) {
        return startWithPreStartTask(lock, executor, task, () -> {
        }, serviceName);
    }

    public static void stop(ReentrantLock lock, ExecutorService executor, String serviceName) {
        stopWithPostStopTask(lock, executor, () -> {
        }, serviceName);
    }

    public static ExecutorService startWithPreStartTask(ReentrantLock lock, ExecutorService executor, Runnable task, Runnable preStartTask, String serviceName) {
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }

    }

    public static void stopWithPostStopTask(ReentrantLock lock, ExecutorService executor, Runnable postStopTask, String serviceName) {
        lock.lock();
        try {
            if (executor != null) {
                executor.shutdownNow();
                postStopTask.run();
                log.info("{} stopped", serviceName);
            } else {
                log.info("{} already stopped", serviceName);
            }
        } finally {
            lock.unlock();
        }
    }

}
