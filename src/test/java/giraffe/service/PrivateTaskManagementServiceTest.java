package giraffe.service;

import com.google.common.collect.Iterables;
import giraffe.GiraffeApplicationTestCase;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.GiraffeException;
import giraffe.domain.account.GiraffeAuthority;
import giraffe.domain.account.User;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.repository.security.AuthorityRepository;
import giraffe.repository.user.UserRepository;
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
public class PrivateTaskManagementServiceTest extends GiraffeApplicationTestCase {

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
        user = new User("testUser", "testPassword");
        GiraffeAuthority giraffeAuthority = new GiraffeAuthority(GiraffeAuthority.Role.USER);
        authorityRepository.save(giraffeAuthority);
        user.addAuthority(authorityRepository.findByUuidAndStatus(giraffeAuthority.getUuid(), GiraffeEntity.Status.ACTIVE));
        giraffeAuthority.addUser(user);
        userRepository.save(user);
    }

    @Test
    public void shouldCreatePrivateTask() {
        PrivateTask task = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);

        assertThat(privateTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(task)));
    }

    @Test
    public void shouldAddSubtaskToCurrentTask() {
        PrivateTask task1 = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);

        PrivateTask task2 = privateTaskManagementService.addSubtask("testName2", user.getUuid(), PrivateTask.Type.EVENT, 3, task1.getUuid(), null, null);

        assertThat(Iterables.getFirst(privateTaskRepository.findByParentAndStatus(task1, GiraffeEntity.Status.ACTIVE), null), is(equalTo(task2)));
    }

    @Test
    public void shouldFindAllSubtasksForCurrentTask() {
        PrivateTask task1 = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);

        privateTaskManagementService.addSubtask("testName2", user.getUuid(), PrivateTask.Type.EVENT, 3, task1.getUuid(), null, null);
        PrivateTask task3 = privateTaskManagementService.addSubtask("testName3", user.getUuid(), PrivateTask.Type.EVENT, 5, task1.getUuid(), null, null);
        privateTaskManagementService.addSubtask("testName4", user.getUuid(), PrivateTask.Type.EVENT, 5, task3.getUuid(), null, null);

        assertThat(Iterables.size(privateTaskManagementService.findAllSubtasksForCurrentTask(task1.getUuid())), is(3));
    }


    @Test
    public void shouldFindAllTasksOpenedByAccount() {
        PrivateTask task1 = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);

        new PrivateTask("testName2", user, privateTaskManagementService.findPrivateTaskByUuid(task1.getUuid()), PrivateTask.Type.EVENT, 3);
        privateTaskManagementService.addSubtask("testName2", user.getUuid(), PrivateTask.Type.EVENT, 3, task1.getUuid(), null, null);

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksOpenedByAccount(user.getUuid())), is(2));
    }

    @Test
    public void shouldFindAllTasksAssignedToAccount() {
        PrivateTask task1 = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);
        privateTaskManagementService.assignTask(task1.getUuid(), user.getUuid());

        PrivateTask task2 = privateTaskManagementService.createPrivateTask("testName2", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);
        privateTaskManagementService.assignTask(task2.getUuid(), user.getUuid());

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksAssignedToUser(user.getUuid())), is(2));
    }

    @Test
    public void shouldDeleteTaskWithSubtasks() throws GiraffeException.CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task1 = privateTaskManagementService.createPrivateTask("testName1", user.getUuid(), PrivateTask.Type.EVENT, 3, null, null);

        privateTaskManagementService.addSubtask("testName2", user.getUuid(), PrivateTask.Type.EVENT, 3, task1.getUuid(), null, null);

        privateTaskManagementService.deletePrivateTask(task1.getUuid(), true);

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksOpenedByAccount(user.getUuid())), is(0));
    }

}
