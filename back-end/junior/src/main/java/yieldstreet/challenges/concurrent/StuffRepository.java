package yieldstreet.challenges.concurrent;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface StuffRepository {

    /**
     * Examines the {@link Stuff#getPayload() payload} of the given argument
     * and looks for an existing stuff matching that payload. If there's
     * already something matching that payload, return its id. This method
     * should not examine the {@link Stuff#getId() id} of its argument.
     *
     * @param payload the stuff payload.
     * @return a completion stage for an optional with the stuff id. It's
     *     completed with an empty optional if no stuff is found.
     */
    CompletionStage<Optional<String>> findStuff(Stuff payload);

    /**
     * Creates new stuff with the given payload and returns the id for the
     * newly created stuff. This method should generate a new id for the
     * payload and return it.
     *
     * @param payload the stuff payload.
     * @return a completion stage for the newly created stuff id.
     */
    CompletionStage<String> createStuff(Stuff payload);

    /**
     * Finds existing stuff matching the given payload, or creates the stuff if
     * it's not found. In other words, this should try to {@link #findStuff(Stuff)
     * find} a matching payload first; if there is a match, return its id,
     * otherwise {@link #createStuff(Stuff) create} new stuff and return the
     * newly generated id.
     *
     * @param payload the stuff payload.
     * @return the existing or newly generated stuff id.
     */
    default CompletionStage<String> findOrCreateStuff(Stuff payload) {
        // TODO: add your implementation here
        throw new UnsupportedOperationException();
    }

}
