package ohsoontaxi.login.global.exception;

import ohsoontaxi.login.global.error.exception.ErrorCode;
import ohsoontaxi.login.global.error.exception.OhSoonException;

public class ExpiredTokenException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}