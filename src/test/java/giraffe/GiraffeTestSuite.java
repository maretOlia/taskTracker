package giraffe;

import giraffe.service.activity.complex.ComplexTaskManagmentTest;
import giraffe.service.activity.complex.PeriodManagementTest;
import giraffe.service.activity.complex.ProjectManagementServiceTest;
import giraffe.service.activity.simple.SimpleTaskManagementServiceTest;
import giraffe.service.activity.simple.SimpleToDoListManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SimpleTaskManagementServiceTest.class,
        SimpleToDoListManagementTest.class,
        ProjectManagementServiceTest.class,
        PeriodManagementTest.class,
        ComplexTaskManagmentTest.class
})
public class GiraffeTestSuite {
}
