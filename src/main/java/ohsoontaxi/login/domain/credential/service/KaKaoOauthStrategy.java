package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
//import ohsoontaxi.login.domain.credential.presentation.dto.OauthCommonUserInfoDto;
//import ohsoontaxi.login.global.client.KakaoInfoClient;
import lombok.extern.slf4j.Slf4j;
import ohsoontaxi.login.global.api.client.KakaoOauthClient;
//import ohsoontaxi.login.global.client.dto.KakaoInformationResponse;
import ohsoontaxi.login.global.client.dto.OIDCPublicKeysResponse;
import ohsoontaxi.login.global.property.OauthProperties;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

//import static ohsoontaxi.login.global.client.dto.KakaoInformationResponse.*;

@AllArgsConstructor
@Component("KAKAO")
@Slf4j
public class KaKaoOauthStrategy implements OauthStrategy{

//    private static final String QUERY_STRING =
//            "/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
//    private static final String PREFIX = "Bearer ";
    private final OauthProperties oauthProperties;
    private final KakaoOauthClient kakaoOauthClient;
//    private final KakaoInfoClient kakaoInfoClient;
    private final OauthOIDCProvider oauthOIDCProvider;

    private static final String ISSUER = "https://kauth.kakao.com";

    //oauthLink 발급(로그인 페이지 발급)
//    public String getOauthLink() {
//        return oauthProperties.getBaseUrl()
//                + String.format(
//                        QUERY_STRING,
//                        oauthProperties.getClientId(),
//                        oauthProperties.getRedirectUrl());
//    }
//
//    //어세스토큰 발급
//    public String getAccessToken(String code) {
//        return kakaoOauthClient
//                .kakaoAuth(
//                        oauthProperties.getClientId(),
//                        oauthProperties.getRedirectUrl(),
//                        code,
//                        oauthProperties.getClientSecret()
//                ).getAccessToken();
//    }
//
//    //발급된 어세스 토큰으로 유저정보 조회
//    public OauthCommonUserInfoDto getUserInfo(String oauthAccessToken){
//        KakaoInformationResponse response =
//                kakaoInfoClient.kakaoUserInfo(PREFIX + oauthAccessToken);
//        KakaoAcoount kakaoAccount = response.getKakaoAcoount();
//        String oauthId = response.getId();
//        OauthCommonUserInfoDto.OauthCommonUserInfoDtoBuilder oauthCommonUserInfoDtoBuilder =
//                OauthCommonUserInfoDto.builder()
//                .oauthId(oauthId);
//        //계정 정보가 null이 아니면
//        if (kakaoAccount != null){
//            String email = kakaoAccount.getEmail();
//            if (email != null) {
//                oauthCommonUserInfoDtoBuilder.email(email);
//            }
//            String name = kakaoAccount.getName();
//            if (name != null) {
//                oauthCommonUserInfoDtoBuilder.name(name);
//            }
//            String gender = kakaoAccount.getGender();
//            if (gender != null) {
//                oauthCommonUserInfoDtoBuilder.gender(gender);
//            }
//            String phone_number = kakaoAccount.getPhone_number();
//            if (phone_number != null) {
//                oauthCommonUserInfoDtoBuilder.phone_number(phone_number);
//            }
//        }
//        return oauthCommonUserInfoDtoBuilder.build();
//    }

    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        log.info("strategy token = {}",token);
        OIDCPublicKeysResponse oidcPublicKeysResponse = kakaoOauthClient.getKakaoOIDCOpenKeys(); //@feign을 이용해서 공개키 배열 가져오기(keys 리스트)
        log.info("oidcPublicKeysResponse={}",oidcPublicKeysResponse);
        return oauthOIDCProvider.getPayloadFromIdToken(
                token, ISSUER, oauthProperties.getAppId(), oidcPublicKeysResponse); //id token 검증
    }

}
