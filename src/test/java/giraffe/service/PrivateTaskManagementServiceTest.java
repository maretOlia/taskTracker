package giraffe.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import giraffe.GiraffeApplicationTestCase;
import giraffe.domain.GiraffeException;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.domain.activity.utils.PrivateTaskBuilder;
import giraffe.domain.user.PrivateAccount;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.domain.GiraffeAuthority;
import giraffe.service.activity.household.PrivateTaskManagementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class PrivateTaskManagementServiceTest extends GiraffeApplicationTestCase {

    @Autowired
    Neo4jOperations neo4jTemplate;

    @Autowired
    PrivateTaskManagementService privateTaskManagementService;

    @Autowired
    PrivateTaskRepository privateTaskRepository;


    @Test
    public void shouldFindAllPrivateTaskWithSubtasksSharedWithUser() {
        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));

        PrivateTask task1 = new PrivateTaskBuilder().name("test task 1").build();
        task1.shareWith(account);
        neo4jTemplate.save(task1);

        PrivateTask task2 = new PrivateTaskBuilder().name("test task 2").build();
        task2.parentTask(task1);
        neo4jTemplate.save(task2);

        PrivateTask task3 = new PrivateTaskBuilder().name("test task 3").build();
        task3.shareWith(account);
        neo4jTemplate.save(task3);

        PrivateTask task4 = new PrivateTaskBuilder().name("test task 4").build();
        task4.parentTask(task3);
        neo4jTemplate.save(task4);

        PrivateTask task5 = new PrivateTaskBuilder().name("test task 5").build();
        task5.parentTask(task4);
        neo4jTemplate.save(task5);

        assertThat(Iterables.size(privateTaskManagementService.findTasksSharedWithUser(account.getUuid())), is(5));
    }

    @Test
    public void shouldCreatePrivateTask() {
        PrivateTask task = privateTaskManagementService.createPrivateTask(new PrivateTaskBuilder().name("testName").build());

        assertThat(privateTaskRepository.findByUuid(task.getUuid()) , is(equalTo(task)));
    }

    @Test
    public void shouldSharePrivateTaskWithPrivateAccount() {
        PrivateTask task = neo4jTemplate.save(new PrivateTaskBuilder().name("testName").build());

        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));
        privateTaskManagementService.sharePrivateTask(Lists.newArrayList(account), task.getUuid());

        assertThat(Iterables.getFirst(privateTaskRepository.findTasksSharedWithAccount(account.getUuid()), null), is(equalTo(task)));
    }

    @Test
    public void shouldDeletePrivateTaskWithAllSubtasks() throws GiraffeException.CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task1 = neo4jTemplate.save(new PrivateTaskBuilder().name("test task 1").build());
        neo4jTemplate.save(task1);

        PrivateTask task2 = new PrivateTaskBuilder().name("test task 2").build();
        task2.parentTask(task1);
        neo4jTemplate.save(task2);

        privateTaskManagementService.deletePrivateTask(task1.getUuid(), true);

        assertThat(privateTaskRepository.findByUuid(task1.getUuid()).getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
        assertThat(privateTaskRepository.findByUuid(task2.getUuid()).getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
    }

    @Test
    public void shouldAddSubtaskToCurrentTask() {
        PrivateTask task1 = neo4jTemplate.save(new PrivateTaskBuilder().name("test task 1").build());
        neo4jTemplate.save(task1);

        PrivateTask task2 = new PrivateTaskBuilder().name("test task 2").build();
        privateTaskManagementService.addSubtask(task2, task1.getUuid());

        assertThat(Iterables.getFirst(privateTaskRepository.findAllSubtasksForTask(task1.getUuid()), null), is(equalTo(task2)));
    }

    @Test
    public void shouldFindTaskByUuid() {
        PrivateTask task = neo4jTemplate.save(new PrivateTaskBuilder().name("test task 1").build());
        neo4jTemplate.save(task);

        assertThat(privateTaskRepository.findByUuid(task.getUuid()), is(equalTo(task)));
    }

    public void shouldFindAllTasksOpenedByAccount() {
        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));

        PrivateTask task1 = neo4jTemplate.save(new PrivateTaskBuilder().name("test task 1").openedBy(account).build());
        neo4jTemplate.save(task1);

        PrivateTask task2 = new PrivateTaskBuilder().name("test task 2").openedBy(account).build();
        neo4jTemplate.save(task2);

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksOpenedByAccount(account.getUuid())), is(2));
    }

    @Test
    public void shouldFindAllTasksAssignedToAccount() {
        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));

        PrivateTask task1 = neo4jTemplate.save(new PrivateTaskBuilder().name("test task 1").assignedTo(account).build());
        neo4jTemplate.save(task1);

        PrivateTask task2 = new PrivateTaskBuilder().name("test task 2").assignedTo(account).build();
        neo4jTemplate.save(task2);

        assertThat(Iterables.size(privateTaskManagementService.findPrivateTasksAssignedToAccount(account.getUuid())), is(2));
    }
}
