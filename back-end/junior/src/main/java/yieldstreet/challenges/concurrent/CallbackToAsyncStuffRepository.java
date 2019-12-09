package yieldstreet.challenges.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CallbackToAsyncStuffRepository {
    private final CallbackStuffRepository callbackRepository;

    public CallbackToAsyncStuffRepository(CallbackStuffRepository callbackRepository) {
        this.callbackRepository = callbackRepository;
    }

    /**
     * Find some stuff by id, asynchronously. Hint: {@link CompletableFuture}
     * is your friend.
     *
     * @param id the stuff id
     * @return a completion stage for the stuff.
     */
    public CompletionStage<Stuff> findById(String id) {
        // TODO: provide your implementation here
        throw new UnsupportedOperationException();
    }
}