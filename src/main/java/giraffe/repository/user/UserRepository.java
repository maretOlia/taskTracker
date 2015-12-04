package giraffe.repository.user;

import giraffe.domain.user.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends GraphRepository<User> {

    User findByUuid(final String uuid);

    @Query("MATCH (user:User) RETURN user")
    Iterable<User> listAllUsers();

    @Query("MATCH (PrivateAccount)<-[HAS_ACCOUNT]-(user) RETURN user")
    Iterable<User> listUsersWhoHavePrivateAccount();

}
