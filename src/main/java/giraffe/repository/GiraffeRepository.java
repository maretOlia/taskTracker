package giraffe.repository;

import giraffe.domain.GiraffeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface GiraffeRepository<T extends GiraffeEntity> extends CrudRepository<T, String> { }
