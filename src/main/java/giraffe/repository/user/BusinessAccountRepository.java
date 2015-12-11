package giraffe.repository.user;

import giraffe.domain.user.BusinessAccount;
import org.springframework.data.neo4j.annotation.Query;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface BusinessAccountRepository extends AccountRepository<BusinessAccount> {

    @Query("MATCH (account:BusinessAccount)-[:USER]->(:User {uuid:{0}}) RETURN account")
    BusinessAccount findByUserUuid(final String userUuid);
}
