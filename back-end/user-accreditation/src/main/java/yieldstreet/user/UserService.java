package yieldstreet.user;

import java.util.concurrent.CompletionStage;

import akka.actor.ActorRef;

public interface UserService {

    /**
     * Get the current accreditation status for a given user.
     *
     * @param userId the user id.
     * @return the user accreditation status.
     */
    CompletionStage<Accreditation> get(String userId);

    /**
     * Update the accreditation status for a given user.
     *
     * @param userId the user id.
     * @param accredited if the user is accredited or not.
     */
    void update(String userId, boolean accredited);

    /**
     * Subscribe to accreditation status changes for a user.
     *
     * @param userId the user id.
     * @param subscriber the actor that will get accreditation updates.
     */
    void subscribe(String userId, ActorRef subscriber);

}
