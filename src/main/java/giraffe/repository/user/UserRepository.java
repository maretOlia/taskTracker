package giraffe.repository.user;

import giraffe.domain.user.User;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends GiraffeRepository<User> {

    @Query("MATCH (users:User) RETURN users")
    Iterable<User> findAllUsers();

}
