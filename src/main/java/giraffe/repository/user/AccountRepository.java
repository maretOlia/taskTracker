package giraffe.repository.user;

import giraffe.domain.user.Account;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface AccountRepository<T extends Account> extends GiraffeRepository<T> {

    @Query("MATCH (accounts:Account)-[:USER]->(:User {uuid:{0}}) RETURN accounts")
    Iterable<Account> findUserAccounts(final String userUuid);
}
