package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CredentialService {

    private final KaKaoOauthStrategy kaKaoOauthStrategy;

    //로그인 page 발급
    public String getOauthLink() {
        return kaKaoOauthStrategy.getOauthLink();
    }

    //
}
