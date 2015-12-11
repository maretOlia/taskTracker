package giraffe;

import giraffe.domain.user.PrivateAccount;
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
        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword"));

        assertThat(account.getUuid(), notNullValue());
        assertThat(account.getTimeCreated(), notNullValue());
    }

    @Test(expected = DataRetrievalFailureException.class)
    public void shouldCreateUniqueConstraintOnField() {
        neo4jTemplate.save(new PrivateAccount("testLogin", "testPassword"));
        neo4jTemplate.save(new PrivateAccount("testLogin", "testPassword"));
    }
}
