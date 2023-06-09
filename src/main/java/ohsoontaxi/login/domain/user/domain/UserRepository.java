package ohsoontaxi.login.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findByOauthId(String oauthId);
    Optional<User> findByOauthIdAndOauthProvider(String oauthId, String oauthProvider);
}
