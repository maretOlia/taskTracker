package giraffe.service.activity.simple;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.repository.UserRepository;
import giraffe.repository.simple.SimpleTaskRepository;
import giraffe.repository.simple.SimpleToDoListRepository;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Service
public class SimpleToDoListManagementService {

    private SimpleToDoListRepository simpleToDoListRepository;

    private UserRepository userRepository;

    private SimpleTaskRepository simpleTaskRepository;

    @Autowired
    public SimpleToDoListManagementService(SimpleToDoListRepository simpleToDoListRepository,
                                           UserRepository userRepository,
                                           SimpleTaskRepository simpleTaskRepository) {
        this.simpleToDoListRepository = simpleToDoListRepository;
        this.userRepository = userRepository;
        this.simpleTaskRepository = simpleTaskRepository;
    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public SimpleToDoList updateOrCreate(String userUuid, SimpleToDoList simpleToDoList) throws GiraffeAccessDeniedException {
        SimpleToDoList oldToDoList = simpleToDoListRepository.findByUuidAndStatus(simpleToDoList.getUuid(), GiraffeEntity.Status.ACTIVE);

        if (oldToDoList != null) {
            if (!oldToDoList.getCreatedBy().getUuid().equals(userUuid))
                throw new GiraffeAccessDeniedException(userUuid, simpleToDoList.getUuid());

            if (oldToDoList.getCreatedBy().getUuid().equals(userUuid))
                return simpleToDoListRepository.save(simpleToDoList);
        }

        // create new SimpleToDoList
        return simpleToDoListRepository.save(simpleToDoList.setCreatedBy(userRepository.findByUuidAndStatus(userUuid, GiraffeEntity.Status.ACTIVE)));
    }

    @Transactional(readOnly = true)
    public Iterable<SimpleToDoList> findByCreatedBy(String uuid) {
        return simpleToDoListRepository.findByCreatedByAndStatus(
                userRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE),
                GiraffeEntity.Status.ACTIVE);

    }

    @Transactional(rollbackFor = GiraffeAccessDeniedException.class)
    public SimpleToDoList delete(String userUuid, String uuid) throws GiraffeAccessDeniedException {
        SimpleToDoList simpleToDoList = simpleToDoListRepository.findByUuidAndStatus(uuid, GiraffeEntity.Status.ACTIVE);

        if (!simpleToDoList.getCreatedBy().getUuid().equals(userUuid))
            throw new GiraffeAccessDeniedException(userUuid, simpleToDoList.getUuid());

        long timeDeleted = System.currentTimeMillis();
        simpleTaskRepository.findBySimpleToDoListAndStatus(simpleToDoList, GiraffeEntity.Status.ACTIVE, null)
                .getContent().forEach(task ->
                simpleTaskRepository.save(task.setStatus(GiraffeEntity.Status.DELETED).setTimeDeleted(timeDeleted)));

        return simpleToDoListRepository.save(simpleToDoList
                .setStatus(GiraffeEntity.Status.DELETED)
                .setTimeDeleted(timeDeleted));
    }

}
