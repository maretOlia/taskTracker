package giraffe.service.user;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.user.Account;
import giraffe.domain.user.BusinessAccount;
import giraffe.domain.user.PrivateAccount;
import giraffe.domain.user.User;
import giraffe.repository.user.AccountRepository;
import giraffe.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class UserManagementService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    Neo4jOperations neo4jTemplate;


    @Transactional
    public User createUser(final String login, final String password) {
        User savedUser = neo4jTemplate.save(new User(login, password));

        neo4jTemplate.save(new PrivateAccount(savedUser));
        neo4jTemplate.save(new BusinessAccount(savedUser));

        return savedUser;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public User deleteUser(final String uuid) {
        User user = userRepository.findByUuid(uuid);

        user.setStatus(GiraffeEntity.Status.DELETED);
        userRepository.save(user);

        ((Iterable<Account>) accountRepository.findUserAccounts(uuid)).forEach(account -> {
            account.setStatus(GiraffeEntity.Status.DELETED);
            accountRepository.save(account);
        });
        return user;
    }
}
