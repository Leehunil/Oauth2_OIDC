package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import ohsoontaxi.login.domain.credential.presentation.dto.OauthCommonUserInfoDto;
import ohsoontaxi.login.global.client.KakaoInfoClient;
import ohsoontaxi.login.global.client.KakaoOauthClient;
import ohsoontaxi.login.global.client.dto.KakaoInformationResponse;
import ohsoontaxi.login.global.property.OauthProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static ohsoontaxi.login.global.client.dto.KakaoInformationResponse.*;

@AllArgsConstructor
@Component
public class KaKaoOauthStrategy {

    private static final String QUERY_STRING =
            "/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String PREFIX = "Bearer ";
    private final OauthProperties oauthProperties;
    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoInfoClient kakaoInfoClient;

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
        return kakaoOauthClient
                .kakaoAuth(
                        oauthProperties.getClientId(),
                        oauthProperties.getRedirectUrl(),
                        code,
                        oauthProperties.getClientSecret()
                ).getAccessToken();
    }

    //발급된 어세스 토큰으로 유저정보 조회
    public OauthCommonUserInfoDto getUserInfo(String oauthAccessToken){
        KakaoInformationResponse response =
                kakaoInfoClient.kakaoUserInfo(PREFIX + oauthAccessToken);
        KakaoAcoount kakaoAccount = response.getKakaoAcoount();
        String oauthId = response.getId();
        OauthCommonUserInfoDto.OauthCommonUserInfoDtoBuilder oauthCommonUserInfoDtoBuilder =
                OauthCommonUserInfoDto.builder()
                .oauthId(oauthId);
        //계정 정보가 null이 아니면
        if (kakaoAccount != null){
            String email = kakaoAccount.getEmail();
            if (email != null) {
                oauthCommonUserInfoDtoBuilder.email(email);
            }
            String name = kakaoAccount.getName();
            if (name != null) {
                oauthCommonUserInfoDtoBuilder.name(name);
            }
            String gender = kakaoAccount.getGender();
            if (gender != null) {
                oauthCommonUserInfoDtoBuilder.gender(gender);
            }
            String phone_number = kakaoAccount.getPhone_number();
            if (phone_number != null) {
                oauthCommonUserInfoDtoBuilder.phone_number(phone_number);
            }
        }
        return oauthCommonUserInfoDtoBuilder.build();
    }
}
