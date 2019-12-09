package yieldstreet.user;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

import play.Environment;
import play.inject.Binding;
import play.inject.Module;

import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import com.typesafe.config.Config;

public class UserModule extends Module {

    @Override
    public List<Binding<?>> bindings(Environment environment, Config config) {
        return List.of(bindClass(UserService.class).toProvider(UserServiceProvider.class));
    }

    private static class UserServiceProvider implements Provider<UserService> {
        private final UserService service;

        @Inject
        UserServiceProvider(ActorSystem system) {
            var cluster = Cluster.get(system);
            cluster.join(cluster.selfAddress());
            service = new UserServiceImpl(system);
        }

        @Override
        public UserService get() {
            return service;
        }
    }

}
