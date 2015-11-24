package giraffe.activities.business;

import giraffe.activities.Activity;
import giraffe.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTask extends Activity {

    protected User openedBy;

    protected Status status;

    protected Priority priority;

    protected int estimate;

    protected List<BusinessTask> subTasks = new ArrayList<>();


    public BusinessTask(final int id,
                        final String name,
                        final String comment,
                        final User assignedTo,
                        final Map<String, String> links,
                        final List<String> img,
                        final User openedBy,
                        final Status status,
                        final Priority priority,
                        final int estimate) {
        super(id, name, comment, assignedTo, links, img);
        this.openedBy = openedBy;
        this.status = status;
        this.priority = priority;
        this.estimate = estimate;
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

    public User getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(final User openedBy) {
        this.openedBy = openedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public List<BusinessTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BusinessTask that = (BusinessTask) o;
        return Objects.equals(openedBy, that.openedBy) &&
                status == that.status && Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), openedBy, status, subTasks);
    }

}
