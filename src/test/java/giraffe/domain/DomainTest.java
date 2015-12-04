package giraffe.domain;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import giraffe.context.PersistenceContext;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.domain.activity.utils.PrivateTaskBuilder;
import giraffe.domain.user.PrivateAccount;
import giraffe.domain.user.User;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.repository.user.PrivateAccountRepository;
import giraffe.repository.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@ContextConfiguration(classes = {PersistenceContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DomainTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Neo4jOperations neo4jTemplate;

    @Autowired
    PrivateAccountRepository privateAccountRepository;

    @Autowired
    PrivateTaskRepository privateTaskRepository;


    @Test
    public void actionListenerShouldPopulateUuidAndTimeFieldForEntity() {
        User savedUser = neo4jTemplate.save(new User("testUser", "testPassword"));

        assertThat(savedUser.getUuid(), notNullValue());
        assertThat(savedUser.getTimeCreated(), notNullValue());
    }

    @Test
    public void shouldCreateUserAndPrivateAccount() {
        User savedUser = neo4jTemplate.save(new User("testUser", "testPassword"));
        User fetchedUser = userRepository.findByUuid(savedUser.getUuid());

        assertThat(savedUser, equalTo(fetchedUser));

        PrivateAccount account = neo4jTemplate.save(new PrivateAccount());
        fetchedUser.hasAccount(account);
        neo4jTemplate.save(fetchedUser);

        assertThat(Iterables.isEmpty(privateAccountRepository.listAllUserPrivateAccounts(fetchedUser.getUuid())), is(false));
    }

    @Test
    public void shouldCreatePrivateTaskWithRelationsToUserAccount() {
        User savedUser = neo4jTemplate.save(new User("testUser", "testPassword"));

        PrivateAccount account = neo4jTemplate.save(new PrivateAccount());
        savedUser.hasAccount(account);
        neo4jTemplate.save(savedUser);

        PrivateTask savedTask = neo4jTemplate.save(new PrivateTaskBuilder().name("testTask").build());

        savedTask.assignedTo(account);
        savedTask.openedBy(account);

        PrivateTask changedTask = neo4jTemplate.save(savedTask);

        assertThat(savedTask, equalTo(changedTask));
        assertThat(Iterables.size(privateTaskRepository.listPrivateTaskAssignedToAccount(account.getUuid())), equalTo(Iterables.size(privateTaskRepository.listPrivateTaskOpenedByAccount(account.getUuid()))));
    }

    @Test
    public void shouldFlatStringCollectionAndMapBack(){
        PrivateTask task = new PrivateTaskBuilder()
                .name("testTask")
                .build();
        task.getImgs().addAll(Lists.newArrayList("mockUrl1", "mockUrl2"));
        PrivateTask savedTask = neo4jTemplate.save(task);

        assertThat(task, equalTo(savedTask));
        assertThat(task, equalTo(privateTaskRepository.findByUuid(task.getUuid())));
    }
}
