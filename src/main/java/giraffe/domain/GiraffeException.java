package giraffe.domain;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract public class GiraffeException extends Exception {

    public GiraffeException(final String message) {
        super(message);
    }

    abstract public Integer getErrorCode();


    public static class CanNotDeleteTaskWithLinkedSubtasksException extends GiraffeException {

        public CanNotDeleteTaskWithLinkedSubtasksException(final String uuid) {
            super("Can not delete task with uuid: " + uuid + ". Current task have linked subtasks");
        }

        @Override
        public Integer getErrorCode() {
            return 1000;
        }
    }

    public static class AccountWithCurrentLoginExistsException extends GiraffeException {

        public AccountWithCurrentLoginExistsException(final String login) {
            super("Account with login: : " + login + ". already exists");
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

        public void setErrorCode(final int errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }
    }

}
