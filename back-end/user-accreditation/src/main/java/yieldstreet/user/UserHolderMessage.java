package yieldstreet.user;

import lombok.Value;

/** Message protocol for the {@link UserHolder} actor. */
public interface UserHolderMessage {

    /** The id of the user this message is about. */
    String getUserId();

    /**
     * Request accreditation details for an user. The sender will get a
     * {@link Accreditation} message back.
     */
    @Value
    class GetAccreditation implements UserHolderMessage {
        String userId;
    }

    /** Update accreditation details for a user. */
    @Value
    class UpdateAccreditation implements UserHolderMessage {
        String  userId;
        boolean accredited;
    }

    /**
     * Subscribe an actor to updates to user accreditation status. The
     * sender will immediately get a {@link Accreditation} message with
     * the current accreditation status, and another message of the same type
     * every time the accreditation is updated.
     */
    @Value
    class Subscribe implements UserHolderMessage {
        String userId;
    }


}
