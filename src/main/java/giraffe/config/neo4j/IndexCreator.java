package giraffe.config.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Component
public class IndexCreator {

    @Autowired
    Neo4jOperations neo4jTemplate;


    @PostConstruct
    public void createIndexes() {
        try {
            neo4jTemplate.query("CREATE INDEX ON :GiraffeEntity(uuid)", Collections.emptyMap());
            neo4jTemplate.query("CREATE CONSTRAINT ON (user:User) ASSERT user.login IS UNIQUE", Collections.emptyMap());
        } catch (Exception e) {
           // ignore
        }
    }

}
