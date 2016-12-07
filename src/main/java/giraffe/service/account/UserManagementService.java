package giraffe.service.account;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.GiraffeException;
import giraffe.domain.account.GiraffeAuthority;
import giraffe.domain.account.User;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.repository.security.AuthorityRepository;
import giraffe.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class UserManagementService {

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private PrivateTaskRepository privateTaskRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(UserRepository userRepository, AuthorityRepository authorityRepository, PrivateTaskRepository privateTaskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.privateTaskRepository = privateTaskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createAccount(String login, String password) throws GiraffeException.AccountWithCurrentLoginExistsException {
        if (userRepository.findByLoginAndStatus(login, GiraffeEntity.Status.ACTIVE) != null)
            throw new GiraffeException.AccountWithCurrentLoginExistsException(login);

        String passwordHash = passwordEncoder.encode(password);
        User user = new User()
                .setLogin(login)
                .setPasswordHash(passwordHash)
                .addAuthority(authorityRepository.findByRole(GiraffeAuthority.Role.USER));

        return userRepository.save(user);
    }

    public User findPrivateAccount(String uuid) {
        return userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
    }

    @Transactional
    public User deletePrivateAccount(String uuid) {
        User user = userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
        user.setStatus(GiraffeEntity.Status.DELETED);

        Iterable<PrivateTask> userTasks = privateTaskRepository.findByOpenedByAndStatus(user, GiraffeEntity.Status.ACTIVE);
        userTasks.forEach(task -> {
            task.setStatus(GiraffeEntity.Status.DELETED);
            privateTaskRepository.save(task);

        });

        return userRepository.save(user);
    }

}
