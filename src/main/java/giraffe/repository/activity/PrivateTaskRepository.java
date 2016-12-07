package giraffe.repository.activity;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.account.User;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface PrivateTaskRepository extends GiraffeRepository<PrivateTask> {

    Iterable<PrivateTask> findByAssignedToAndStatus(User user, GiraffeEntity.Status status);

    Iterable<PrivateTask> findByOpenedByAndStatus(User user, GiraffeEntity.Status status);

    Iterable<PrivateTask> findByParentAndStatus(PrivateTask parent, GiraffeEntity.Status status);

}
