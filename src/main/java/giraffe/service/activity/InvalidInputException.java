package giraffe.service.activity;

import giraffe.domain.GiraffeException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class InvalidInputException extends GiraffeException {

    public InvalidInputException(String fieldsWithError) {
        super(String.format("Next input parameters are either invalid or not present: %s", fieldsWithError));

    }

    @Override
    public Integer getErrorCode() {
        return 1310;
    }

}
