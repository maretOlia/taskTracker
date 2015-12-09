package giraffe;

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

}
