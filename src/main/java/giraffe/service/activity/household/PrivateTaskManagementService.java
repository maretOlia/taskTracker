package giraffe.service.activity.household;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.GiraffeException.CanNotDeleteTaskWithLinkedSubtasksException;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.activity.PrivateTaskRepository;
import giraffe.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class PrivateTaskManagementService {

    @Autowired
    PrivateTaskRepository privateTaskRepository;

    @Autowired
    UserRepository userRepository;


    public PrivateTask findPrivateTaskByUuid(String uuid) {
        return privateTaskRepository.findOne(uuid);
    }

    public PrivateTask createPrivateTask(String name, String openedBy, PrivateTask.Type type, Integer term, String assignedTo, String comment) {

        PrivateTask task = new PrivateTask(name, userRepository.findOne(openedBy), null, type, term);
        if (assignedTo != null) task.setAssignedTo(userRepository.findOne(assignedTo));
        if (comment != null) task.setComment(comment);

        privateTaskRepository.save(task);

        return task;
    }


    public Iterable<PrivateTask> findPrivateTasksAssignedToUser(String userUuid) {
        return privateTaskRepository.findByAssignedTo(userRepository.findOne(userUuid));
    }

    public Iterable<PrivateTask> findPrivateTasksOpenedByAccount(String userUuid) {
        return privateTaskRepository.findByOpenedBy(userRepository.findOne(userUuid));
    }

    public Iterable<PrivateTask> findAllSubtasksForCurrentTask(String parentUuid) {
        return findSubtasksRecursively(privateTaskRepository.findOne(parentUuid));
    }

    private Iterable<PrivateTask> findSubtasksRecursively(PrivateTask parent) {
        Iterable<PrivateTask> tasks = privateTaskRepository.findByParent(parent);

        for (PrivateTask task : tasks) {
            tasks = Iterables.concat(tasks, findSubtasksRecursively(task));
        }

        return tasks;
    }

    @Transactional
    public PrivateTask deletePrivateTask(String uuid, boolean withSubtasks) throws
            CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task = privateTaskRepository.findOne(uuid);

        Iterable<PrivateTask> subtasks = privateTaskRepository.findByParent(privateTaskRepository.findOne(uuid));
        if (!Iterables.isEmpty(subtasks) && !withSubtasks)
            throw new CanNotDeleteTaskWithLinkedSubtasksException(uuid);

        subtasks.forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED);
                    privateTaskRepository.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED);

        return privateTaskRepository.save(task);
    }

    @Transactional
    public PrivateTask addSubtask(String name, String openedBy, PrivateTask.Type type, Integer term, String
            parentUuid, String assignedTo, String comment) {
        PrivateTask parent = privateTaskRepository.findOne(parentUuid);
        PrivateTask subtask = new PrivateTask(name, userRepository.findOne(openedBy), parent, type, term);
        if (assignedTo != null) subtask.setAssignedTo(userRepository.findOne(assignedTo));
        if (comment != null) subtask.setComment(comment);

        // parent.addChildTask(subtask);

        //privateTaskRepository.save(parent);
        privateTaskRepository.save(subtask);

        return subtask;
    }

    public PrivateTask asignTask(String taskUuid, String userUuuid) {
        PrivateTask task = privateTaskRepository.findOne(taskUuid);
        task.setAssignedTo(userRepository.findOne(userUuuid));

        return privateTaskRepository.save(task);
    }

}
