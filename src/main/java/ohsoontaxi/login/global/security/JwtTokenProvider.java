package ohsoontaxi.login.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ohsoontaxi.login.domain.user.domain.AccountRole;
import ohsoontaxi.login.global.property.JwtProperties;
import ohsoontaxi.login.global.security.Auth.AuthDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";

    private final String ROLE = "role";
    private final String TYPE = "type";

    //발행자
    private final String ISSUER = "knockknock";

    public String resolveToken(HttpServletRequest request) {
        String rawHeader = request.getHeader(jwtProperties.getHeader());

        if (rawHeader != null
                && rawHeader.length() > jwtProperties.getPrefix().length()
                && rawHeader.startsWith(jwtProperties.getPrefix())) {
            return rawHeader.substring(jwtProperties.getPrefix().length() + 1);
        }
        return null;
    }

    //Authentication 가져오기
    public Authentication getAuthentication(String token) {
        String id = getJws(token).getBody().getSubject();
        String role = (String) getJws(token).getBody().get(ROLE);
        UserDetails userDetails = new AuthDetails(id, role);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    private Jws<Claims> getJws(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
    }

    //키 해싱
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    //accessToken build
    private String buildAccessToken(
            Long id, Date issuedAt, Date accessTokenExpiresIn, AccountRole accountRole) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim(TYPE, ACCESS_TOKEN)
                .claim(ROLE, accountRole.getValue())
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    //refreshToken build
    private String buildRefreshToken(Long id, Date issuedAt, Date accessTokenExpiresIn) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim(TYPE, REFRESH_TOKEN)
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    //accessToken 만들기
    public String generateAccessToken(Long id, AccountRole accountRole) {
        final Date issuedAt = new Date();
        final Date accessTokenExpiresIn =
                new Date(issuedAt.getTime() + jwtProperties.getAccessExp() * 1000);

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, accountRole);
    }

    public String generateRefreshToken(Long id) {
        final Date issuedAt = new Date();
        final Date refreshTokenExpiresIn =
                new Date(issuedAt.getTime() + jwtProperties.getRefreshExp() * 1000);
        return buildRefreshToken(id, issuedAt, refreshTokenExpiresIn);
    }

    public boolean isAccessToken(String token) {
        return getJws(token).getBody().get(TYPE).equals(ACCESS_TOKEN);
    }

    public boolean isRefreshToken(String token) {
        return getJws(token).getBody().get(TYPE).equals(REFRESH_TOKEN);
    }

    public Long parseAccessToken(String token) {
        if (isAccessToken(token)) {
            Claims claims = getJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } else {
            return null;
        }
    }

    public Long parseRefreshToken(String token) {
        if (isRefreshToken(token)) {
            Claims claims = getJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } else {
            return null;
        }
    }

    public Long getRefreshTokenTTlSecond() {
        return jwtProperties.getRefreshExp();
    }
}