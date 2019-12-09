package yieldstreet.user;

import lombok.Value;
import lombok.With;

/** Reply from a {@link UserHolderMessage.GetAccreditation} message. */
@Value
public
class Accreditation {
    String userId;
    @With boolean accredited;
}
