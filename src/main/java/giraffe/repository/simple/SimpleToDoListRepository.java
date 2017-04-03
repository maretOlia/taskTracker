package giraffe.repository.simple;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface SimpleToDoListRepository extends GiraffeRepository<SimpleToDoList> {

    Iterable<SimpleToDoList> findByCreatedByAndStatus(User user, GiraffeEntity.Status status);

}
