package giraffe;

import giraffe.service.PrivateTaskManagementServiceTest;
import giraffe.service.UserManagementServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConfigurationTest.class,
        UserManagementServiceTest.class,
        PrivateTaskManagementServiceTest.class
})
public class GiraffeTestSuite {
}
