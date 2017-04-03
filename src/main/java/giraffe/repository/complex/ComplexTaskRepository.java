package giraffe.repository.complex;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.complex.ComplexTask;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Repository
public interface ComplexTaskRepository extends GiraffeRepository<ComplexTask> {

    Iterable<ComplexTask> findByParentAndStatus(ComplexTask parent, GiraffeEntity.Status status);

    Page<ComplexTask> findByProjectAndStatus(Project project, GiraffeEntity.Status status, Pageable page);

    Page<ComplexTask> findByPeriodAndStatus(Period period, GiraffeEntity.Status status, Pageable page);

}
