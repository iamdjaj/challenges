package yieldstreet.challenges.concurrent;

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