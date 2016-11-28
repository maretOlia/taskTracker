package giraffe;

import giraffe.context.PersistenceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@ContextConfiguration(classes = {PersistenceContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class GiraffeApplicationTestCase {

    @Autowired
    private Session session;

    @Before
    public void clear() {
        session.purgeDatabase();
    }

}
