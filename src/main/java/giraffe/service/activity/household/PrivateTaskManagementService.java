package giraffe.service.activity.household;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeException.CanNotDeleteTaskWithLinkedSubtasksException;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.household.PrivateTask;
import giraffe.domain.user.PrivateAccount;
import giraffe.repository.activity.PrivateTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class PrivateTaskManagementService {

    @Autowired
    PrivateTaskRepository privateTaskRepository;

    @Autowired
    Neo4jOperations neo4jTemplate;


    public PrivateTask findPrivateTaskByUuid(final String uuid) {
        return privateTaskRepository.findByUuid(uuid);
    }

    public PrivateTask createPrivateTask(final PrivateTask task) {
        return neo4jTemplate.save(task);
    }

    public Iterable<PrivateTask> findPrivateTasksAssignedToAccount(final String accountUuid) {
        return privateTaskRepository.findByAssignedTo(accountUuid);
    }

    public Iterable<PrivateTask> findPrivateTasksOpenedByAccount(final String accountUuid) {
        return privateTaskRepository.findByOpenedBy(accountUuid);
    }

    public Iterable<PrivateTask> findTasksSharedWithUser(final String userUuid) {
        return privateTaskRepository.findTasksSharedWithAccount(userUuid);
    }

    @Transactional
    public PrivateTask deletePrivateTask(final String uuid, boolean withSubtasks) throws CanNotDeleteTaskWithLinkedSubtasksException {
        PrivateTask task = privateTaskRepository.findByUuid(uuid);

        Iterable<PrivateTask> subtasks = privateTaskRepository.findAllSubtasksForTask(uuid);
        if (!Iterables.isEmpty(subtasks) && !withSubtasks) throw new CanNotDeleteTaskWithLinkedSubtasksException(uuid);

        subtasks.forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED);
                    neo4jTemplate.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED);

        return neo4jTemplate.save(task);
    }

    @Transactional
    public PrivateTask addSubtask(final PrivateTask subtask, final String parentTaskUuid) {
        subtask.parentTask(privateTaskRepository.findByUuid(parentTaskUuid));

        return neo4jTemplate.save(subtask);
    }

    @Transactional
    public PrivateTask sharePrivateTask(final List<PrivateAccount> accountsToShareWith, final String taskUuid) {
        final PrivateTask task = privateTaskRepository.findByUuid(taskUuid);

        accountsToShareWith.forEach(task::shareWith);

        return privateTaskRepository.save(task);
    }

}
