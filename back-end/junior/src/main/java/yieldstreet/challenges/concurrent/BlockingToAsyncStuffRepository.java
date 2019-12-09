package yieldstreet.challenges.concurrent;

import java.util.concurrent.CompletionStage;

public class BlockingToAsyncStuffRepository {
    private final BlockingStuffRepository blockingRepository;

    public BlockingToAsyncStuffRepository(BlockingStuffRepository blockingRepository) {
        this.blockingRepository = blockingRepository;
    }

    /**
     * Find some stuff by id, asynchronously.
     *
     * @param id the stuff id
     * @return a completion stage for the stuff.
     */
    public CompletionStage<Stuff> findById(String id) {
        // TODO: provide your implementation here
        throw new UnsupportedOperationException();
    }
}