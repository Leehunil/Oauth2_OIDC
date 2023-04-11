package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import ohsoontaxi.login.global.property.OauthProperties;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KaKaoOauthStrategy {

    private static final String QUERY_STRING =
            "/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private final OauthProperties oauthProperties;

    //oauthLink 발급(로그인 페이지 발급)
    public String getOauthLink() {
        return oauthProperties.getBaseUrl()
                + String.format(
                        QUERY_STRING,
                        oauthProperties.getClientId(),
                        oauthProperties.getRedirectUrl());
    }

    //어세스토큰 발급
    public String getAccessToken(String code) {

    }
}
