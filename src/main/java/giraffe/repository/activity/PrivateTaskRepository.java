package giraffe.repository.activity;

import giraffe.domain.activity.household.PrivateTask;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface PrivateTaskRepository extends GraphRepository<PrivateTask> {

    PrivateTask findByUuid(final String uuid);

    @Query("MATCH (task:PrivateTask)-[ASSIGNED_TO]->(:PrivateAccount {uuid:{0}}) RETURN task")
    Iterable<PrivateTask> listPrivateTaskAssignedToAccount(final String privateAccountUuid);

    @Query("MATCH (task:PrivateTask)-[OPENED_BY]->(:PrivateAccount {uuid:{0}}) RETURN task")
    Iterable<PrivateTask> listPrivateTaskOpenedByAccount(final String privateAccountUuid);
}
