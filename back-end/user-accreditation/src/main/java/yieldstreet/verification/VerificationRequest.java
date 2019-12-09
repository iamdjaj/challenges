package yieldstreet.verification;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Value;

/**
 * A request to the verifications verification service. Every request must have
 * a unique identifier, and an arbitrary JSON payload. The request id will be
 * echoed in the response.
 */
@Value
public class VerificationRequest {
    String     requestId;
    ObjectNode payload;
}
