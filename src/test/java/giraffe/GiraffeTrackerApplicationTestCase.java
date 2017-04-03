package giraffe;

import giraffe.domain.GiraffeAuthority;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.repository.AuthorityRepository;
import giraffe.repository.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GiraffeTrackerApplicationTestCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    protected User user;

    @Before
    public void createUser() {
        user = createUser("testUser");
    }

    protected User createUser(String login) {
        User user = new User()
                .setLogin(login)
                .setUserType(User.UserType.REGISTERED)
                .setPasswordHash("testPassword");

        GiraffeAuthority giraffeAuthority = new GiraffeAuthority();
        giraffeAuthority.setRole(GiraffeAuthority.Role.USER);
        authorityRepository.save(giraffeAuthority);

        user.addAuthority(authorityRepository.findByUuidAndStatus(giraffeAuthority.getUuid(), GiraffeEntity.Status.ACTIVE));
        giraffeAuthority.addUser(user);

        return userRepository.save(user);
    }

}

