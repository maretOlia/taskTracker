package giraffe.service.activity;

import giraffe.domain.activity.Activity;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public abstract class ActivityManagementService<T extends Activity> {

    public abstract T findByUuid(String userUuid, String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException;

}
