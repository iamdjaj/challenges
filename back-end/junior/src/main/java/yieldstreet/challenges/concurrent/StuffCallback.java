package yieldstreet.challenges.concurrent;

public interface StuffCallback {
    /** Called when findById completes successfully. */
    void onSuccess(Stuff stuff);

    /** Called when findById throws an exception. */
    void onError(Throwable throwable);
}
