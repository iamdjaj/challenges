package yieldstreet.user;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.pf.ReceiveBuilder;
import yieldstreet.user.UserHolderMessage.GetAccreditation;
import yieldstreet.user.UserHolderMessage.Subscribe;
import yieldstreet.user.UserHolderMessage.UpdateAccreditation;

public class UserHolder extends AbstractActor {
    private final DiagnosticLoggingAdapter logger;

    private Set<ActorRef> subscribers;
    private Accreditation accreditation;

    public UserHolder() {
        var userId = URLDecoder.decode(getSelf().path().name(), StandardCharsets.UTF_8);

        logger = Logging.getLogger(this);
        logger.setMDC(Map.of("userId", userId));

        this.subscribers = new HashSet<>();
        this.accreditation = new Accreditation(userId, false);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(GetAccreditation.class, this::onGetAccreditation)
            .match(UpdateAccreditation.class, this::onUpdateAccreditation)
            .match(Subscribe.class, this::onSubscribe)
            .match(Terminated.class, this::onTerminated)
            .build();
    }

    private void onGetAccreditation(GetAccreditation message) {
        logger.debug("Querying accreditation status {}", message);
        sendAccreditation(getSender());
    }

    private void onUpdateAccreditation(UpdateAccreditation message) {
        logger.debug("Updating accreditation status with {}", message);
        this.accreditation = this.accreditation.withAccredited(message.isAccredited());
        this.subscribers.forEach(this::sendAccreditation);
    }

    private void onSubscribe(Subscribe message) {
        var sender = getSender();
        logger.debug("Subscribing actor {} to user {}", sender, accreditation.getUserId());

        this.subscribers.add(sender);
        getContext().watch(sender);
        sendAccreditation(sender);
    }

    private void onTerminated(Terminated message) {
        this.subscribers.remove(message.getActor());
    }

    private void sendAccreditation(ActorRef target) {
        logger.debug("Sending accreditation status to {}", target);
        target.tell(this.accreditation, getSelf());
    }
}
