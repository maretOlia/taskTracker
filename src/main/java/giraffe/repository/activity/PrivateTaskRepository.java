package giraffe.repository.activity;

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

    Iterable<PrivateTask> findByAssignedTo(User user);

    Iterable<PrivateTask> findByOpenedBy(User user);

    Iterable<PrivateTask> findByParent(PrivateTask parent);

}
