package yieldstreet.accreditation;

import java.util.UUID;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import yieldstreet.user.UserService;
import yieldstreet.verification.VerificationRequest;
import yieldstreet.verification.VerificationServiceProvider;

public class AccreditationController extends Controller {
    private final ObjectMapper                objectMapper;
    private final VerificationServiceProvider verificationServiceProvider;
    private final UserService                 userService;

    @Inject
    public AccreditationController(
        ObjectMapper objectMapper,
        VerificationServiceProvider verificationServiceProvider,
        UserService userService) {
        this.objectMapper = objectMapper;
        this.verificationServiceProvider = verificationServiceProvider;
        this.userService = userService;
    }

    public CompletionStage<Result> accreditation(Http.Request httpRequest) throws JsonProcessingException {
        var requestId = UUID.randomUUID().toString();
        var body = objectMapper.treeToValue(httpRequest.body().asJson(), RequestBody.class);
        var request = new VerificationRequest(requestId, body.getAccreditation());

        return verificationServiceProvider.verify(request).thenApply(response -> {
            userService.update(body.getUserId(), response.isAccredited());
            var json = objectMapper.valueToTree(response);
            return ok(json);
        });
    }

    @Data
    public static class RequestBody {
        String     userId;
        ObjectNode accreditation;
    }
}
