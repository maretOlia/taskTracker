package giraffe.service.account;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.user.BusinessAccount;
import giraffe.domain.user.PrivateAccount;
import giraffe.repository.user.BusinessAccountRepository;
import giraffe.repository.user.PrivateAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
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
    Neo4jOperations neo4jTemplate;


    public PrivateAccount createPrivateAccount(final String login, final String password) {
        return neo4jTemplate.save(new PrivateAccount(login, password));
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

    public BusinessAccount createBusinessAccount(final String login, final String password) {
        return neo4jTemplate.save(new BusinessAccount(login, password));
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
