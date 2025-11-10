package com.skynill.timetracker.cucumber.steps;

import com.skynill.timetracker.domain.timeentry.buffer.TimeConsumer;
import com.skynill.timetracker.domain.timeentry.buffer.TimeProducer;
import com.skynill.timetracker.domain.timeentry.buffer.TimeQueue;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class BufferSteps extends AbstractSteps {

    private final TimeConsumer timeConsumer;
    private final TimeQueue timeBuffer;
    private final TimeProducer timeProducer;

    @And("async db writer is running")
    public void asyncDbWriterIsRunning() {
        timeConsumer.startAsync();
    }

    @Then("{long} records were pushed to buffer")
    public void timeEntriesWerePulledToBuffer(long count) {
        Assertions.assertEquals(count, timeBuffer.getOfferCount());
    }

    @And("buffer contains {long} records")
    public void bufferContainsRecords(long count) {
        Assertions.assertEquals(count, timeBuffer.getQueueSize());
    }

    @And("waiting for the buffer to empty")
    public void waitingForTheBufferToEmpty() {
        Awaitility.await()
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> timeBuffer.getQueueSize() == 0);
    }

    @Then("queue in the buffer is growing up in {int} sec")
    public void queueInTheBufferIsGrowingUpInSec(int sec) {
        long initialQueueSize = timeBuffer.getQueueSize();
        sleep(sec * 1000L);
        long currentQueueSize = timeBuffer.getQueueSize();
        Assertions.assertTrue(initialQueueSize < currentQueueSize);

    }

    @And("waiting buffer overload")
    public void waitingBufferOverload() {
        Awaitility.await()
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() -> timeBuffer.getQueueRemainingCapacity() == 0);
    }

    @Then("new records will be lost")
    public void newRecordsWillBeLost() {
        Assertions.assertFalse(timeProducer.getLastResult());
    }

    @Then("new records are stored")
    public void newRecordsAreStored() {
        Assertions.assertTrue(timeProducer.getLastResult());
    }

}
