package giraffe.domain.activity.business;

import giraffe.domain.activity.Activity;
import giraffe.domain.user.BusinessAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTask extends Activity {

    protected Status status;

    protected Priority priority;

    protected Integer estimate;

    protected BusinessAccount assignedTo;

    protected BusinessAccount openedBy;

    protected List<BusinessTask> subTasks = new ArrayList<>();


    private BusinessTask() { }

    public BusinessTask(final String name,
                        final String comment,
                        final List<String> references,
                        final List<String> imgs,
                        final Status status,
                        final Priority priority,
                        final Integer estimate,
                        final BusinessAccount assignedTo,
                        final BusinessAccount openedBy) {
        super(name, comment, references, imgs);
        this.openedBy = openedBy;
        this.status = status;
        this.priority = priority;
        this.estimate = estimate;
        this.openedBy = openedBy;
        this.assignedTo = assignedTo;
    }

    public enum Status {
        OPEN(0), IN_PROGRESS(1), DELAYED(2), DONE(3), NEEDS_REVIEW(4), CLOSED(5);

        private int value;

        Status(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Priority {
        TRIVIAL(0), MINOR(1), MAJOR(2), CRITICAL(3), BLOCKER(4);

        private int value;

        Priority(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public BusinessAccount getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(final BusinessAccount openedBy) {
        this.openedBy = openedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(final Integer estimate) {
        this.estimate = estimate;
    }

    public BusinessAccount getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(final BusinessAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<BusinessTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BusinessTask that = (BusinessTask) o;
        return status == that.status &&
                priority == that.priority &&
                Objects.equals(estimate, that.estimate) &&
                Objects.equals(assignedTo, that.assignedTo) &&
                Objects.equals(openedBy, that.openedBy) &&
                Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, priority, estimate, assignedTo, openedBy, subTasks);
    }
}
