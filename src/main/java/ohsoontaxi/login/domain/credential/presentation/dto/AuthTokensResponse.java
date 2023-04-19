package ohsoontaxi.login.domain.credential.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthTokensResponse {

    private final String accessToken;
    private final String refreshToken;
}
