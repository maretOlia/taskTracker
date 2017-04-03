package giraffe.service.activity.complex;

import giraffe.domain.GiraffeException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class GiraffeAccessDeniedException extends GiraffeException {

    public GiraffeAccessDeniedException(String userUuid, String projectUuid) {
        super(String.format("User access to all project content denied.\\nUser \'uuid\': %s" + "\\nProject \'uuid\': %s",
                userUuid, projectUuid));
    }

    @Override
    public Integer getErrorCode() {
        return 1410;
    }

}
