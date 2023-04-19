package ohsoontaxi.login.global.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohsoontaxi.login.domain.credential.service.OIDCDecodePayload;
import ohsoontaxi.login.global.property.JwtProperties;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtOIDCProvider {

    private final JwtProperties jwtProperties;

    private final String KID = "kid";

    /**
     * Header에서 KID를 가져온다.
     * @param token
     * @param iss
     * @param aud
     */
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        return (String) getUnsignedTokenClaims(token, iss, aud).getHeader().get(KID);
    }

    /**
     * JWT를 파싱해서 검증
     * @param token
     * @param iss
     * @param aud
     */
    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) {
        return Jwts.parserBuilder() //JWT 파서를 생성하기 위한 빌더 객체
                .requireAudience(aud) //발급된 앱의 키 검증
                .requireIssuer(iss) //발급한 인증 기관 검증
                .build()
                .parseClaimsJwt(getUnsignedToken(token)); //파싱할 JWT토큰을 전달 Header와 Claims를 추출
    }

    /**
     * 토큰 SIGNATURE파트는 날리고 헤더랑 페이로드만 가져옴
     */
    private String getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");
        return splitToken[0] + "." + splitToken[1] + ".";
    }

    /**
     * Claims 추출
     * @param token
     * @param modulus
     * @param exponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder()
                .setSigningKey(getRSAPublicKey(modulus, exponent)) //서명 생성
                .build()
                .parseClaimsJws(token); //claim 추출
    }

    /**
     * claims에서 issuer, audience, subject, email 빼와서 decodePayload에 저장
     * @param token
     * @param modulus
     * @param exponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Claims body = getOIDCTokenJws(token, modulus, exponent).getBody();
        return new OIDCDecodePayload(
                body.getIssuer(),
                body.getAudience(),
                body.getSubject(),
                body.get("email", String.class));
    }

    /**
     * modulus와 exponent를 사용해서 RSA 공개키를 생성 메서드
     * @param modulus
     * @param exponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }
}
