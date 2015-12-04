package giraffe.repository.user;

import giraffe.domain.user.PrivateAccount;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface PrivateAccountRepository extends GraphRepository<PrivateAccount> {

    PrivateAccount findByUuid(final String uuid);

    @Query("MATCH (account:PrivateAccount)<-[HAS_ACCOUNT]-(:User {uuid:{0}}) RETURN account")
    Iterable<PrivateAccount> listAllUserPrivateAccounts(final String uuid);

}
