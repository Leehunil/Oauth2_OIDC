package ohsoontaxi.login.global.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AfterOauthResponse {

    private String accessToken;

    private String refreshToken;

    private Boolean isRegistered;
}
