package giraffe.repository.user;

import giraffe.domain.account.User;
import giraffe.repository.GiraffeRepository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface UserRepository extends GiraffeRepository<User> {

    User findByLogin(String login);

}
