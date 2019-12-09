package yieldstreet.challenges.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CallbackAdapterTest {
    private CallbackToAsyncStuffRepository repository;

    @Before
    public void setUp() {
        CallbackStuffRepository callbackRepository = (id, callback) -> {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    callback.onError(e);
                }

                if ("error".equals(id)) {
                    callback.onError(new IllegalArgumentException("error"));
                } else {
                    callback.onSuccess(new DefaultStuff(id, new Object()));
                }
            }, "callback-repository-thread");

            thread.start();
        };

        repository = new CallbackToAsyncStuffRepository(callbackRepository);
    }

    @Test
    public void testRepositorySuccess() {
        Stuff result = repository.findById("good").toCompletableFuture().join();
        assertThat(result.getId(), equalTo("good"));
    }

    @Test(expected = CompletionException.class)
    public void testRepositoryError() {
        repository.findById("error").toCompletableFuture().join();
        fail();
    }

    @Test(timeout = 1000)
    public void testRepositoryIsConcurrent() {
        CompletableFuture[] futures = IntStream.range(0, 4)
            .mapToObj(String::valueOf)
            .map(repository::findById)
            .map(CompletionStage::toCompletableFuture)
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures);
    }
}
