package giraffe.service.activity.simple;

import com.google.common.collect.Iterables;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.simple.SimpleTask;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.UserRepository;
import giraffe.repository.simple.SimpleTaskRepository;
import giraffe.repository.simple.SimpleToDoListRepository;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.TaskManagementService;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class SimpleTaskManagementService extends TaskManagementService<SimpleTask> {

    private SimpleTaskRepository simpleTaskRepository;

    private UserRepository userRepository;

    private SimpleToDoListRepository simpleToDoListRepository;

    @Autowired
    public SimpleTaskManagementService(SimpleTaskRepository simpleTaskRepository,
                                       UserRepository userRepository,
                                       SimpleToDoListRepository simpleToDoListRepository) {
        this.simpleTaskRepository = simpleTaskRepository;
        this.userRepository = userRepository;
        this.simpleToDoListRepository = simpleToDoListRepository;
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public SimpleTask updateOrCreate(String userUuid, SimpleTask task, String assignedToUuid, String parentUuid, String simpleToDoListUuid) throws GiraffeAccessDeniedException {
        SimpleTask oldTask = simpleTaskRepository.findByUuidAndStatus(task.getUuid(), GiraffeEntity.Status.ACTIVE);

        if (oldTask == null) return create(task, userUuid, assignedToUuid, parentUuid, simpleToDoListUuid);

        if (!oldTask.getCreatedBy().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, simpleToDoListUuid);

        task.setSimpleToDoList(
                !oldTask.getSimpleToDoList().getUuid().equals(simpleToDoListUuid) ?
                        simpleToDoListRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE) :
                        oldTask.getSimpleToDoList());

        // if assignedToUuid == null then old value in DB will be erased
        if (assignedToUuid != null) {
            task.setAssignedTo(
                    oldTask.getAssignedTo() == null || !oldTask.getAssignedTo().getUuid().equals(assignedToUuid) ?
                            userRepository.findByUuidAndStatus(assignedToUuid, GiraffeEntity.Status.ACTIVE) :
                            oldTask.getAssignedTo());
        }

        // if parentUuid == null then old value in DB will be erased
        if (parentUuid != null) {
            task.setParent(
                    oldTask.getParent() == null || !oldTask.getParent().getUuid().equals(parentUuid) ?
                            simpleTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE) :
                            oldTask.getParent());
        }

        return simpleTaskRepository.save(task);
    }

    private SimpleTask create(SimpleTask task, String createdByUuid, String assignedToUuid, String parentUuid, String simpleToDoListUuid) {
        task.setCreatedBy(userRepository.findByUuidAndStatus(createdByUuid, GiraffeEntity.Status.ACTIVE));

        if (assignedToUuid != null)
            task.setAssignedTo(userRepository.findByUuidAndStatus(assignedToUuid, GiraffeEntity.Status.ACTIVE));
        if (simpleToDoListUuid != null)
            task.setSimpleToDoList(simpleToDoListRepository.findByUuidAndStatus(simpleToDoListUuid, GiraffeEntity.Status.ACTIVE));
        if (parentUuid != null)
            task.setParent(simpleTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE));

        return simpleTaskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {GiraffeAccessDeniedException.class, NoActivityWithCurrentUuidException.class})
    public SimpleTask findByUuid(String userUuid, String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        SimpleTask task = simpleTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if(task == null) throw new NoActivityWithCurrentUuidException(uuid);

        if (!task.getCreatedBy().getUuid().equals(userUuid)
                || !task.getAssignedTo().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, task.getSimpleToDoList().getUuid());

        return task;
    }

    @Transactional(readOnly = true)
    public Page<SimpleTask> findByAssignedTo(String userUuid, Pageable page) {
        return simpleTaskRepository.findByAssignedToAndStatus(
                userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE),
                GiraffeEntity.Status.ACTIVE,
                page);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Iterable<SimpleTask> findByParent(String userUuid, String parentUuid) throws GiraffeAccessDeniedException {
        SimpleTask task = simpleTaskRepository.findByUuidAndStatus(parentUuid, GiraffeEntity.Status.ACTIVE);

        if (!task.getCreatedBy().getUuid().equals(userUuid)
                || !task.getAssignedTo().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, task.getSimpleToDoList().getUuid());

        return findSubtasksRecursively(task);
    }

    private Iterable<SimpleTask> findSubtasksRecursively(SimpleTask parent) {
        Iterable<SimpleTask> tasks = simpleTaskRepository.findByParentAndStatus(parent, GiraffeEntity.Status.ACTIVE);

        for (SimpleTask task : tasks) {
            tasks = Iterables.concat(tasks, findSubtasksRecursively(task));
        }

        return tasks;
    }

    @Transactional(readOnly = true, rollbackFor = GiraffeAccessDeniedException.class)
    public Page<SimpleTask> findBySimpleToDoList(String userUuid, String simpleToDoListUuid, Pageable page) throws GiraffeAccessDeniedException {
        SimpleToDoList simpleToDoList = simpleToDoListRepository.findByUuidAndStatus(simpleToDoListUuid, GiraffeEntity.Status.ACTIVE);

        if (!simpleToDoList.getCreatedBy().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, simpleToDoListUuid);

        return simpleTaskRepository.findBySimpleToDoListAndStatus(
                simpleToDoList,
                GiraffeEntity.Status.ACTIVE,
                page);
    }

    @Override
    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public SimpleTask delete(String userUuid, String uuid) throws GiraffeAccessDeniedException {
        SimpleTask task = simpleTaskRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (!task.getCreatedBy().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, task.getSimpleToDoList().getUuid());

        findByParent(userUuid, uuid).forEach(subtask -> {
                    subtask.setStatus(GiraffeEntity.Status.DELETED)
                            .setTimeDeleted(System.currentTimeMillis());
                    simpleTaskRepository.save(subtask);
                }
        );
        task.setStatus(GiraffeEntity.Status.DELETED)
                .setTimeDeleted(System.currentTimeMillis());

        return simpleTaskRepository.save(task);
    }

}
