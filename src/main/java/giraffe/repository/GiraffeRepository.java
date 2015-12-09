package giraffe.repository;

import giraffe.domain.GiraffeEntity;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface GiraffeRepository<T extends GiraffeEntity> extends GraphRepository<T> {

    T findByUuid(final String uuid);
}
