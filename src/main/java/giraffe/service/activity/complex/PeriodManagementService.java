package giraffe.service.activity.complex;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.UserRepository;
import giraffe.repository.complex.ComplexTaskRepository;
import giraffe.repository.complex.PeriodRepository;
import giraffe.repository.complex.ProjectRepository;
import giraffe.repository.complex.security.ProjectUserRightsRepository;
import giraffe.service.activity.ActivityManagementService;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class PeriodManagementService extends ActivityManagementService<Period> {

    private PeriodRepository periodRepository;

    private ProjectRepository projectRepository;

    private ComplexTaskRepository complexTaskRepository;

    private UserRepository userRepository;

    private ProjectUserRightsRepository projectUserRightsRepository;

    public PeriodManagementService(PeriodRepository periodRepository,
                                   ProjectRepository projectRepository,
                                   ComplexTaskRepository complexTaskManagementService,
                                   UserRepository userRepository,
                                   ProjectUserRightsRepository projectUserRightsRepository) {
        this.periodRepository = periodRepository;
        this.projectRepository = projectRepository;
        this.complexTaskRepository = complexTaskManagementService;
        this.userRepository = userRepository;
        this.projectUserRightsRepository = projectUserRightsRepository;
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public Period updateOrCreate(String userUuid,
                                 Period period,
                                 String projectUuid) throws GiraffeAccessDeniedException {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, projectUuid);

        Period oldPeriod = periodRepository.findByUuidAndStatus(period.getUuid(), GiraffeEntity.Status.ACTIVE);
        period.setCreatedBy(user);
        if (oldPeriod != null) return periodRepository.save(period.setProject(oldPeriod.getProject()));

        return periodRepository.save(period.setProject(project));
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {GiraffeAccessDeniedException.class, NoActivityWithCurrentUuidException.class})
    public Period findByUuid(String userUuid, String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        Period period = periodRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (period == null) throw new NoActivityWithCurrentUuidException(uuid);

        Project project = period.getProject();
        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(
                userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE),
                project,
                Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, project.getUuid());

        return period;
    }

    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Iterable<Period> findByProject(String userUuid, String projectUuid) throws GiraffeAccessDeniedException {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, projectUuid);

        return periodRepository.findByProjectAndStatus(project, GiraffeEntity.Status.ACTIVE);
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public Period delete(String userUuid, String uuid) throws GiraffeAccessDeniedException {
        Period period = periodRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = period.getProject();

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, project.getUuid());

        complexTaskRepository.findByPeriodAndStatus(period, GiraffeEntity.Status.ACTIVE, null)
                .forEach(task -> complexTaskRepository.save(task.setPeriod(null)));

        return periodRepository.save(period
                .setStatus(GiraffeEntity.Status.DELETED)
                .setTimeDeleted(System.currentTimeMillis()));
    }

}
