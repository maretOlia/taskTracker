package giraffe.service.activity;

import giraffe.domain.activity.Task;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public abstract class TaskManagementService<T extends Task> extends ActivityManagementService<T>{

    public abstract Iterable<T> findByParent(String userUuid, String parentUuid) throws GiraffeAccessDeniedException;

    public abstract T delete(String userUuid, String uuid) throws GiraffeAccessDeniedException;

}
