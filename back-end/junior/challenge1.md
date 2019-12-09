# Singleton

Let's say you have a class with dependencies injected via the constructor, but you also want
a singleton default instance, lazily created when first accessed. This is one possible implementation:

```java
public class StuffService {
    private final Object dep1;
    private final Object dep2;

    public StuffService(Object dep1, Object dep2) {
        this.dep1 = dep1;
        this.dep2 = dep2;
    }

    public static StuffService defaultInstance() {
        if (_instance == null) {
            _instance = new StuffService(new Object(), new Object());
        }

        return _instance;
    }

    private static StuffService _instance;
}
```

What's wrong with the `defaultInstance()` implementation? How would you fix it?

### Notes and hints
* The _default_ instance for `StuffService` must be a singleton, but other non-default instances can be instantiated. The constructor doesn't have to be private.
* The constructor parameters for this class represent dependencies, and we're using `Object` as a placeholder. In other words, there's nothing wrong with `new Object()`.
* What happens if two threads call `defaultInstance()` at the same time?
