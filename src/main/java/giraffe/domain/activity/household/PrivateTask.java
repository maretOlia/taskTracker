package giraffe.domain.activity.household;

import com.google.common.collect.Sets;
import giraffe.domain.activity.Activity;
import giraffe.domain.account.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
@Table(name = "private_task")
public class PrivateTask extends Activity {

    @ManyToOne
    @JoinColumn(name = "parent_uuid")
    private PrivateTask parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<PrivateTask> childTasks = Sets.newHashSet();

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

    PrivateTask() {
    }

    public PrivateTask(String name, User openedBy, PrivateTask parent, Type type, Integer term) {
        super(name, openedBy);
        Assert.notNull(type, "Type must not be null");
        this.type = type;
        this.term = term;
        this.parent = parent;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Set<PrivateTask> getChildTasks() {
        return childTasks;
    }

    public void addChildTask(PrivateTask childTask) {
        if (!childTasks.contains(childTask)) {
            childTasks.add(childTask);
        }
    }

    public PrivateTask getParent() {
        return parent;
    }

    public void setParent(PrivateTask parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateTask)) return false;
        if (!super.equals(o)) return false;

        PrivateTask that = (PrivateTask) o;

        if (parent != null ? !parent.getUuid().equals(that.parent.getUuid()) : that.parent != null) return false;
        if (type != that.type) return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;
        return taskStatus == that.taskStatus;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.getUuid().hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + taskStatus.hashCode();
        return result;
    }
}
