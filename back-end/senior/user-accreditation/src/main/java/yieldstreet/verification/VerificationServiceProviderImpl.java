package yieldstreet.verification;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificationServiceProviderImpl implements VerificationServiceProvider {
    private final Logger       logger;
    private final SecureRandom random;
    private final Executor     executor;

    public VerificationServiceProviderImpl() throws NoSuchAlgorithmException {
        this.logger = LoggerFactory.getLogger(VerificationServiceProviderImpl.class);
        this.random = SecureRandom.getInstanceStrong();
        this.executor = Executors.newFixedThreadPool(4);
    }

    @Override
    public CompletionStage<VerificationResponse> verify(VerificationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            waitForSimulatedLatency(request);

            if (shouldFail(request)) {
                logger.debug("Failing verification request {}", request.getRequestId());
                throw new VerificationException("request failed");
            }

            var accreditedNode = request.getPayload().path("accredited");
            var accredited = !accreditedNode.isBoolean() || accreditedNode.booleanValue();

            logger.debug("Completing verification request {} with accredited = {}",
                request.getRequestId(), accredited);

            return new VerificationResponse(request.getRequestId(), accredited);
        }, executor);
    }

    private void waitForSimulatedLatency(VerificationRequest request) {
        try {
            long latency = latency(request);

            logger.debug("Waiting {}ms before completing verification request {}",
                latency, request.getRequestId());

            Thread.sleep(latency);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final double FAIL_CHANCE  = 0.02;
    private static final long   LATENCY_MIN  = 50;
    private static final long   LATENCY_MAX  = 5000;
    private static final long   LATENCY_SKEW = 3;

    /**
     * Get the failure flag for a given request. If the request payload includes
     * a property named {@code fail} with a boolean value, it will determine
     * whether the request fails or not; otherwise there's a 2% chance any
     * request will fail.
     *
     * @param request the verification request.
     */
    private boolean shouldFail(VerificationRequest request) {
        JsonNode fail = request.getPayload().path("fail");
        if (fail.isBoolean()) return fail.booleanValue();
        return random.nextDouble() < FAIL_CHANCE;
    }

    /**
     * Get the simulated latency for a given request. If the request payload
     * includes a property named {@code delay} with an integral number value,
     * that will be the request delay in milliseconds. If the {@code delay}
     * property is a boolean with a {@code true} value, the request will have
     * the maximum latency of 5 seconds; otherwise a random number between 50ms
     * and 5s will determine the request latency.
     *
     * @param request the verification request.
     * @see #latency()
     */
    private long latency(VerificationRequest request) {
        JsonNode delay = request.getPayload().path("delay");

        if (delay.isIntegralNumber()) {
            return delay.longValue();
        }

        if (delay.isBoolean() && delay.booleanValue()) {
            return LATENCY_MAX;
        }

        return latency();
    }

    /** Generate a random latency in a uniform distribution from 50ms to 5s */
    private long latency() {
        double num = -1;

        while (num > 1 || num < 0) {
            double u = 0, v = 0;
            while (u == 0) u = random.nextDouble();
            while (v == 0) v = random.nextDouble();

            num = Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v);
            num = num / 10 + 0.5;
        }

        logger.debug("Generated random gaussian {}", num);

        double skewed = Math.pow(num, LATENCY_SKEW);
        logger.debug("After adjusting skew: {}", skewed);

        return (long) (skewed * (LATENCY_MAX - LATENCY_MIN) + LATENCY_MIN);
    }

}
