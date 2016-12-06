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

    public PrivateTask createPrivateTask(PrivateTask task) {
        return privateTaskRepository.save(task);
    }

    public Iterable<PrivateTask> findPrivateTasksAssignedToAccount(String userUuid) {
        return privateTaskRepository.findByAssignedTo(userRepository.findOne(userUuid));
    }

    public Iterable<PrivateTask> findPrivateTasksOpenedByAccount(String userUuid) {
        return privateTaskRepository.findByOpenedBy(userRepository.findOne(userUuid));
    }

    @Transactional
    public PrivateTask deletePrivateTask(String uuid, boolean withSubtasks) throws CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task = privateTaskRepository.findOne(uuid);

        Iterable<PrivateTask> subtasks = privateTaskRepository.findByParent(privateTaskRepository.findOne(uuid));
        if (!Iterables.isEmpty(subtasks) && !withSubtasks) throw new CanNotDeleteTaskWithLinkedSubtasksException(uuid);

        subtasks.forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED);
                    privateTaskRepository.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED);

        return privateTaskRepository.save(task);
    }

    public PrivateTask addSubtask(PrivateTask subtask, String parentUuid) {
        PrivateTask parent = privateTaskRepository.findOne(parentUuid);
        subtask.setParent(parent);
        parent.addChildTask(subtask);

        privateTaskRepository.save(parent);

        return subtask;
    }

    public PrivateTask asignTask(String taskUuid, String userUuuid) {
        PrivateTask task = privateTaskRepository.findOne(taskUuid);
        task.setAssignedTo(userRepository.findOne(userUuuid));

        return privateTaskRepository.save(task);
    }

}
