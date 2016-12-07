package giraffe.domain;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract public class GiraffeException extends Exception {

    public GiraffeException(String message) {
        super(message);
    }

    abstract public Integer getErrorCode();


    public static class AccountWithCurrentLoginExistsException extends GiraffeException {

        public AccountWithCurrentLoginExistsException(String login) {
            super("User with login: : " + login + ". already exists");
        }

        @Override
        public Integer getErrorCode() {
            return 1100;
        }
    }


    public static class ErrorResponse {

        private int errorCode;

        private String message;


        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
