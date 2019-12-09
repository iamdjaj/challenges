package yieldstreet.verification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

public class VerificationServiceProviderImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ObjectMapper                objectMapper;
    private VerificationServiceProvider verificationServiceProvider;

    @Before
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.verificationServiceProvider = new VerificationServiceProviderImpl();
    }

    @Test
    public void testFail() {
        VerificationRequest request = new VerificationRequest("id",
            objectMapper.createObjectNode().put("fail", true));

        thrown.expectCause(isA(VerificationException.class));
        verificationServiceProvider.verify(request).toCompletableFuture().join();
    }

    @Test
    public void testSuccess() {
        VerificationRequest request = new VerificationRequest("id",
            objectMapper.createObjectNode()
                .put("fail", false)
                .put("delay", 0));

        var response = verificationServiceProvider.verify(request).toCompletableFuture().join();
        assertThat(response.getRequestId(), equalTo(request.getRequestId()));
        assertThat(response.isAccredited(), equalTo(true));
    }

    @Test
    public void testDelay() throws InterruptedException {
        VerificationRequest request = new VerificationRequest("id",
            objectMapper.createObjectNode().put("delay", 250));

        var future = verificationServiceProvider.verify(request).toCompletableFuture();
        Thread.sleep(200);
        assertThat(future.isDone(), equalTo(false));

        Thread.sleep(100);
        assertThat(future.isDone(), equalTo(true));

        var response = future.join();
        assertThat(response.getRequestId(), equalTo(request.getRequestId()));
        assertThat(response.isAccredited(), equalTo(true));
    }

}
