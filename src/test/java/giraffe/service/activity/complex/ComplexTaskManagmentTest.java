package giraffe.service.activity.complex;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.ComplexTask;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.complex.PeriodRepository;
import giraffe.repository.complex.ProjectRepository;
import giraffe.repository.complex.security.ProjectUserRightsRepository;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class ComplexTaskManagmentTest extends ComplexActivitiesManagementTestCase {

    @Autowired
    private ComplexTaskManagementService complexTaskManagementService;

    @Autowired
    private ProjectUserRightsRepository projectUserRightsRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PeriodRepository periodRepository;

    @Test
    public void shouldCreateComplexTask() throws TimeScheduledAndPeriodInconsistencyException, GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask task = complexTaskManagementService.updateOrCreate(user.getUuid(),
                createBasicTask(),
                user.getUuid(),
                null,
                myProject.getUuid(),
                null,
                0.0);

        assertThat(complexTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(task)));
    }

    @Test
    public void shouldUpdateComplexTask() throws TimeScheduledAndPeriodInconsistencyException, GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        ComplexTask parent = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject)
                .setAssignedTo(user);
        complexTaskRepository.save(parent);

        task = complexTaskManagementService.updateOrCreate(user.getUuid(),
                task.setCreatedBy(null)
                        .setProject(null)
                        .setName("testName")
                        .setPriority(ComplexTask.Priority.CRITICAL)
                        .setEstimate(2.00)
                        .updateTaskStatus(ComplexTask.TaskStatus.DELAYED)
                        .setTimeScheduled(System.currentTimeMillis() + 3000),
                user.getUuid(),
                parent.getUuid(),
                myProject.getUuid(),
                null,
                2.0);

        assertThat(complexTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(task)));
    }

    @Test
    public void shouldFindByUuid() throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ));
        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        assertThat(complexTaskManagementService.findByUuid(user.getUuid(), task.getUuid()), is(equalTo(task)));
    }

    @Test
    public void shouldFindByProject() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ));

        createAndSaveTaskWithSubtasks(myProject, null);

        assertThat(Iterables.size(complexTaskManagementService.findByProject(user.getUuid(), myProject.getUuid(), null)), is(4));
    }

    @Test
    public void shouldFindByPeriod() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));
        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setPeriod(myPeriod)
                .setProject(myProject);
        complexTaskRepository.save(task);

        assertThat(Iterables.size(complexTaskManagementService.findByPeriod(user.getUuid(), myPeriod.getUuid(), null)), is(1));
    }

    @Test
    public void shouldFindByParent() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ));

        ComplexTask parent = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject)
                .setAssignedTo(user);
        complexTaskRepository.save(parent);

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject)
                .setParent(parent);
        complexTaskRepository.save(task);

        assertThat(Iterables.size(complexTaskManagementService.findByParent(user.getUuid(), parent.getUuid())), is(1));
    }

    @Test
    public void shouldDeleteComplexTask() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask taskWithSubtasks = createAndSaveTaskWithSubtasks(myProject, null);

        complexTaskManagementService.delete(user.getUuid(), taskWithSubtasks.getUuid());

        Iterable<ComplexTask> deletedTasks = complexTaskRepository.findAll();

        deletedTasks.forEach(deletedTask -> {
                    assertThat(deletedTask.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
                    assertThat(deletedTask.getTimeDeleted(), is(notNullValue()));
                }
        );
    }

    @Test(expected = TimeScheduledAndPeriodInconsistencyException.class)
    public void shouldThrowTimeScheduledAndPeriodInconsistencyExceptionOnCreate() throws GiraffeAccessDeniedException, TimeScheduledAndPeriodInconsistencyException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));
        complexTaskManagementService.updateOrCreate(user.getUuid(),
                new ComplexTask().setName("testName")
                        .setTimeScheduled(System.currentTimeMillis() + 10000),

                user.getUuid(),
                null,
                myProject.getUuid(),
                myPeriod.getUuid(),
                2.0);
    }

    @Test(expected = TimeScheduledAndPeriodInconsistencyException.class)
    public void shouldThrowTimeScheduledAndPeriodInconsistencyExceptionOnUpdate() throws GiraffeAccessDeniedException, TimeScheduledAndPeriodInconsistencyException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));
        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        complexTaskManagementService.updateOrCreate(user.getUuid(),
                task.setName("testName")
                        .setProject(myProject)
                        .setCreatedBy(user)
                        .setTimeScheduled(System.currentTimeMillis() + 100000),

                user.getUuid(),
                null,
                myProject.getUuid(),
                myPeriod.getUuid(),
                2.0);
    }


    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnUpdate() throws GiraffeAccessDeniedException, TimeScheduledAndPeriodInconsistencyException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        complexTaskManagementService.updateOrCreate(otherUser.getUuid(), task
                        .setName("newName")
                        .setCreatedBy(null)
                        .setPeriod(null)
                        .setProject(null),
                user.getUuid(),
                null,
                myProject.getUuid(),
                null,
                2.0);
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnDelete() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        complexTaskManagementService.delete(otherUser.getUuid(), task.getUuid());
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnFindByProject() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject);
        complexTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        complexTaskManagementService.findByProject(otherUser.getUuid(), myProject.getUuid(), null);

    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnFindByPeriod() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));
        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setPeriod(myPeriod)
                .setProject(myProject);
        complexTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        complexTaskManagementService.findByPeriod(otherUser.getUuid(), myPeriod.getUuid(), null);
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnFindByParent() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        ComplexTask parent = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject)
                .setAssignedTo(user);
        complexTaskRepository.save(parent);

        ComplexTask task = createBasicTask()
                .setCreatedBy(user)
                .setProject(myProject)
                .setParent(parent);
        complexTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        complexTaskManagementService.findByParent(otherUser.getUuid(), parent.getUuid());
    }

    @Test(expected = NoActivityWithCurrentUuidException.class)
    public void shouldThrowNoActivityWithCurrentUuidException() throws NoActivityWithCurrentUuidException, GiraffeAccessDeniedException {
        complexTaskManagementService.findByUuid(user.getUuid(), "fake-uuid");
    }

}
