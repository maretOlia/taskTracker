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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class PeriodManagementTest extends ComplexActivitiesManagementTestCase {

    @Autowired
    private PeriodManagementService periodManagementService;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRightsRepository projectUserRightsRepository;


    @Test
    public void shouldCreatePeriod() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodManagementService.updateOrCreate(user.getUuid(),
                new Period()
                        .setName("myName")
                        .setTimeScheduledToStart(System.currentTimeMillis())
                        .setTimeScheduledToFinish(System.currentTimeMillis() + 3000)
                , myProject.getUuid());

        assertThat(periodRepository.findByUuidAndStatus(myPeriod.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myPeriod)));
    }

    @Test
    public void shouldUpdatePeriod() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(new Period()
                .setName("myName")
                .setTimeScheduledToStart(System.currentTimeMillis())
                .setTimeScheduledToFinish(System.currentTimeMillis() + 3000)
                .setCreatedBy(user)
                .setProject(myProject));

        myPeriod.setName("newName")
                .setComment("comment");
        periodManagementService.updateOrCreate(user.getUuid(),
                myPeriod
                        .setProject(null)
                        .setCreatedBy(null),
                myProject.getUuid());

        assertThat(periodRepository.findByUuidAndStatus(myPeriod.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myPeriod)));
    }

    @Test
    public void shouldFindByProject() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ));
        periodRepository.save(new Period()
                .setName("myName")
                .setTimeScheduledToStart(System.currentTimeMillis())
                .setTimeScheduledToFinish(System.currentTimeMillis() + 3000)
                .setCreatedBy(user)
                .setProject(myProject));

        assertThat(Iterables.size(periodManagementService.findByProject(user.getUuid(), myProject.getUuid())), is(1));
    }

    @Test
    public void shouldDeletePeriod() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        projectUserRightsRepository.save(createProjectUserRights(myProject, ProjectUserRights.Rights.READ_WRITE));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));

        createAndSaveTaskWithSubtasks(myProject, myPeriod);

        periodManagementService.delete(user.getUuid(), myPeriod.getUuid());

        Iterable<ComplexTask> tasks = complexTaskRepository.findAll();
        Period deletedPeriod = periodRepository.findOne(myPeriod.getUuid());

        assertThat(deletedPeriod.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
        assertThat(deletedPeriod.getTimeDeleted(), is(notNullValue()));
        assertThat(Iterables.size(tasks), is(4));
        tasks.forEach(task -> {
            assertThat(task.getStatus(), is(equalTo(GiraffeEntity.Status.ACTIVE)));
            assertThat(task.getPeriod(), is(nullValue()));
        });
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnUpdate() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));

        User otherUser = createUser("otherUser");

        periodManagementService.updateOrCreate(otherUser.getUuid(), myPeriod.setName("newName"), myProject.getUuid());
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnDelete() throws GiraffeAccessDeniedException {
        Project myProject = projectRepository.save(createBasicProject().setCreatedBy(user));
        Period myPeriod = periodRepository.save(createBasicPeriod()
                .setCreatedBy(user)
                .setProject(myProject));

        User otherUser = createUser("otherUser");

        periodManagementService.delete(otherUser.getUuid(), myPeriod.getUuid());
    }

}
