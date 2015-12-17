package giraffe.repository.user;

import giraffe.domain.user.PrivateAccount;
import org.springframework.data.neo4j.annotation.Query;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface PrivateAccountRepository extends AccountRepository<PrivateAccount> {

    @Query("MATCH (account:PrivateAccount)-[:USER]->(:User {uuid:{0}}) RETURN account")
    PrivateAccount findByUserUuid(final String userUuid);

    PrivateAccount findByLogin(final String login);

}
