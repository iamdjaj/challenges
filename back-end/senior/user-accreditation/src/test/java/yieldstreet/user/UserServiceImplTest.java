package yieldstreet.user;

import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserServiceImplTest {
    private ActorSystem system;
    private UserService service;

    @Before
    public void setUp() {
        system = ActorSystem.create();
        Cluster cluster = Cluster.get(system);
        cluster.join(cluster.selfAddress());
        service = new UserServiceImpl(system);
    }

    @After
    public void tearDown() {
        TestKit.shutdownActorSystem(system);
    }

    @Test
    public void testGetAndUpdate() {
        var accreditation = service.get("user").toCompletableFuture().join();
        assertThat(accreditation.getUserId(), equalTo("user"));
        assertThat(accreditation.isAccredited(), equalTo(false));

        service.update("user", true);
        accreditation = service.get("user").toCompletableFuture().join();
        assertThat(accreditation.isAccredited(), equalTo(true));
    }

    @Test
    public void testSubscribe() {
        var subscriber = new TestProbe(system);

        service.subscribe("user", subscriber.ref());
        var accreditation = subscriber.expectMsgClass(Accreditation.class);
        assertThat(accreditation.getUserId(), equalTo("user"));
        assertThat(accreditation.isAccredited(), equalTo(false));

        service.update("user", true);
        accreditation = subscriber.expectMsgClass(Accreditation.class);
        assertThat(accreditation.isAccredited(), equalTo(true));
    }
}
