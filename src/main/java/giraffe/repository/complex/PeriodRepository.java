package giraffe.repository.complex;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface PeriodRepository extends GiraffeRepository<Period>  {

    Iterable<Period> findByProjectAndStatus(Project project, GiraffeEntity.Status status);

}
