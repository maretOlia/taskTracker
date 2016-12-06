package giraffe.repository.security;

import giraffe.domain.account.GiraffeAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface AuthorityRepository extends CrudRepository<GiraffeAuthority, String> {

    GiraffeAuthority findByRole(GiraffeAuthority.Role role);

}