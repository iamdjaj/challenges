package yieldstreet.challenges.concurrent;

public interface CallbackStuffRepository {
    /**
     * Finds some stuff by id.
     *
     * @param id the stuff id.
     * @param callback a callback for signalling completion.
     */
    void findById(String id, StuffCallback callback);
}
