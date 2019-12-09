package yieldstreet.user;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ShardRegion;
import akka.pattern.Patterns;
import yieldstreet.user.UserHolderMessage.GetAccreditation;
import yieldstreet.user.UserHolderMessage.Subscribe;
import yieldstreet.user.UserHolderMessage.UpdateAccreditation;

public class UserServiceImpl implements UserService {
    private static final String   TYPE_NAME = "UserHolder";
    private static final Duration TIMEOUT   = Duration.ofSeconds(10);

    private final ActorRef region;

    @Inject
    public UserServiceImpl(ActorSystem system) {
        var sharding = ClusterSharding.get(system);

        this.region = sharding.start(TYPE_NAME,
            Props.create(UserHolder.class, UserHolder::new),
            new UserHolderMessageExtractor());
    }

    @Override
    public CompletionStage<Accreditation> get(String userId) {
        return Patterns
            .ask(region, new GetAccreditation(userId), TIMEOUT)
            .thenApply(Accreditation.class::cast);
    }

    @Override
    public void update(String userId, boolean accredited) {
        UpdateAccreditation message = new UpdateAccreditation(userId, accredited);
        region.tell(message, ActorRef.noSender());
    }

    @Override
    public void subscribe(String userId, ActorRef subscriber) {
        Subscribe message = new Subscribe(userId);
        region.tell(message, subscriber);
    }

    private static class UserHolderMessageExtractor implements ShardRegion.MessageExtractor {
        @Override
        public String entityId(Object message) {
            if (message instanceof UserHolderMessage) {
                return URLEncoder.encode(((UserHolderMessage) message).getUserId(), StandardCharsets.UTF_8);
            }

            return null;
        }

        @Override
        public Object entityMessage(Object message) {
            return message;
        }

        @Override
        public String shardId(Object message) {
            if (message instanceof UserHolderMessage) {
                String userId = ((UserHolderMessage) message).getUserId();
                return String.valueOf(userId.hashCode() % 12);
            }

            return null;
        }
    }
}
