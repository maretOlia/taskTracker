package giraffe.service.activity.complex;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.ComplexTask;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.complex.PeriodRepository;
import giraffe.repository.complex.ProjectRepository;
import giraffe.repository.complex.security.ProjectUserRightsRepository;
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
public class ProjectManagementServiceTest extends ComplexActivitiesManagementTestCase {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRightsRepository projectUserRightsRepository;

    @Autowired
    private PeriodRepository periodRepository;


    @Test
    public void shouldCreateProject() throws GiraffeAccessDeniedException {
        Project myProject = projectManagementService.updateOrCreate(user.getUuid(), createBasicProject());

        assertThat(projectRepository.findByUuidAndStatus(myProject.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myProject)));
    }

    @Test
    public void shouldUpdateProject() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));

        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        myProject.setName("newName")
                .setComment("comment");
        projectManagementService.updateOrCreate(user.getUuid(), myProject.setCreatedBy(null));

        assertThat(projectRepository.findByUuidAndStatus(myProject.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myProject)));
    }

    @Test
    public void shouldCreateProjectUserRightsReference() throws GiraffeAccessDeniedException {
        Project myProject = projectManagementService.updateOrCreate(user.getUuid(), createBasicProject());

        assertThat(projectUserRightsRepository.findByUserAndProjectAndRightsIn(user, myProject, Sets.newHashSet(ProjectUserRights.Rights.READ_WRITE)), is(notNullValue()));
    }

    @Test
    public void shouldFindByCreatedBy() {
        projectRepository.save(createBasicProject().setCreatedBy(user));
        projectRepository.save(createBasicProject().setCreatedBy(user));

        assertThat(Iterables.size(projectManagementService.findByCreatedBy(user.getUuid())), is(2));
    }

    @Test
    public void shouldAddUserWithRights() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));

        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        User otherUser = createUser("otherUser");
        projectManagementService.addUserWithRights(user.getUuid(), otherUser.getUuid(), myProject.getUuid(), ProjectUserRights.Rights.READ);

        ProjectUserRights projectUserRights = projectUserRightsRepository.findByUserAndProjectAndRightsIn(otherUser, myProject, Sets.newHashSet(ProjectUserRights.Rights.READ));

        assertThat(projectUserRights.getProject(), is(equalTo(myProject)));
        assertThat(projectUserRights.getUser(), is(equalTo(otherUser)));
        assertThat(projectUserRights.getRights(), is(equalTo(ProjectUserRights.Rights.READ)));
    }

    @Test
    public void shouldDeleteProjectWithAllPeriodsAndTasks() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));
        periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));

        createAndSaveTaskWithSubtasks(myProject, myPeriod);

        projectManagementService.delete(user.getUuid(), myProject.getUuid());

        Iterable<ComplexTask> deletedTasks = complexTaskRepository.findAll();
        Iterable<Period> deletedPeriods = periodRepository.findAll();
        Project deletedProject = projectRepository.findOne(myProject.getUuid());

        assertThat(deletedProject.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
        assertThat(deletedProject.getTimeDeleted(), is(notNullValue()));
        assertThat(Iterables.size(deletedPeriods), is(2));
        deletedPeriods.forEach(period -> {
            assertThat(period.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
            assertThat(period.getTimeDeleted(), is(notNullValue()));
        });
        assertThat(Iterables.size(deletedTasks), is(4));
        deletedTasks.forEach(task -> {
            assertThat(task.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
            assertThat(task.getTimeDeleted(), is(notNullValue()));
        });
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnUpdate() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(new Project().setName("MyProject").setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        User otherUser = createUser("otherUser");

        projectManagementService.updateOrCreate(otherUser.getUuid(), myProject);
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnDelete() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        User otherUser = createUser("otherUser");

        projectManagementService.delete(otherUser.getUuid(), myProject.getUuid());
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnAddUserWithRights() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));

        User otherUser = createUser("otherUser");

        projectManagementService.addUserWithRights(otherUser.getUuid(), otherUser.getUuid(), myProject.getUuid(), ProjectUserRights.Rights.READ);
    }

}
