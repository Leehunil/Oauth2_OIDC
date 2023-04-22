package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohsoontaxi.login.domain.credential.domain.RefreshTokenRedisEntity;
import ohsoontaxi.login.domain.credential.domain.RefreshTokenRedisEntityRepository;
import ohsoontaxi.login.domain.credential.presentation.dto.AuthTokensResponse;
//import ohsoontaxi.login.domain.credential.presentation.dto.OauthCommonUserInfoDto;
import ohsoontaxi.login.domain.credential.presentation.dto.RegisterRequest;
import ohsoontaxi.login.domain.user.domain.User;
import ohsoontaxi.login.domain.user.domain.UserRepository;
//import ohsoontaxi.login.global.client.dto.AfterOauthResponse;
import ohsoontaxi.login.global.client.dto.AvailableRegisterResponse;
import ohsoontaxi.login.global.exception.UserNotFoundException;
import ohsoontaxi.login.global.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CredentialService {

    private final KaKaoOauthStrategy kaKaoOauthStrategy;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisEntityRepository refreshTokenRedisEntityRepository;
    private final OauthFactory oauthFactory;

//    //로그인 page 발급
//    public String getOauthLink() {
//        return kaKaoOauthStrategy.getOauthLink();
//    }

//    //
//    public AfterOauthResponse oauthCodeToUser(String code) {
//        String oauthAccessToken = kaKaoOauthStrategy.getAccessToken(code);
//        OauthCommonUserInfoDto oauthUserInfo = kaKaoOauthStrategy.getUserInfo(oauthAccessToken);
//
//        String oauthId = oauthUserInfo.getOauthId();
//        String email = oauthUserInfo.getEmail();
//        String name = oauthUserInfo.getName();
//        String gender = oauthUserInfo.getGender();
//        String phone_number = oauthUserInfo.getPhone_number();
//
//        User user =
//                userRepository
//                        .findByOauthId(oauthId)
//                        .orElseGet(
//                                ()->{
//                                    User newUser =
//                                            User.builder()
//                                                    .oauthId(oauthId)
//                                                    .email(email)
//                                                    .name(name)
//                                                    .gender(gender)
//                                                    .phone_number(phone_number)
//                                                    .build();
//                                    userRepository.save(newUser);
//
//                                    return newUser;
//                                });
//
//        Long userId = user.getId();
//
//        String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getAccountRole());
//        String refreshToken = generateRefreshToken(userId);
//
//        return AfterOauthResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    private String generateRefreshToken(Long userId) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        Long tokenExpiredAt = jwtTokenProvider.getRefreshTokenTTlSecond();
        RefreshTokenRedisEntity build =
                RefreshTokenRedisEntity.builder()
                        .id(userId.toString())
                        .ttl(tokenExpiredAt)
                        .refreshToken(refreshToken)
                        .build();
        refreshTokenRedisEntityRepository.save(build);
        return refreshToken;
    }

    /**
     * decodePayload의 회원번호와 oauthprovider로 유저가 있는지 확인
     * true : 회원가입하지 않은 회원
     * false : 회원가입한 회원
     */
    private Boolean checkUserCanRegister(
            OIDCDecodePayload oidcDecodePayload, OauthProvider oauthProvider) {
        Optional<User> user =
                userRepository.findByOauthIdAndOauthProvider(
                        oidcDecodePayload.getSub(), oauthProvider.getValue());
        return user.isEmpty();
    }


    /**
     * 이미 가입한 회원인지 확인
     * @param token
     * @param oauthProvider
     */
    public AvailableRegisterResponse getUserAvailableRegister(String token, OauthProvider oauthProvider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("service token = {}",token);
        OauthStrategy oauthstrategy = oauthFactory.getOauthstrategy(oauthProvider); //provider에 맞는 strategy 꺼내기
        OIDCDecodePayload oidcDecodePayload = oauthstrategy.getOIDCDecodePayload(token); //공개 키 가져와서 idtoken 검증
        Boolean isRegistered = !checkUserCanRegister(oidcDecodePayload, oauthProvider); //공개 키로 회원가입 유저인지 확인(true 이미 회원가입한 회원)
        return new AvailableRegisterResponse(isRegistered);
    }

    /**
     * 회원가입
     * @param token
     * @param registerUserRequest
     * @param oauthProvider
     * @return
     */
    @Transactional
    public AuthTokensResponse registerUserByOCIDToken(
            String token, RegisterRequest registerUserRequest, OauthProvider oauthProvider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OauthStrategy oauthStrategy = oauthFactory.getOauthstrategy(oauthProvider);
        OIDCDecodePayload oidcDecodePayload = oauthStrategy.getOIDCDecodePayload(token);

        User newUser =
                User.builder()
                        .oauthProvider(oauthProvider.getValue())
                        .oauthId(oidcDecodePayload.getSub())
                        .email(oidcDecodePayload.getEmail())
                        .gender(registerUserRequest.getGender())
                        .phone_number(registerUserRequest.getPhone_number())
                        .build();
        userRepository.save(newUser);

        String accessToken =
                jwtTokenProvider.generateAccessToken(newUser.getId(), newUser.getAccountRole());
        String refreshToken = generateRefreshToken(newUser.getId());

        return AuthTokensResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 로그인
     * @param token
     * @param oauthProvider
     * @return
     */
    public AuthTokensResponse loginUserByOCIDToken(String token, OauthProvider oauthProvider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OauthStrategy oauthStrategy = oauthFactory.getOauthstrategy(oauthProvider);
        OIDCDecodePayload oidcDecodePayload = oauthStrategy.getOIDCDecodePayload(token);

        User user =
                userRepository
                        .findByOauthIdAndOauthProvider(
                                oidcDecodePayload.getSub(), oauthProvider.getValue())
                        .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        String accessToken =
                jwtTokenProvider.generateAccessToken(user.getId(), user.getAccountRole());

        String refreshToken = generateRefreshToken(user.getId());

        return AuthTokensResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 로그아웃
     */

//    @Transactional
//    public void logoutUser() {
//        User user = userUtils.getUserFromSecurityContext();
//        refreshTokenRedisEntityRepository.deleteById(user.getId().toString());
//    }
}
