package ohsoontaxi.login.global.api.client;

//import ohsoontaxi.login.domain.credential.presentation.dto.OauthAccessTokenResponse;
import ohsoontaxi.login.domain.credential.service.OauthStrategy;
import ohsoontaxi.login.global.client.dto.OIDCPublicKeysResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "KakaoAuthClient", url = "https://kauth.kakao.com")
public interface KakaoOauthClient {

//    @PostMapping(
//            "/oauth/token?grant_type=authorization_code&client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&code={CODE}&client_secret={CLIENT_SECRET}")
//    OauthAccessTokenResponse kakaoAuth(
//            @PathVariable("CLIENT_ID") String clientId,
//            @PathVariable("REDIRECT_URI") String redirectUri,
//            @PathVariable("CODE") String code,
//            @PathVariable("CLIENT_SECRET") String client_secret);

    /**
     * 공개키 목록을 빈번하게 조회할 경우 요청이 차단된다.
     * 그래서 캐싱해서 사용해야 한다.
     * redis 캐싱을 사용
     */
    @Cacheable(cacheNames = "KakaoOICD", cacheManager = "oidcCacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeysResponse getKakaoOIDCOpenKeys();
}
