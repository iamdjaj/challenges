package yieldstreet.verification;

import lombok.Value;

/** A response from the verification service. */
@Value
public class VerificationResponse {
    String  requestId;
    boolean accredited;
}
