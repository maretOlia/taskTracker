package giraffe.service.activity.complex;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.UserRepository;
import giraffe.repository.complex.ComplexTaskRepository;
import giraffe.repository.complex.PeriodRepository;
import giraffe.repository.complex.ProjectRepository;
import giraffe.repository.complex.security.ProjectUserRightsRepository;
import giraffe.service.activity.ActivityManagementService;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class ProjectManagementService extends ActivityManagementService<Project>{

    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    private ProjectUserRightsRepository projectUserRightsRepository;

    private ComplexTaskRepository complexTaskRepository;

    private PeriodRepository periodRepository;

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository,
                                    UserRepository userRepository,
                                    ProjectUserRightsRepository projectUserRightsRepository,
                                    ComplexTaskRepository complexTaskRepository,
                                    PeriodRepository periodRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectUserRightsRepository = projectUserRightsRepository;
        this.complexTaskRepository = complexTaskRepository;
        this.periodRepository = periodRepository;
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public Project updateOrCreate(String userUuid, Project project) throws GiraffeAccessDeniedException {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project oldProject = projectRepository.findByUuidAndStatus(project.getUuid(), GiraffeEntity.Status.ACTIVE);

        project.setCreatedBy(user);
        if (oldProject != null) {
            if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)) == null)
                throw new GiraffeAccessDeniedException(userUuid, oldProject.getUuid());

            return projectRepository.save(project);
        }

        // create new Project
        Project newProject = projectRepository.save(project);

        projectUserRightsRepository.save(new ProjectUserRights()
                .setUser(user)
                .setProject(project)
                .setRights(ProjectUserRights.Rights.READ_WRITE));

        return newProject;
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public Project addUserWithRights(String userUuid, String userToAddUuid, String projectUuid, ProjectUserRights.Rights rights) throws GiraffeAccessDeniedException {
        Project project = projectRepository.findByUuidAndStatus(projectUuid, GiraffeEntity.Status.ACTIVE);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(
                userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE),
                project,
                Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, project.getUuid());

        projectUserRightsRepository.save(new ProjectUserRights()
                .setProject(project)
                .setUser(userRepository.findByUuidAndStatus(userToAddUuid, GiraffeEntity.Status.ACTIVE))
                .setRights(rights));

        return project;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {GiraffeAccessDeniedException.class, NoActivityWithCurrentUuidException.class})
    public Project findByUuid(String userUuid, String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        Project project = projectRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (project == null) throw new NoActivityWithCurrentUuidException(uuid);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(
                userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE),
                project,
                Sets.newHashSet(ProjectUserRights.Rights.READ, ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, project.getUuid());

        return project;
    }

    @Transactional(readOnly = true)
    public Iterable<Project> findByCreatedBy(String uuid) {
        return projectRepository.findByCreatedByAndStatus(
                userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE),
                GiraffeEntity.Status.ACTIVE);
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public Project delete(String userUuid, String uuid) throws GiraffeAccessDeniedException {
        User user = userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE);
        Project project = projectRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, project, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)) == null)
            throw new GiraffeAccessDeniedException(userUuid, project.getUuid());

        long timeDeleted = System.currentTimeMillis();

        complexTaskRepository.findByProjectAndStatus(project, GiraffeEntity.Status.ACTIVE, null)
                .forEach(task -> complexTaskRepository.save(task.setStatus(GiraffeEntity.Status.DELETED).setTimeDeleted(timeDeleted))
                );
        periodRepository.findByProjectAndStatus(project, GiraffeEntity.Status.ACTIVE)
                .forEach(period -> periodRepository.save(period.setStatus(GiraffeEntity.Status.DELETED).setTimeDeleted(timeDeleted))
                );

        return projectRepository.save(project
                .setStatus(GiraffeEntity.Status.DELETED)
                .setTimeDeleted(timeDeleted));
    }

}
