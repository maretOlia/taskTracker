package giraffe.service.activity.simple;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.simple.SimpleTask;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.simple.SimpleTaskRepository;
import giraffe.repository.simple.SimpleToDoListRepository;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
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
public class SimpleToDoListManagementTest extends SimpleActivitiesManagementTestCase {

    @Autowired
    private SimpleToDoListManagementService simpleToDoListManagementService;

    @Autowired
    private SimpleToDoListRepository simpleToDoListRepository;

    @Autowired
    private SimpleTaskRepository simpleTaskRepository;

    @Test
    public void shouldCreateSimpleToDoList() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListManagementService.updateOrCreate(user.getUuid(), createSimpleToDoList());

        assertThat(simpleToDoListRepository.findByUuidAndStatus(myList.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myList)));
    }

    @Test
    public void shouldUpdateSimpleToDoList() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        myList.setComment("comment")
                .setName("newName");
        simpleToDoListManagementService.updateOrCreate(user.getUuid(), myList);

        assertThat(simpleToDoListRepository.findByUuidAndStatus(myList.getUuid(), GiraffeEntity.Status.ACTIVE), is(equalTo(myList)));
    }


    @Test
    public void shouldFindSimpleToDoListByCreatedBy() throws GiraffeAccessDeniedException {
        simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));
        simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        assertThat(Iterables.size(simpleToDoListManagementService.findByCreatedBy(user.getUuid())), is(2));
    }

    @Test
    public void shouldFindByUuid() throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        assertThat(simpleToDoListManagementService.findByUuid(user.getUuid(), myList.getUuid()), is(equalTo(myList)));
    }

    @Test
    public void shouldDeleteSimpleToDoList() throws GiraffeAccessDeniedException {
        SimpleToDoList myList = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        createAndSaveTaskWithSubtasks(myList);

        simpleToDoListManagementService.delete(user.getUuid(), myList.getUuid());

        Iterable<SimpleTask> deletedTasks = simpleTaskRepository.findAll();
        SimpleToDoList deletedToDoList = simpleToDoListRepository.findOne(myList.getUuid());


        assertThat(deletedToDoList.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
        assertThat(deletedToDoList.getTimeDeleted(), is(notNullValue()));
        assertThat(Iterables.size(deletedTasks), is(4));
        deletedTasks.forEach(deletedTask -> {
            assertThat(deletedTask.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));
            assertThat(deletedTask.getTimeDeleted(), is(notNullValue()));
        });
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnUpdate() throws GiraffeAccessDeniedException {
        SimpleToDoList list = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        User otherUser = createUser("otherUser");
        simpleToDoListManagementService.updateOrCreate(otherUser.getUuid(), list.setName("otherName"));
    }

    @Test(expected = GiraffeAccessDeniedException.class)
    public void shouldThrowGiraffeAccessDeniedExceptionOnDelete() throws GiraffeAccessDeniedException {
        SimpleToDoList list = simpleToDoListRepository.save(createSimpleToDoList().setCreatedBy(user));

        User otherUser = createUser("otherUser");
        simpleToDoListManagementService.delete(otherUser.getUuid(), list.getUuid());
    }

    @Test(expected = NoActivityWithCurrentUuidException.class)
    public void shouldThrowNoActivityWithCurrentUuidException() throws NoActivityWithCurrentUuidException, GiraffeAccessDeniedException {
        simpleToDoListManagementService.findByUuid(user.getUuid(), "fake-uuid");
    }

}
