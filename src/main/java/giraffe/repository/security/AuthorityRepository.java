package giraffe.repository.security;

import giraffe.domain.GiraffeAuthority;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface AuthorityRepository extends GraphRepository<GiraffeAuthority> {

    GiraffeAuthority findByRole(final GiraffeAuthority.Role role);

}