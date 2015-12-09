package giraffe;

import giraffe.domain.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.neo4j.template.Neo4jOperations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */

public class ConfigurationTest extends GiraffeApplicationTestCase {

    @Autowired
    Neo4jOperations neo4jTemplate;


    @Test
    public void shouldPopulateUuidAndTimeFieldForEntity() {
        User savedUser = neo4jTemplate.save(new User("testUser", "testPassword"));

        assertThat(savedUser.getUuid(), notNullValue());
        assertThat(savedUser.getTimeCreated(), notNullValue());
    }

    @Test(expected = DataRetrievalFailureException.class)
    public void shouldCreateUniqueConstraintOnField() {
        neo4jTemplate.save(new User("testLogin", "testPassword"));
        neo4jTemplate.save(new User("testLogin", "testPassword"));
    }
}
