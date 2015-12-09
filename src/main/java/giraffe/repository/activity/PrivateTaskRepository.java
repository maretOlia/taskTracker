package giraffe.repository.activity;

import giraffe.domain.activity.household.PrivateTask;
import giraffe.repository.GiraffeRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface PrivateTaskRepository extends GiraffeRepository<PrivateTask> {

    @Query("MATCH (tasks:PrivateTask)-[:ASSIGNED_TO]->(:PrivateAccount {uuid:{0}}) RETURN tasks")
    Iterable<PrivateTask> findByAssignedTo(final String privateAccountUuid);

    @Query("MATCH (tasks:PrivateTask)-[:OPENED_BY]->(:PrivateAccount {uuid:{0}}) RETURN tasks")
    Iterable<PrivateTask> findByOpenedBy(final String privateAccountUuid);

    @Query("MATCH (:PrivateAccount {uuid:{0}})<-[:SHARED_WITH]-(tasks:PrivateTask) OPTIONAL MATCH (tasks)<-[:PARENT_TASK*]-(subtasks:PrivateTask) RETURN collect(tasks) + collect(subtasks)")
    Iterable<PrivateTask> findTasksSharedWithAccount(final String privateAccountUuid);

    @Query("MATCH (subtasks:PrivateTask)-[:PARENT_TASK*]->(tasks:PrivateTask {uuid:{0}}) RETURN subtasks")
    Iterable<PrivateTask> findAllSubtasksForTask(final String uuid);
}
