package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class OauthFactory {

    private final Map<String, OauthStrategy> oauthStrategyMap;

    public OauthStrategy getOauthstrategy(OauthProvider oauthProvider) {

        return oauthStrategyMap.get(oauthProvider.getValue());
    }
}
