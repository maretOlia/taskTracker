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
        return privateTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);
    }

    public PrivateTask createPrivateTask(String name, String openedBy, PrivateTask.Type type, Integer term, String assignedTo, String comment) {

        PrivateTask task = new PrivateTask(name, userRepository.findByUuidAndStatus(openedBy, GiraffeEntity.Status.ACTIVE), null, type, term);
        if (assignedTo != null) task.setAssignedTo(userRepository.findByUuidAndStatus(assignedTo, GiraffeEntity.Status.ACTIVE));
        if (comment != null) task.setComment(comment);

        privateTaskRepository.save(task);

        return task;
    }


    public Iterable<PrivateTask> findPrivateTasksAssignedToUser(String userUuid) {
        return privateTaskRepository.findByAssignedToAndStatus(userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE), GiraffeEntity.Status.ACTIVE);
    }

    public Iterable<PrivateTask> findPrivateTasksOpenedByAccount(String userUuid) {
        return privateTaskRepository.findByOpenedByAndStatus(userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE), GiraffeEntity.Status.ACTIVE);
    }

    public Iterable<PrivateTask> findAllSubtasksForCurrentTask(String parentUuid) {
        return findSubtasksRecursively(privateTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE));
    }

    private Iterable<PrivateTask> findSubtasksRecursively(PrivateTask parent) {
        Iterable<PrivateTask> tasks = privateTaskRepository.findByParentAndStatus(parent, GiraffeEntity.Status.ACTIVE);

        for (PrivateTask task : tasks) {
            tasks = Iterables.concat(tasks, findSubtasksRecursively(task));
        }

        return tasks;
    }

    @Transactional
    public PrivateTask deletePrivateTask(String uuid, boolean withSubtasks) throws CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task = privateTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        Iterable<PrivateTask> subtasks = privateTaskRepository.findByParentAndStatus(privateTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE), GiraffeEntity.Status.ACTIVE);
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
        PrivateTask parent = privateTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE);
        PrivateTask subtask = new PrivateTask(name, userRepository.findByUuidAndStatus(openedBy, GiraffeEntity.Status.ACTIVE), parent, type, term);
        if (assignedTo != null) subtask.setAssignedTo(userRepository.findByUuidAndStatus(assignedTo, GiraffeEntity.Status.ACTIVE));
        if (comment != null) subtask.setComment(comment);

        privateTaskRepository.save(subtask);

        return subtask;
    }

    public PrivateTask assignTask(String taskUuid, String userUuuid) {
        PrivateTask task = privateTaskRepository.findByUuidAndStatus(taskUuid, GiraffeEntity.Status.ACTIVE);
        task.setAssignedTo(userRepository.findByUuidAndStatus(userUuuid, GiraffeEntity.Status.ACTIVE));

        return privateTaskRepository.save(task);
    }

}
