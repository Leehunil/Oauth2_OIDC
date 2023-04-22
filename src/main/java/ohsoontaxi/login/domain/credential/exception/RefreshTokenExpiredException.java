package ohsoontaxi.login.domain.credential.exception;

import ohsoontaxi.login.global.error.exception.ErrorCode;
import ohsoontaxi.login.global.error.exception.OhSoonException;

public class RefreshTokenExpiredException extends OhSoonException {
    public static final OhSoonException EXCEPTION = new RefreshTokenExpiredException();

    private RefreshTokenExpiredException() {
        super(ErrorCode.REGISTER_EXPIRED_TOKEN);
    }
}
