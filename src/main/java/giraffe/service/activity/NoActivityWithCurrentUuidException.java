package giraffe.service.activity;

import giraffe.domain.GiraffeException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class NoActivityWithCurrentUuidException extends GiraffeException {

    public NoActivityWithCurrentUuidException(String uuid) {
        super(String.format("Provided uuid isn't related to any 'ACTIVE' activity. Provided uuid: %s", uuid));
    }

    @Override
    public Integer getErrorCode() {
        return 1300;
    }

}
