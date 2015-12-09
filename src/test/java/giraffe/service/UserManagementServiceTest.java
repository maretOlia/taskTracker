package giraffe.service;

import com.google.common.collect.Iterables;
import giraffe.GiraffeApplicationTestCase;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.user.Account;
import giraffe.domain.user.User;
import giraffe.repository.user.AccountRepository;
import giraffe.repository.user.UserRepository;
import giraffe.service.user.UserManagementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class UserManagementServiceTest extends GiraffeApplicationTestCase {

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;


    @Test
    public void shouldCreateUserAndAccountsForHim() {
        User user = userManagementService.createUser("testLogin", "testPassword");

        assertThat(user, is(equalTo(userRepository.findByUuid(user.getUuid()))));
        assertThat(Iterables.size(accountRepository.findUserAccounts(user.getUuid())), is(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDeleteUser() {
        User user = userManagementService.createUser("testLogin", "testPassword");
        userManagementService.deleteUser(user.getUuid());

        assertThat(userRepository.findByUuid(user.getUuid()).getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)));

        Iterable<Account> userAccounts = accountRepository.findUserAccounts(user.getUuid());

        ((Iterable<Account>) accountRepository.findUserAccounts(user.getUuid())).forEach(account ->
                assertThat(account.getStatus(), is(equalTo(GiraffeEntity.Status.DELETED)))
        );
    }
}
