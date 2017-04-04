package giraffe.service.activity.complex;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.ComplexTask;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.UserRepository;
import giraffe.repository.complex.ComplexTaskRepository;
import giraffe.repository.complex.PeriodRepository;
import giraffe.repository.complex.ProjectRepository;
import giraffe.repository.complex.security.ProjectUserRightsRepository;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class ComplexTaskManagementService extends TaskManagementService<ComplexTask> {

    private ComplexTaskRepository complexTaskRepository;

    private UserRepository userRepository;

    private ProjectRepository projectRepository;

    private PeriodRepository periodRepository;

    private ProjectUserRightsRepository projectUserRightsRepository;

    @Autowired
    public ComplexTaskManagementService(ComplexTaskRepository trivialTaskRepository,
                                        UserRepository userRepository,
                                        ProjectRepository projectRepository,
                                        PeriodRepository periodRepository,
                                        ProjectUserRightsRepository projectUserRightsRepository) {
        this.complexTaskRepository = trivialTaskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.periodRepository = periodRepository;
        this.projectUserRightsRepository = projectUserRightsRepository;
    }

    @Transactional(rollbackFor = {GiraffeAccessDeniedException.class, TimeScheduledAndPeriodInconsistencyException.class})
    public ComplexTask updateOrCreate(String userUuid,
                                      ComplexTask task,
                                      String assignedToUuid,
                                      String parentUuid,
                                      String projectUuid,
                                      String periodUuid,
                                      double progress) throws TimeScheduledAndPeriodInconsistencyException, GiraffeAccessDeniedException {
        ComplexTask oldTask = complexTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE);
        task.addProgress(progress);

        if (oldTask == null) return create(task, userUuid, assignedToUuid, parentUuid, projectUuid, periodUuid);

        if (!hasRights(userUuid, projectUuid, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)))
            throw new GiraffeAccessDeniedException(userUuid, projectUuid);

        if (oldTask.getTaskStatus() != task.getTaskStatus()) {
            task.updateTaskStatus(task.getTaskStatus());
        }
        task.setProject(
                !oldTask.getProject().getUuid().equals(projectUuid) ?
                        projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE) :
                        oldTask.getProject());

        // if assignedToUuid == null then old value in DB will be erased
        if (assignedToUuid != null) {
            task.setAssignedTo(
                    oldTask.getAssignedTo() == null || !oldTask.getAssignedTo().getUuid().equals(assignedToUuid) ?
                            userRepository.findByUuidAndStatus(assignedToUuid, GiraffeEntity.Status.ACTIVE) :
                            oldTask.getAssignedTo());
        }

        // if parentUuid == null then old value in DB will be erased
        if (parentUuid != null) {
            task.setParent(
                    oldTask.getParent() == null || !oldTask.getParent().getUuid().equals(parentUuid) ?
                            complexTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE) :
                            oldTask.getParent());
        }

        // if periodUuid == null then old value in DB will be erased
        if (periodUuid != null) {
            if (oldTask.getPeriod() == null || !oldTask.getPeriod().getUuid().equals(periodUuid)) {
                Period period = periodRepository.findByUuidAndStatus(periodUuid, GiraffeEntity.Status.ACTIVE);
                if (task.getTimeScheduled() != null && !isTimeScheduledAndPeriodConsistent(task.getTimeScheduled(), period)) {
                    throw new TimeScheduledAndPeriodInconsistencyException();
                }
            }
            task.setPeriod(
                    oldTask.getPeriod() == null || !oldTask.getPeriod().getUuid().equals(periodUuid) ?
                            periodRepository.findByUuidAndStatus(periodUuid, GiraffeEntity.Status.ACTIVE) :
                            oldTask.getPeriod());
        }

        return complexTaskRepository.save(task);
    }

    private ComplexTask create(ComplexTask task,
                               String createdByUuid,
                               String assignedToUuid,
                               String parentUuid,
                               String projectUuid,
                               String periodUuid) throws TimeScheduledAndPeriodInconsistencyException {

        task.setCreatedBy(userRepository.findByUuidAndStatus(createdByUuid, GiraffeEntity.Status.ACTIVE))
                .setProject(projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE));

        if (assignedToUuid != null) {
            task.setAssignedTo(userRepository.findByUuidAndStatus(assignedToUuid, GiraffeEntity.Status.ACTIVE));
        }
        if (parentUuid != null) {
            task.setParent(complexTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE));
        }
        if (periodUuid != null) {
            Period period = periodRepository.findByUuidAndStatus(periodUuid, GiraffeEntity.Status.ACTIVE);
            if (task.getTimeScheduled() != null && !isTimeScheduledAndPeriodConsistent(task.getTimeScheduled(), period)) {
                throw new TimeScheduledAndPeriodInconsistencyException();
            }

            task.setPeriod(period);
        }

        complexTaskRepository.save(task);

        return task;
    }

    private boolean hasRights(String userUuid, String projectUuid, Set<ProjectUserRights.Rights> rights) {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE);

        return projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, rights) != null;
    }

    private boolean isTimeScheduledAndPeriodConsistent(long timeScheduled, Period period) {
        return period.getTimeScheduledToStart() <= timeScheduled && timeScheduled <= period.getTimeScheduledToFinish();

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {GiraffeAccessDeniedException.class, NoActivityWithCurrentUuidException.class})
    public ComplexTask findByUuid(String userUuid, String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        ComplexTask task = complexTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if(task == null) throw new NoActivityWithCurrentUuidException((uuid));

        if (!hasRights(userUuid, task.getProject().getUuid(), Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)))
            throw new GiraffeAccessDeniedException(userUuid, task.getProject().getUuid());

        return task;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Iterable<ComplexTask> findByParent(String userUuid, String parentUuid) throws GiraffeAccessDeniedException {
        ComplexTask task = complexTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE);

        if (!hasRights(userUuid, task.getProject().getUuid(), Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)))
            throw new GiraffeAccessDeniedException(userUuid, task.getProject().getUuid());

        return findRecursively(task);
    }

    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Iterable<ComplexTask> findByProject(String userUuid, String projectUuid, Pageable page) throws GiraffeAccessDeniedException {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, projectUuid);

        return complexTaskRepository.findByProjectAndStatus(project, GiraffeEntity.Status.ACTIVE, page);
    }

    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Iterable<ComplexTask> findByPeriod(String userUuid, String periodUuid, Pageable page) throws GiraffeAccessDeniedException {
        Period period = periodRepository.findByUuidAndStatus(periodUuid, GiraffeEntity.Status.ACTIVE);

        if (!hasRights(userUuid, period.getProject().getUuid(), Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)))
            throw new GiraffeAccessDeniedException(userUuid, period.getProject().getUuid());

        return complexTaskRepository.findByPeriodAndStatus(period, GiraffeEntity.Status.ACTIVE, page);
    }

    @Override
    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public ComplexTask delete(String userUuid, String uuid) throws GiraffeAccessDeniedException {
        ComplexTask task = complexTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (!hasRights(userUuid, task.getProject().getUuid(), Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)))
            throw new GiraffeAccessDeniedException(userUuid, task.getProject().getUuid());

        long timeDeleted = System.currentTimeMillis();
        findRecursively(task).forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED)
                            .setTimeDeleted(timeDeleted);
                    complexTaskRepository.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED);
        task.setTimeDeleted(timeDeleted);

        return complexTaskRepository.save(task);
    }

    private Iterable<ComplexTask> findRecursively(ComplexTask parent) {
        Iterable<ComplexTask> tasks = complexTaskRepository.findByParentAndStatus(parent, GiraffeEntity.Status.ACTIVE);

        for (ComplexTask task : tasks) {
            tasks = Iterables.concat(tasks, findRecursively(task));
        }

        return tasks;
    }

}
