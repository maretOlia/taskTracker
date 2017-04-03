package giraffe.domain.activity;

import giraffe.domain.User;

import javax.persistence.*;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@MappedSuperclass
public abstract class Task<T extends Task> extends Activity<T> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_user_uuid", referencedColumnName = "uuid")
    protected User assignedTo;

    protected Long timeScheduled;

    protected Task() {
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public T setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
        return self();
    }

    public Long getTimeScheduled() {
        return timeScheduled;
    }

    public T setTimeScheduled(Long timeScheduled) {
        this.timeScheduled = timeScheduled;
        return self();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        if (!super.equals(o)) return false;

        Task<?> task = (Task<?>) o;
        if (timeScheduled != null && !timeScheduled.equals(task.timeScheduled)) return false;
        
        return (assignedTo != null ? assignedTo.getUuid().equals(task.assignedTo.getUuid()) : task.assignedTo == null) && timeScheduled.equals(task.timeScheduled);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (assignedTo != null ? assignedTo.getUuid().hashCode() : 0);
        result = 31 * result + (timeScheduled != null ? timeScheduled.hashCode() : 0);

        return result;
    }

}
