package giraffe.config.neo4j;

import giraffe.domain.GiraffeAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * Created by Olga Gushchyna.
 * @version 1.0.0
 */
@Component
public class DataCreator {

    @Autowired
    Neo4jOperations neo4jTemplate;

    /**
     * Populate DB with base data
     */
    @PostConstruct
    public void createData() {
        try {
            neo4jTemplate.save(new GiraffeAuthority((GiraffeAuthority.Role.ADMIN)));
            neo4jTemplate.save(new GiraffeAuthority((GiraffeAuthority.Role.USER)));
            neo4jTemplate.save(new GiraffeAuthority((GiraffeAuthority.Role.ANONIMOUS)));
        } catch (Exception e) {
            // ignore
        }
    }

}
