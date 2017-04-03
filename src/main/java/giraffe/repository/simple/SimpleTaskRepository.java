package giraffe.repository.simple;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.simple.SimpleTask;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface SimpleTaskRepository extends GiraffeRepository<SimpleTask> {

    Iterable<SimpleTask> findByParentAndStatus(SimpleTask parent, GiraffeEntity.Status status);

    Page<SimpleTask> findBySimpleToDoListAndStatus(SimpleToDoList namedToDoList, GiraffeEntity.Status status, Pageable page);

    Page<SimpleTask> findByAssignedToAndStatus(User user, GiraffeEntity.Status status, Pageable page);

}
