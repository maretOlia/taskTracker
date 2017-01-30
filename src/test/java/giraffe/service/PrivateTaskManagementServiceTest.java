package giraffe.service;

import com.google.common.collect.Iterables;
import giraffe.GiraffeTrackerApplicationTestCase;
import giraffe.domain.GiraffeAuthority;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.AuthorityRepository;
import giraffe.repository.UserRepository;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.service.activity.household.PrivateTaskManagementService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class PrivateTaskManagementServiceTest extends GiraffeTrackerApplicationTestCase {

    @Autowired
    PrivateTaskRepository privateTaskRepository;

    @Autowired
    PrivateTaskManagementService privateTaskManagementService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    User user;


    @Before
    public void createAccount() {
        user = new User()
                .setLogin("testUser")
                .setUserType(User.UserType.REGISTERED)
                .setPasswordHash("testPassword");

        GiraffeAuthority giraffeAuthority = new GiraffeAuthority();
        giraffeAuthority.setRole(GiraffeAuthority.Role.USER);
        authorityRepository.save(giraffeAuthority);

        user.addAuthority(authorityRepository.findByUuidAndStatus(giraffeAuthority.getUuid(), GiraffeEntity.Status.ACTIVE));
        giraffeAuthority.addUser(user);

        userRepository.save(user);
    }

    @Test
    public void shouldCreatePrivateTask() {
        PrivateTask task = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

       privateTaskManagementService.createPrivateTask(task, user.getUuid(), null, null);

       assertThat(privateTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(task)));
    }

    @Test
    public void shouldAddSubtaskToCurrentTask() {
        PrivateTask task1 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

        privateTaskManagementService.createPrivateTask(task1, user.getUuid(), null, null);

        PrivateTask task2 = new PrivateTask()
                .setType(PrivateTask.Type.GIFT)
                .setTerm(3)
                .setName("testName2");

        privateTaskManagementService.createPrivateTask(task2, user.getUuid(), null, task1.getUuid());

        assertThat(Iterables.getFirst(privateTaskRepository.findByParentAndStatus(task1, GiraffeEntity.Status.ACTIVE), null), is(equalTo(task2)));
    }

    @Test
    public void shouldFindAllSubtasksForCurrentTask() {
        PrivateTask task1 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

        privateTaskManagementService.createPrivateTask(task1, user.getUuid(), null, null);

        PrivateTask task2 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName2");

        privateTaskManagementService.createPrivateTask(task2, user.getUuid(), null, task1.getUuid());

        PrivateTask task3 = new PrivateTask()
                .setType(PrivateTask.Type.GIFT)
                .setTerm(3)
                .setName("testName3");

        privateTaskManagementService.createPrivateTask(task3, user.getUuid(), null, task2.getUuid());

        assertThat(Iterables.size(privateTaskManagementService.findAllSubtasksForCurrentTask(task1.getUuid())), is(2));
    }


    @Test
    public void shouldFindAllTasksOpenedByAccount() {
        PrivateTask task1 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

        privateTaskManagementService.createPrivateTask(task1, user.getUuid(), null, task1.getUuid());

        PrivateTask task2 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName2");

        privateTaskManagementService.createPrivateTask(task2, user.getUuid(), null, task2.getUuid());

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksOpenedByAccount(user.getUuid())), is(2));
    }

    @Test
    public void shouldFindAllTasksAssignedToAccount() {
        PrivateTask task1 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

        privateTaskManagementService.createPrivateTask(task1, user.getUuid(), null, task1.getUuid());

        PrivateTask task2 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName2");

        privateTaskManagementService.createPrivateTask(task2, user.getUuid(), null, task2.getUuid());

        privateTaskManagementService.assignTask(task1.getUuid(), user.getUuid());
        privateTaskManagementService.assignTask(task2.getUuid(), user.getUuid());

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksAssignedToUser(user.getUuid())), is(2));
    }

    @Test
    public void shouldDeleteTaskWithSubtasks() {
        PrivateTask task1 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName1");

        privateTaskManagementService.createPrivateTask(task1, user.getUuid(), null, null);

        PrivateTask task2 = new PrivateTask()
                .setType(PrivateTask.Type.EVENT)
                .setTerm(3)
                .setName("testName2");

        privateTaskManagementService.createPrivateTask(task2, user.getUuid(), null, task1.getUuid());

        PrivateTask task3 = new PrivateTask()
                .setType(PrivateTask.Type.GIFT)
                .setTerm(3)
                .setName("testName3");

        privateTaskManagementService.createPrivateTask(task3, user.getUuid(), null, task2.getUuid());

        privateTaskManagementService.deletePrivateTask(task1.getUuid());

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksOpenedByAccount(user.getUuid())), is(0));
    }

}
