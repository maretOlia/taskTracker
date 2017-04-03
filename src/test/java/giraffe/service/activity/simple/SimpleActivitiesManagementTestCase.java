package giraffe.service.activity.simple;

import giraffe.GiraffeTrackerApplicationTestCase;
import giraffe.domain.activity.simple.SimpleTask;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.simple.SimpleTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class SimpleActivitiesManagementTestCase extends GiraffeTrackerApplicationTestCase {

    protected static Long testTimeScheduled = System.currentTimeMillis() + 30000;

    @Autowired
    protected SimpleTaskRepository simpleTaskRepository;

    protected SimpleToDoList createSimpleToDoList() {
        return new SimpleToDoList().setName("myList");
    }

    protected SimpleTask createBasicTask() {
        return new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setCreatedBy(user)
                .setName("myTask");
    }

    protected  SimpleTask createAndSaveTaskWithSubtasks(SimpleToDoList simpleToDoList){
        SimpleTask task = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName1")
                .setCreatedBy(user)
                .setSimpleToDoList(simpleToDoList)
                .setAssignedTo(user);
        simpleTaskRepository.save(task);

        SimpleTask task2 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName2")
                .setCreatedBy(user)
                .setSimpleToDoList(simpleToDoList)
                .setAssignedTo(user)
                .setParent(task);
        simpleTaskRepository.save(task2);

        SimpleTask task3 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName3")
                .setCreatedBy(user)
                .setSimpleToDoList(simpleToDoList)
                .setAssignedTo(user)
                .setParent(task);
        simpleTaskRepository.save(task3);

        SimpleTask task4 = new SimpleTask()
                .setTimeScheduled(testTimeScheduled)
                .setName("testName4")
                .setCreatedBy(user)
                .setSimpleToDoList(simpleToDoList)
                .setAssignedTo(user)
                .setParent(task2);

        simpleTaskRepository.save(task4);

        return task;
    }

}
