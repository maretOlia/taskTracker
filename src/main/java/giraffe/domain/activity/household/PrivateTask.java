package giraffe.domain.activity.household;

import giraffe.domain.activity.Activity;
import giraffe.domain.user.PrivateAccount;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NodeEntity
final public class PrivateTask extends Activity {

    private Status status;

    private Type type;

    private Long term;

    @Relationship(type = "OPENED_BY", direction = Relationship.OUTGOING)
    private PrivateAccount openedBy;

    @Relationship(type = "ASSIGNED_TO", direction = Relationship.OUTGOING)
    private PrivateAccount assignedTo;

    @Relationship(type = "SHARED_WITH", direction = Relationship.OUTGOING)
    private Set<PrivateAccount> sharedWith = new HashSet<>();

    @Relationship(type = "HAS_SUBTASK", direction = Relationship.OUTGOING)
    private Set<PrivateTask> subTasks = new HashSet<>();


    private PrivateTask() {
    }

    public PrivateTask(final String name,
                       final String comment,
                       final List<String> references,
                       final List<String> imgs,
                       final Status status,
                       final Type type,
                       final Long term,
                       final PrivateAccount assignedTo,
                       final PrivateAccount openedBy) {
        super(name, comment, references, imgs);
        this.status = status;
        this.type = type;
        this.term = term;
        this.openedBy = openedBy;
        this.assignedTo = assignedTo;
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

    public enum Status {
        OPEN(0), DELAYED(1), DONE(2);

        private int value;

        Status(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void sharedWith(final PrivateAccount account) {
        sharedWith.add(account);
    }

    public void hasSubtask(final PrivateTask subtask) {
        subTasks.add(subtask);
    }

    public void openedBy(PrivateAccount openedBy) {
        this.openedBy = openedBy;
    }

    public void assignedTo(PrivateAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    public PrivateAccount getOpenedBy() {
        return openedBy;
    }

    public Set<PrivateAccount> getSharedWith() {
        return sharedWith;
    }

    public Set<PrivateTask> getSubTasks() {
        return subTasks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
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
        return status == that.status &&
                type == that.type &&
                Objects.equals(term, that.term) &&
                Objects.equals(openedBy, that.openedBy) &&
                Objects.equals(assignedTo, that.assignedTo) &&
                Objects.equals(sharedWith, that.sharedWith) &&
                Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, type, term, openedBy, assignedTo, sharedWith, subTasks);
    }
}
