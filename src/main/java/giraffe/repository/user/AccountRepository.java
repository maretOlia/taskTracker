package giraffe.repository.user;

import giraffe.domain.user.Account;
import giraffe.repository.GiraffeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface AccountRepository<T extends Account> extends GiraffeRepository<T> { }