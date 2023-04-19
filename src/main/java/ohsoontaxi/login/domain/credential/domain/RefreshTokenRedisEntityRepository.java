package ohsoontaxi.login.domain.credential.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisEntityRepository
        extends CrudRepository<RefreshTokenRedisEntity, String> {
    Optional<RefreshTokenRedisEntity> findByRefreshToken(String refreshToken);
}
