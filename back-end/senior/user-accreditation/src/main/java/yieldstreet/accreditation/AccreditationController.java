package yieldstreet.accreditation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import yieldstreet.verification.VerificationRequest;

public class AccreditationController extends Controller {
    private final ObjectMapper objectMapper;

    @Inject
    public AccreditationController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CompletionStage<Result> accreditation(Http.Request httpRequest) throws JsonProcessingException {
        var requestId = UUID.randomUUID().toString();
        var body = objectMapper.treeToValue(httpRequest.body().asJson(), RequestBody.class);
        var request = new VerificationRequest(requestId, body.getAccreditation());

        // TODO: this is where your solution comes in
        // It should provide consistent response times shielding clients from
        // the wildly varying latencies and errors of the verification service.
        // You don't have to wait for a request to be processed by the
        // verification service before returning from this endpoint.
        return CompletableFuture.completedFuture(TODO(httpRequest));
    }

    @Data
    public static class RequestBody {
        String     userId;
        ObjectNode accreditation;
    }
}
