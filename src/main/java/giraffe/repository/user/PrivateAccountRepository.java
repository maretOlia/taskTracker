package giraffe.repository.user;

import giraffe.domain.user.PrivateAccount;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.neo4j.annotation.Query;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface PrivateAccountRepository extends GiraffeRepository<PrivateAccount> {

    @Query("MATCH (account:PrivateAccount {login:{0}}) RETURN account")
    PrivateAccount findByLogin(final String login);
}
