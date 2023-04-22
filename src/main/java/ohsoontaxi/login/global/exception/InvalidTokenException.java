package ohsoontaxi.login.global.exception;

import ohsoontaxi.login.global.error.exception.ErrorCode;
import ohsoontaxi.login.global.error.exception.OhSoonException;

public class InvalidTokenException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
