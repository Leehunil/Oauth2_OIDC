package ohsoontaxi.login.domain.credential.service;

import lombok.RequiredArgsConstructor;
import ohsoontaxi.login.global.client.dto.OIDCPublicKeyDto;
import ohsoontaxi.login.global.client.dto.OIDCPublicKeysResponse;
import ohsoontaxi.login.global.security.JwtOIDCProvider;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
@RequiredArgsConstructor
public class OauthOIDCProvider {

    private final JwtOIDCProvider jwtOIDCProvider;

    /**
     * 서명되지않은 idToken에서 공개 키(kid)가져오기
     * @param token
     * @param iss
     * @param aud
     *
     */
    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
        return jwtOIDCProvider.getKidFromUnsignedTokenHeader(token, iss, aud);
    }

    /**
     * idToken 헤더에서 꺼낸 kid와 서버에서 가져온 kid가 같은지 확인하고 body에서 OIDCDECODEPAYLOAD정보 빼온다.
     * @param token
     * @param iss
     * @param aud
     * @param oidcPublicKeysResponse
     */
    public OIDCDecodePayload getPayloadFromIdToken(
            String token, String iss, String aud, OIDCPublicKeysResponse oidcPublicKeysResponse) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String kid = getKidFromUnsignedIdToken(token, iss, aud);

        OIDCPublicKeyDto oidcPublicKeyDto =
                oidcPublicKeysResponse.getKeys().stream()
                        .filter(o -> o.getKid().equals(kid))
                        .findFirst()
                        .orElseThrow();

        return (OIDCDecodePayload)
                jwtOIDCProvider.getOIDCTokenBody(
                        token, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
    }
}
