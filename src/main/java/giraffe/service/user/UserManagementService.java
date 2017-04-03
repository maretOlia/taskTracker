package giraffe.service.user;

import giraffe.AccountWithCurrentLoginExistsException;
import giraffe.domain.GiraffeAuthority;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.repository.AuthorityRepository;
import giraffe.repository.UserRepository;
import giraffe.repository.simple.SimpleTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class UserManagementService {

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private SimpleTaskRepository simpleTaskRepository;

    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserManagementService(UserRepository userRepository,
                                 AuthorityRepository authorityRepository,
                                 SimpleTaskRepository simpleTaskRepository,
                                 PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.simpleTaskRepository = simpleTaskRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(rollbackFor = AccountWithCurrentLoginExistsException.class)
    public User create(String login, String password) throws AccountWithCurrentLoginExistsException {
        if (userRepository.findByLoginAndStatus(login, GiraffeEntity.Status.ACTIVE) != null)
            throw new AccountWithCurrentLoginExistsException(login);

        String passwordHash = bCryptPasswordEncoder.encode(password);
        User user = new User()
                .setLogin(login)
                .setPasswordHash(passwordHash)
                .addAuthority(authorityRepository.findByRole(GiraffeAuthority.Role.USER));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User find(String uuid) {
        return userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
    }

}
