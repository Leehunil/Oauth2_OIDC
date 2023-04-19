package ohsoontaxi.login.domain.credential.service;

//import ohsoontaxi.login.domain.credential.presentation.dto.OauthCommonUserInfoDto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface OauthStrategy {

//    OauthCommonUserInfoDto getUserInfo(String oauthAccessToken);
//
//    String getOauthLink();
//
//    String getAccessToken(String code);

    OIDCDecodePayload getOIDCDecodePayload(String token) throws NoSuchAlgorithmException, InvalidKeySpecException;

//    void unLink(String oauthAccessToken);
}