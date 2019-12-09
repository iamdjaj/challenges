package yieldstreet.challenges.singleton;

public class StuffService {
    private final Object dep1;
    private final Object dep2;

    public StuffService(Object dep1, Object dep2) {
        this.dep1 = dep1;
        this.dep2 = dep2;

        try {
            // this is here to simulate a more costly initialization
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static StuffService defaultInstance() {
        if (_instance == null) {
            _instance = new StuffService(new Object(), new Object());
        }

        return _instance;
    }

    private static StuffService _instance;
}
