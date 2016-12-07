package giraffe.domain.activity.business;

import com.google.common.collect.Sets;
import giraffe.domain.activity.Activity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
@Table(name = "business_task")
public class BusinessTask extends Activity<BusinessTask> {

    @ManyToOne
    @JoinColumn(name = "parent_uuid")
    protected BusinessTask parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
    protected Set<BusinessTask> childTasks = Sets.newHashSet();

    @Enumerated
    @Column(name = "task_status", nullable = false)
    private TaskStatus taskStatus = TaskStatus.OPEN;

    @Enumerated
    @Column(nullable = false)
    private Priority priority;

    private Integer estimate; //hours

    @ManyToOne
    @JoinColumn(name = "component_uuid")
    private Component component;

    @ManyToOne
    @JoinColumn(name = "stream_uuid")
    private Stream stream;


    public enum TaskStatus {
        OPEN(0), IN_PROGRESS(1), DELAYED(2), DONE(3), NEEDS_REVIEW(4), CLOSED(5);

        private int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Priority {
        TRIVIAL(0), MINOR(1), MAJOR(2), CRITICAL(3), BLOCKER(4);

        private int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    BusinessTask(){ }


    @Override
    public BusinessTask self() {
        return this;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public BusinessTask setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public BusinessTask setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public BusinessTask setEstimate(Integer estimate) {
        this.estimate = estimate;
        return this;
    }

    public Component getComponent() {
        return component;
    }

    public BusinessTask setComponent(Component component) {
        this.component = component;
        return this;
    }

    public Stream getStream() {
        return stream;
    }

    public BusinessTask setStream(Stream stream) {
        this.stream = stream;
        return this;
    }

    public Set<BusinessTask> getChildTasks() {
        return childTasks;
    }

    public BusinessTask addChildTask(BusinessTask childTask) {
        if (!childTasks.contains(childTask)) {
            childTasks.add(childTask);
        }
        return this;
    }

    public BusinessTask getParent() {
        return parent;
    }

    public BusinessTask setParent(BusinessTask parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessTask)) return false;
        if (!super.equals(o)) return false;

        BusinessTask that = (BusinessTask) o;

        if (parent != null ? !parent.getUuid().equals(that.parent.getUuid()) : that.parent != null) return false;
        if (taskStatus != that.taskStatus) return false;
        if (priority != that.priority) return false;
        if (estimate != null ? !estimate.equals(that.estimate) : that.estimate != null) return false;
        if (!component.getUuid().equals(that.component.getUuid())) return false;
        return stream != null ? stream.getUuid().equals(that.stream.getUuid()) : that.stream == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.getUuid().hashCode() : 0);
        result = 31 * result + taskStatus.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + (estimate != null ? estimate.hashCode() : 0);
        result = 31 * result + component.getUuid().hashCode();
        result = 31 * result + (stream != null ? stream.getUuid().hashCode() : 0);
        return result;
    }
}
