package ohsoontaxi.login.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String oauthProvider;

    private String oauthId;

    private String email;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole = AccountRole.USER;

    @Builder
    public User(
            Long id,
            String nickname,
            String oauthProvider,
            String oauthId,
            String email,
            String profilePath) {
        this.id = id;
        this.nickname = nickname;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.email = email;
        this.profilePath = profilePath;
    }
}
