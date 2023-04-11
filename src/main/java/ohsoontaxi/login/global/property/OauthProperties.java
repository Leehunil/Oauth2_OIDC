package ohsoontaxi.login.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("oauth.kakao")
public class OauthProperties {

    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String appId;

}
