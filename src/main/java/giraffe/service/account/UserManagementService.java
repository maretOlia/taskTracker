package giraffe.service.account;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.GiraffeException;
import giraffe.domain.account.GiraffeAuthority;
import giraffe.domain.account.User;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public User createAccount(String login, String password) throws GiraffeException.AccountWithCurrentLoginExistsException {
       if (userRepository.findByLoginAndStatus(login,  GiraffeEntity.Status.ACTIVE) != null) throw new GiraffeException.AccountWithCurrentLoginExistsException(login);

        final GiraffeAuthority authority = authorityRepository.findByRole(GiraffeAuthority.Role.USER);

        final String passwordHash = passwordEncoder.encode(password);
        final User user = new User(login, passwordHash);
        user.addAuthority(authority);

        return userRepository.save(user);
    }

    public User findPrivateAccount(String uuid) {
        return userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
    }

    @Transactional
    public User deletePrivateAccount(String uuid) {
        User account = userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
        account.setStatus(GiraffeEntity.Status.DELETED);

        return userRepository.save(account);
    }

}
