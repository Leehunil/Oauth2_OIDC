package ohsoontaxi.login.global.exception;

import ohsoontaxi.login.global.error.exception.ErrorCode;
import ohsoontaxi.login.global.error.exception.OhSoonException;

public class UserNotFoundException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}