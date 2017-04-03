package giraffe.service.activity.complex;

import giraffe.GiraffeTrackerApplicationTestCase;
import giraffe.domain.activity.complex.ComplexTask;
import giraffe.domain.activity.complex.Period;
import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.repository.complex.ComplexTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class ComplexActivitiesManagementTestCase extends GiraffeTrackerApplicationTestCase {

    @Autowired
    protected ComplexTaskRepository complexTaskRepository;

    protected ComplexTask createBasicTask() {
        return new ComplexTask().setName("myTask")
                .setPriority(ComplexTask.Priority.CRITICAL)
                .setEstimate(2.00)
                .setTimeScheduled(System.currentTimeMillis() + 3000);
    }

    protected Project createBasicProject() {
        return new Project().setName("myProject");
    }

    protected Period createBasicPeriod() {
        return new Period()
                .setName("myPeriod")
                .setTimeScheduledToStart(System.currentTimeMillis())
                .setTimeScheduledToFinish(System.currentTimeMillis() + 3000);
    }

    protected ProjectUserRights createProjectUserRights(Project myProject, ProjectUserRights.Rights rights) {
        return new ProjectUserRights()
                .setProject(myProject)
                .setUser(user)
                .setRights(rights);
    }

    protected ComplexTask createAndSaveTaskWithSubtasks(Project project, Period period){
        ComplexTask task = new ComplexTask()
                .setName("testName1")
                .setCreatedBy(user)
                .setProject(project)
                .setPeriod(period)
                .setAssignedTo(user);
        complexTaskRepository.save(task);

        ComplexTask task2 = new ComplexTask()
                .setName("testName1")
                .setCreatedBy(user)
                .setProject(project)
                .setPeriod(period)
                .setParent(task)
                .setAssignedTo(user);
        complexTaskRepository.save(task2);

        ComplexTask task3 = new ComplexTask()
                .setName("testName1")
                .setCreatedBy(user)
                .setProject(project)
                .setPeriod(period)
                .setParent(task)
                .setAssignedTo(user);
        complexTaskRepository.save(task3);

        ComplexTask task4 = new ComplexTask()
                .setName("testName1")
                .setCreatedBy(user)
                .setProject(project)
                .setPeriod(period)
                .setParent(task2)
                .setAssignedTo(user);
        complexTaskRepository.save(task4);

        return task;
    }
}
