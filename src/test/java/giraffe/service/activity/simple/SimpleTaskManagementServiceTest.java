package giraffe.service.activity.simple;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.simple.SimpleTask;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.simple.SimpleToDoListRepository;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class SimpleTaskManagementServiceTest extends SimpleActivitiesManagementTestCase {

    @Autowired
    private SimpleTaskManagementService simpleTaskManagementService;

    @Autowired
    private SimpleToDoListRepository simpleToDoListRepository;

    private static Long testTimeScheduled = System.currentTimeMillis() + 30000;

    @Test
    public void shouldCreateSimpleTask() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));
        SimpleTask task = createBasicTask();

        SimpleTask simpleTask = simpleTaskManagementService.updateOrCreate(user.getUuid(), task, user.getUuid(), null, myList.getUuid());

        assertThat(simpleTaskRepository.findByUuidAndStatus(simpleTask.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(simpleTask)));
    }

    @Test
    public void shouldUpdateSimpleTask() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask savedTask = simpleTaskRepository.save(new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setSimpleToDoList(myList)
                .setCreatedBy(user)
                .setName("testName1"));

        User otherUser = createUser("otherUser");

        simpleTaskManagementService.updateOrCreate(user.getUuid(),
                savedTask.setName("new Name")
                        .setTimeScheduled(savedTask.getTimeScheduled() + 30000)
                        .setSimpleToDoList(null)
                        .setComment("comment")
                        .setTaskStatus(SimpleTask.TaskStatus.DONE),
                otherUser.getUuid(),
                null,
                myList.getUuid());
        savedTask.setAssignedTo(otherUser);

        assertThat(simpleTaskRepository.findByUuidAndStatus(savedTask.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(savedTask)));
    }

    @Test
    public void shouldAddSubtask() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task1 = createBasicTask();

        simpleTaskManagementService.updateOrCreate(user.getUuid(), task1, user.getUuid(), null, myList.getUuid());

        SimpleTask task2 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setCreatedBy(user)
                .setName("testName2");

        simpleTaskManagementService.updateOrCreate(user.getUuid(), task2, user.getUuid(), task1.getUuid(), myList.getUuid());

        assertThat(Iterables.getFirst(simpleTaskRepository.findByParentAndStatus(task1, GiraffeEntity.Status.ACTIVE), null), is(equalTo(task2)));
    }

    @Test
    public void shouldFindAllTasksByParent() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask taskWithSubtask = createAndSaveTaskWithSubtasks(myList);

        assertThat(Iterables.size(simpleTaskManagementService.findByParent(user.getUuid(), taskWithSubtask.getUuid())), is(3));
    }

    @Test
    public void shouldFindByUuid() throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task = createBasicTask()
                .setSimpleToDoList(myList)
                .setAssignedTo(user);

        simpleTaskRepository.save(task);

        assertThat(simpleTaskManagementService.findByUuid(user.getUuid(), task.getUuid()), is(equalTo(task)));
    }

    @Test
    public void shouldFindAllTasksAssignedToUser() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task1 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName1")
                .setCreatedBy(user)
                .setSimpleToDoList(myList)
                .setAssignedTo(user);

        simpleTaskRepository.save(task1);

        SimpleTask task2 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName2")
                .setCreatedBy(user)
                .setSimpleToDoList(myList)
                .setAssignedTo(user);

        simpleTaskRepository.save(task2);

        assertThat(simpleTaskManagementService.findByAssignedTo(user.getUuid(), new PageRequest(0, 20)).getTotalElements(), is(2L));
    }

    @Test
    public void shouldDeleteTaskWithSubtasks() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask taskWithSubtasks = createAndSaveTaskWithSubtasks(myList);

        simpleTaskManagementService.delete(user.getUuid(), taskWithSubtasks.getUuid());

        Iterable<SimpleTask> deletedTasks = simpleTaskRepository.findAll();

        assertThat(Iterables.size(deletedTasks), is(4));
        deletedTasks.forEach(task -> {
            assertThat(task.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
            assertThat(task.getTimeDeleted(), is(notNullValue()));
        });
    }

    @Test
    public void shouldFindAllTasksBySimpleToDoList() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        createAndSaveTaskWithSubtasks(myList);

        assertThat(simpleTaskManagementService.findBySimpleToDoList(user.getUuid(), myList.getUuid(), new PageRequest(0, 20)).getTotalElements(), is(4L));
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnUpdate() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task = createBasicTask()
                .setSimpleToDoList(myList)
                .setAssignedTo(user);

        simpleTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        simpleTaskManagementService.updateOrCreate(otherUser.getUuid(), task, otherUser.getUuid(), null, myList.getUuid());
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnDelete() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task = createBasicTask()
                .setSimpleToDoList(myList)
                .setAssignedTo(user);

        simpleTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        simpleTaskManagementService.delete(otherUser.getUuid(), task.getUuid());
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnFindByToDoList() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task = createBasicTask()
                .setSimpleToDoList(myList)
                .setAssignedTo(user);
        simpleTaskRepository.save(task);

        User otherUser = createUser("otherUser");

        simpleTaskManagementService.findBySimpleToDoList(otherUser.getUuid(), myList.getUuid(), null);
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnFindByParent() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        SimpleTask task = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName1")
                .setCreatedBy(user)
                .setSimpleToDoList(myList)
                .setAssignedTo(user);
        simpleTaskRepository.save(task);

        SimpleTask task2 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName2")
                .setCreatedBy(user)
                .setSimpleToDoList(myList)
                .setAssignedTo(user)
                .setParent(task);
        simpleTaskRepository.save(task2);

        User otherUser = createUser("otherUser");

        simpleTaskManagementService.findByParent(otherUser.getUuid(), task.getUuid());
    }

    @Test(expected = NoActivityWithCurrentUuidException.class)
    public void shouldThrowNoActivityWithCurrentUuidException() throws NoActivityWithCurrentUuidException, GiraffeAccessDeniedException {
        simpleTaskManagementService.findByUuid(user.getUuid(), "fake-uuid");
    }

}
