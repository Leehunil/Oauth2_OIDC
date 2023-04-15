package ohsoontaxi.login.domain.credential.service;

import lombok.AllArgsConstructor;
import ohsoontaxi.login.domain.credential.presentation.dto.OauthCommonUserInfoDto;
import ohsoontaxi.login.domain.user.domain.User;
import ohsoontaxi.login.domain.user.domain.UserRepository;
import ohsoontaxi.login.global.client.dto.AfterOauthResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CredentialService {

    private final KaKaoOauthStrategy kaKaoOauthStrategy;
    private final UserRepository userRepository;
    private final

    //로그인 page 발급
    public String getOauthLink() {
        return kaKaoOauthStrategy.getOauthLink();
    }

    //
    public AfterOauthResponse oauthCodeToUser(String code) {
        String oauthAccessToken = kaKaoOauthStrategy.getAccessToken(code);
        OauthCommonUserInfoDto oauthUserInfo = kaKaoOauthStrategy.getUserInfo(oauthAccessToken);

        String oauthId = oauthUserInfo.getOauthId();
        String email = oauthUserInfo.getEmail();
        String name = oauthUserInfo.getName();
        String gender = oauthUserInfo.getGender();
        String phone_number = oauthUserInfo.getPhone_number();

        User user =
                userRepository
                        .findByOauthId(oauthId)
                        .orElseGet(
                                ()->{
                                    User newUser =
                                            User.builder()
                                                    .oauthId(oauthId)
                                                    .email(email)
                                                    .name(name)
                                                    .gender(gender)
                                                    .phone_number(phone_number)
                                                    .build();
                                    userRepository.save(newUser);

                                    return newUser;
                                });

        return AfterOauthResponse.builder()
                .accessToken()
                .refreshToken()
                .build();

    }
}
