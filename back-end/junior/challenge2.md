# CompletionStage and Optional

## Chaining

Given the following:

```java
public interface Stuff {
    /** The stuff id */
    String getId();

    /** The stuff payload */
    Object getPayload();
}
```

```java
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface StuffRepository {
    /**
     * Looks for existing stuff matching the given payload and returns the
     * existing stuff id.
     * 
     * @param payload the stuff payload.
     * @return a completion stage for an optional with the stuff id. It's
     *         completed with an empty optional if no stuff is found.
     */
    CompletionStage<Optional<String>> findStuff(Stuff payload);

    /**
     * Creates new stuff with the given payload and returns the id for the
     * newly created stuff.
     * 
     * @param payload the stuff payload.
     * @return a completion stage for the newly created stuff id.
     */
    CompletionStage<String> createStuff(Stuff payload);

    /**
     * Finds existing stuff matching the given payload, or creates the stuff
     * if it's not found.
     * 
     * @param payload the stuff payload.
     * @return a completion stage with the id for the existing or newly created stuff.
     */
    default CompletionStage<String> findOrCreateStuff(Stuff payload) {
        // return ???
    }
}
```

Provide an implementation for `findOrCreateStuff` in terms of `findStuff` and `createStuff`. Bonus points for doing this in a single statement.

## Adapting a blocking API

You're using a third-party library that does not offer an async API. Something like this:

```java
public interface BlockingStuffRepository {
    /**
     * Find some stuff by id. This is an expensive call involving I/O
     * operations and might take a while to return.
     *
     * @param id the stuff id.
     * @return the stuff.
     */
    Stuff findById(String id);
}
```

Write a method adapting the `findById(String id)` method above to execute
asynchronously. Start from the following implementation, and assume default
execution facilities.

```java
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
        // provide your implementation here
    }
}
```

## Adapting a callback-based API

You're using a third party library with an async API based on callbacks.
Something like this:

```java
public interface StuffCallback {
    /** Called when findById completes successfully. */
    void onSuccess(Stuff stuff);

    /** Called when findById throws an exception. */
    void onError(Throwable throwable);
}
```

```java
public interface CallbackStuffRepository {
    /**
     * Finds some stuff by id.
     *
     * @param id the stuff id.
     * @param callback a callback for signalling completion.
     */
    void findById(String id, StuffCallback callback);
}
```

Write a method adapting the `findById(String id, StuffCallback callback)`
method above to execute asynchronously. Start from the following implementation.

```java
public class CallbackToAsyncStuffRepository {
    private final CallbackStuffRepository callbackRepository;

    public CallbackToAsyncStuffRepository(CallbackStuffRepository callbackRepository) {
        this.callbackRepository = callbackRepository;
    }

    /**
     * Find some stuff by id, asynchronously.
     *
     * @param id the stuff id
     * @return a completion stage for the stuff.
     */
    public CompletionStage<Stuff> findById(String id) {
        // provide your implementation here
    }
}
```
