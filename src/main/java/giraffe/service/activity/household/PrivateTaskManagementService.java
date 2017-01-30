package giraffe.service.activity.household;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.UserRepository;
import giraffe.repository.activity.PrivateTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class PrivateTaskManagementService {

    private PrivateTaskRepository privateTaskRepository;

    private UserRepository userRepository;

    @Autowired
    public PrivateTaskManagementService(PrivateTaskRepository privateTaskRepository, UserRepository userRepository) {
        this.privateTaskRepository = privateTaskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PrivateTask createPrivateTask(PrivateTask task, String openedByUuid, String assignedToUuid, String parentUuid) {
        task.setOpenedBy(userRepository.findByUuidAndStatus(openedByUuid, GiraffeEntity.Status.ACTIVE));
        if (parentUuid != null) task.setParent(privateTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE));
        if (assignedToUuid != null) task.setAssignedTo(userRepository.findByUuidAndStatus(assignedToUuid, GiraffeEntity.Status.ACTIVE));

        privateTaskRepository.save(task);

        return task;
    }

    @Transactional
    public PrivateTask assignTask(String taskUuid, String userUuuid) {
        PrivateTask task = privateTaskRepository.findByUuidAndStatus(taskUuid, GiraffeEntity.Status.ACTIVE);
        task.setAssignedTo(userRepository.findByUuidAndStatus(userUuuid, GiraffeEntity.Status.ACTIVE));

        return privateTaskRepository.save(task);
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
    public PrivateTask deletePrivateTask(String uuid) {
        PrivateTask task = privateTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        Iterable<PrivateTask> subtasks = findAllSubtasksForCurrentTask(uuid);

        subtasks.forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED);
                    privateTaskRepository.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED);

        return privateTaskRepository.save(task);
    }

}
