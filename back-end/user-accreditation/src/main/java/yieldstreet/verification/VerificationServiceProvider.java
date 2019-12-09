package yieldstreet.verification;

import java.util.concurrent.CompletionStage;

public interface VerificationServiceProvider {

    /**
     * Send a  request to the verification service. Every request must have a
     * client-attributed unique identifier, which is echoed in the verification
     * response for correlation. Please note that the verification service is
     * prone to failure and can't take too many requests at once. Clients are
     * advised to throttle their requests and retry failures.
     *
     * @param request the verification request.
     * @return a promise for the verification response.
     */
    CompletionStage<VerificationResponse> verify(VerificationRequest request);

}
