package giraffe.domain.activity.household;

import giraffe.domain.activity.Activity;

import javax.persistence.*;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
@Table(name = "private_task")
public class PrivateTask extends Activity<PrivateTask> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_uuid", referencedColumnName = "uuid")
    private PrivateTask parent;

    @Column(nullable = false)
    @Enumerated
    private Type type;

    private Integer term; // hours

    @Column(name = "task_status", nullable = false)
    @Enumerated
    private TaskStatus taskStatus = TaskStatus.OPEN;


    public enum Type {
        TASK(0), EVENT(1), PURCHASE(2), GIFT(3), EDUCATION(4), JOURNEY(5);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum TaskStatus {
        OPEN(0), DELAYED(2), DONE(3);

        private int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public PrivateTask() {
    }

    @Override
    public PrivateTask self() {
        return this;
    }

    public Type getType() {
        return type;
    }

    public PrivateTask setType(Type type) {
        this.type = type;
        return this;
    }

    public Integer getTerm() {
        return term;
    }

    public PrivateTask setTerm(Integer term) {
        this.term = term;
        return this;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public PrivateTask setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public PrivateTask getParent() {
        return parent;
    }

    public PrivateTask setParent(PrivateTask parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateTask)) return false;
        if (!super.equals(o)) return false;

        PrivateTask that = (PrivateTask) o;

        if (parent != null ? !parent.getUuid().equals(that.parent.getUuid()) : that.parent != null) return false;
        if (type != that.type) return false;
        if (term != that.term) return false;
        return taskStatus == that.taskStatus;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.getUuid().hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + term.hashCode();
        result = 31 * result + taskStatus.hashCode();
        return result;
    }
}
