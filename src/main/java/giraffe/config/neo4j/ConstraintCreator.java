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
public class ConstraintCreator {

    @Autowired
    Neo4jOperations neo4jTemplate;


    /**
     * Create unique constraints in Neo4j DB
     */
    @PostConstruct
    public void createConstraints() {
        try {
            neo4jTemplate.query("CREATE CONSTRAINT ON (entity:GiraffeEntity) ASSERT entity.uuid IS UNIQUE", Collections.emptyMap());
            neo4jTemplate.query("CREATE CONSTRAINT ON (account:Account) ASSERT account.login IS UNIQUE", Collections.emptyMap());
            neo4jTemplate.query("CREATE CONSTRAINT ON (user:User) ASSERT user.login IS UNIQUE", Collections.emptyMap());
            neo4jTemplate.query("CREATE CONSTRAINT ON (authority:GiraffeAuthority) ASSERT authority.role IS UNIQUE", Collections.emptyMap());
        } catch (Exception e) {
            // ignore
        }
    }

}
