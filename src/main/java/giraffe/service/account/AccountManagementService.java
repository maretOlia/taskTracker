package giraffe.service.account;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeException;
import giraffe.domain.GiraffeAuthority;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.user.BusinessAccount;
import giraffe.domain.user.PrivateAccount;
import giraffe.repository.security.AuthorityRepository;
import giraffe.repository.user.BusinessAccountRepository;
import giraffe.repository.user.PrivateAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class AccountManagementService {

    @Autowired
    PrivateAccountRepository privateAccountRepository;

    @Autowired
    BusinessAccountRepository businessAccountRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    Neo4jOperations neo4jTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public PrivateAccount createPrivateAccount(final String login, final String password) throws GiraffeException.AccountWithCurrentLoginExistsException {
       if (privateAccountRepository.findByLogin(login) != null) throw new GiraffeException.AccountWithCurrentLoginExistsException(login);

        //GiraffeAuthority auth = neo4jTemplate.save(new GiraffeAuthority((GiraffeAuthority.Role.USER)));//TODO kill it

        final GiraffeAuthority authority = authorityRepository.findByRole(GiraffeAuthority.Role.USER);

        final String passwordHash = passwordEncoder.encode(password);
        final PrivateAccount privateAccount = new PrivateAccount(login, passwordHash, Sets.newHashSet(authority));

        return neo4jTemplate.save(privateAccount);
    }

    public PrivateAccount findPrivateAccount(final String uuid) {
        return privateAccountRepository.findByUuid(uuid);
    }

    @Transactional
    public PrivateAccount deletePrivateAccount(final String uuid) {
        PrivateAccount account = privateAccountRepository.findByUuid(uuid);
        account.setStatus(GiraffeEntity.Status.DELETED);

        return privateAccountRepository.save(account);
    }

    @Transactional
    public BusinessAccount createBusinessAccount(final String login, final String password) {
        final GiraffeAuthority authority = authorityRepository.findByRole(GiraffeAuthority.Role.USER);
        final BusinessAccount businessAccount = new BusinessAccount(login, password, Sets.newHashSet(authority));
        return neo4jTemplate.save(businessAccount);
    }

    @Transactional
    public BusinessAccount deleteBusinessAccount(final String uuid) {
        BusinessAccount account = businessAccountRepository.findByUuid(uuid);
        account.setStatus(GiraffeEntity.Status.DELETED);

        return businessAccountRepository.save(account);
    }

    public BusinessAccount findBusinessAccountForUser(final String uuid) {
        return businessAccountRepository.findByUuid(uuid);
    }

}
