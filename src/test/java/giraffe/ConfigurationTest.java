package giraffe;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeAuthority;
import giraffe.domain.user.PrivateAccount;
import org.junit.Test;
import org.neo4j.ogm.exception.CypherException;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Session session;


    @Test
    public void shouldPopulateUuidAndTimeFieldForEntity() {
        PrivateAccount account = neo4jTemplate.save(new PrivateAccount("testUser", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));

        assertThat(account.getUuid(), notNullValue());
        assertThat(account.getTimeCreated(), notNullValue());
    }

    //@Test(expected = CypherException.class)
    public void shouldCreateUniqueConstraintOnField() {
       try {
           neo4jTemplate.save(new PrivateAccount("testLogin", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));
           neo4jTemplate.save(new PrivateAccount("testLogin", "testPassword", Sets.newHashSet(new GiraffeAuthority(GiraffeAuthority.Role.USER))));
       }catch (CypherException e){
           System.out.println("********************************");
           System.out.println(e.getDescription());
       }

    }
}
