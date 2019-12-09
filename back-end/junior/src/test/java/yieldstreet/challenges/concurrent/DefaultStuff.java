package yieldstreet.challenges.concurrent;

class DefaultStuff implements Stuff {
    private final String id;
    private final Object payload;

    DefaultStuff(String id, Object payload) {
        this.id = id;
        this.payload = payload;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
