package ohsoontaxi.login.domain.credential.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohsoontaxi.login.domain.credential.presentation.dto.AuthTokensResponse;
import ohsoontaxi.login.domain.credential.presentation.dto.RegisterRequest;
import ohsoontaxi.login.domain.credential.service.CredentialService;
import ohsoontaxi.login.domain.credential.service.OauthProvider;
import ohsoontaxi.login.global.client.dto.AvailableRegisterResponse;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1/credentials")
@RequiredArgsConstructor
@Slf4j
public class CredentialController {

    private final CredentialService credentialService;

    //id token 검증
    @GetMapping("/oauth/valid/register")
    public AvailableRegisterResponse valid(
            @RequestParam("id_token") String token,
            @RequestParam("provider")OauthProvider oauthProvider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("controller token = {}",token);
        return credentialService.getUserAvailableRegister(token, oauthProvider);
    }

    //회원가입
    @PostMapping("/register")
    public AuthTokensResponse registerUser(
            @RequestParam("id_token") String token,
            @RequestParam("provider") OauthProvider oauthProvider,
            @RequestBody RegisterRequest registerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return credentialService.registerUserByOCIDToken(token, registerRequest, oauthProvider);
    }

    //로그인
    @PostMapping("/login")
    public AuthTokensResponse loginUser(
            @RequestParam("id_token") String token,
            @RequestParam("provider") OauthProvider oauthProvider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return credentialService.loginUserByOCIDToken(token, oauthProvider);
    }
}
