package giraffe.repository.complex.security;

import giraffe.domain.User;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface ProjectUserRightsRepository extends GiraffeRepository<ProjectUserRights> {

    ProjectUserRights findByUserAndProjectAndRightsIn(User user, Project project, Set<ProjectUserRights.Rights> rights);

}
