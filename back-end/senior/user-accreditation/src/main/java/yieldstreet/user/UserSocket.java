package yieldstreet.user;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserSocket extends AbstractActor {
    private final LoggingAdapter logger;
    private final ObjectMapper   objectMapper;
    private final ActorRef       out;

    UserSocket(ObjectMapper objectMapper, ActorRef out) {
        this.logger = Logging.getLogger(this);
        this.objectMapper = objectMapper;
        this.out = out;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(JsonNode.class, this::onRequest)
            .match(Accreditation.class, this::onAccreditation)
            .build();
    }

    private void onRequest(JsonNode message) {
        logger.warning("Ignoring unhandled incoming message {}", message);
    }

    private void onAccreditation(Accreditation accreditation) {
        var message = objectMapper.createObjectNode().put("accredited", accreditation.isAccredited());
        out.tell(message, getSelf());
    }
}
