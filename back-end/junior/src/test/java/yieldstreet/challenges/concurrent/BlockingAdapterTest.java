package yieldstreet.challenges.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BlockingAdapterTest {
    private BlockingToAsyncStuffRepository blockingToAsyncStuffRepository;

    @Before
    public void setUp() {
        BlockingStuffRepository blockingStuffRepository = id -> {
            try {
                // simulate some work
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new DefaultStuff(id, new Object());
        };

        blockingToAsyncStuffRepository = new BlockingToAsyncStuffRepository(blockingStuffRepository);
    }

    @Test
    public void testBlockingAdapter() {
        Stuff stuff = blockingToAsyncStuffRepository.findById("single").toCompletableFuture().join();
        assertThat(stuff.getId(), equalTo("single"));
    }

    // this test should complete in less than a second
    // to prove multiple requests are executed concurrently
    @Test(timeout = 1000L)
    public void testMultipleCallsExecutedConcurrently() {
        CompletableFuture[] futures = IntStream.range(1, 4)
            .mapToObj(String::valueOf)
            .map(blockingToAsyncStuffRepository::findById)
            .map(CompletionStage::toCompletableFuture)
            .toArray(CompletableFuture[]::new);

        CompletableFuture.anyOf(futures).join();
    }

}
