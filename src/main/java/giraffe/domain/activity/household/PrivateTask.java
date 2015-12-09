package giraffe.domain.activity.household;

import giraffe.domain.activity.Activity;
import giraffe.domain.user.PrivateAccount;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NodeEntity
final public class PrivateTask extends Activity {

    private TaskStatus taskStatus;

    private Type type;

    private Long term;

    @Relationship(type = "OPENED_BY", direction = Relationship.OUTGOING)
    private PrivateAccount openedBy;

    @Relationship(type = "ASSIGNED_TO", direction = Relationship.OUTGOING)
    private PrivateAccount assignedTo;

    @Relationship(type = "SHARED_WITH", direction = Relationship.OUTGOING)
    private Set<PrivateAccount> sharedWith = new HashSet<>();

    private PrivateTask parentTask;


    private PrivateTask() {
    }

    public PrivateTask(final String name,
                       final String comment,
                       final List<String> references,
                       final List<String> imgs,
                       final TaskStatus taskStatus,
                       final Type type,
                       final Long term,
                       final PrivateAccount assignedTo,
                       final PrivateAccount openedBy,
                       final PrivateTask parentTask) {
        super(name, comment, references, imgs);
        this.taskStatus = taskStatus;
        this.type = type;
        this.term = term;
        this.openedBy = openedBy;
        this.assignedTo = assignedTo;
        this.parentTask = parentTask;
    }

    public enum Type {
        TASK(0), EVENT(1), PURCHASE(2), GIFT(3), EDUCATION(4), JOURNEY(5);

        private int value;

        Type(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum TaskStatus {
        OPEN(0), DELAYED(1), DONE(2);

        private int value;

        TaskStatus(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public PrivateTask getParentTask() {
        return parentTask;
    }

    public void parentTask(final PrivateTask parentTask) {
        this.parentTask = parentTask;
    }

    public void shareWith(final PrivateAccount account) {
        sharedWith.add(account);
    }

    public void openedBy(PrivateAccount openedBy) {
        this.openedBy = openedBy;
    }

    public PrivateAccount getOpenedBy() {
        return openedBy;
    }

    public void assignedTo(PrivateAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    public PrivateAccount getAssignedTo() {
        return assignedTo;
    }

    public Set<PrivateAccount> getSharedWith() {
        return sharedWith;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(final TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(final Long term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrivateTask that = (PrivateTask) o;
        return taskStatus == that.taskStatus &&
                type == that.type &&
                Objects.equals(term, that.term) &&
                Objects.equals(openedBy, that.openedBy) &&
                Objects.equals(assignedTo, that.assignedTo) &&
                Objects.equals(sharedWith, that.sharedWith);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), taskStatus, type, term, openedBy, assignedTo, sharedWith);
    }
}
