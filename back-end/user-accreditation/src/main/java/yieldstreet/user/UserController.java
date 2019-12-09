package yieldstreet.user;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;

import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller handling user accreditation query and update actions. These are
 * mostly for testing the accreditation upload functionality.
 */
public class UserController extends Controller {
    private final ActorSystem  system;
    private final Materializer materializer;
    private final ObjectMapper objectMapper;
    private final UserService  service;

    @Inject
    public UserController(
        ActorSystem system,
        Materializer materializer,
        ObjectMapper objectMapper,
        UserService service) {
        this.system = system;
        this.materializer = materializer;
        this.objectMapper = objectMapper;
        this.service = service;
    }

    /**
     * Get the current accreditation status for a user. The response is the
     * serialized form of an {@link Accreditation} object. All users are not
     * accredited by default, and can have that status changed by the update
     * endpoint below, or by successfully submitting an accreditation payload.
     *
     * @param userId the user to get the accreditation status for.
     */
    public CompletionStage<Result> get(String userId) {
        return service.get(userId).thenApply(accreditation -> {
            var json = objectMapper.valueToTree(accreditation);
            return ok(json);
        });
    }

    /**
     * Update the accreditation status for a user.
     *
     * @param userId the user to update.
     * @param accredited the new accreditation status.
     */
    public Result update(String userId, boolean accredited) {
        service.update(userId, accredited);
        return ok();
    }

    /**
     * Open a WebSocket to receive current and future accreditation status
     * for a user. The socket ignores all incoming messages, and produce an
     * outgoing message on connect and whenever the accreditation status for
     * a user changes.
     *
     * @param userId the user to subscribe to.
     */
    public WebSocket subscribe(String userId) {
        return WebSocket.Json.accept(request -> {
            var props = userSocketProps(userId);
            return ActorFlow.actorRef(props, system, materializer);
        });
    }

    private Function<ActorRef, Props> userSocketProps(String userId) {
        return out -> Props.create(UserSocket.class, () -> {
            var socket = new UserSocket(objectMapper, out);
            service.subscribe(userId, socket.getSelf());
            return socket;
        });
    }
}
