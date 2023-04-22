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
    @Column(name = "user_id")
    private Long id;

    private String oauthProvider;

    private String oauthId;

    private String email;

    private String name;

    private String gender;

    private String phone_number;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole = AccountRole.USER;

    @Builder
    public User(
            Long id,
            String oauthId,
            String oauthProvider,
            String email,
            String name,
            String gender,
            String phone_number) {
        this.id = id;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.phone_number = phone_number;
    }
}
