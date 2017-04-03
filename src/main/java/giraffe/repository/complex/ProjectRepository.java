package giraffe.repository.complex;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.Project;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface ProjectRepository extends GiraffeRepository<Project> {

    Iterable<Project> findByCreatedByAndStatus(User user, GiraffeEntity.Status status);

}
