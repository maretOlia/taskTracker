package giraffe.activities.utils;

import giraffe.activities.business.BusinessTask;
import giraffe.user.User;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTaskBuilder<T extends BusinessTaskBuilder> extends ActivityBuilder<T> {

    protected User openedBy;

    protected BusinessTask.Priority priority;

    protected int estimate;

    protected BusinessTask.Status status = BusinessTask.Status.OPEN;


    public T openedBy(final User openedBy) {
        this.openedBy = openedBy;
        return self();
    }

    public T priority(final BusinessTask.Priority priority) {
        this.priority = priority;
        return self();
    }

    public T status(final BusinessTask.Status status) {
        this.status = status;
        return self();
    }

    public T estimate(final int estimate) {
        this.estimate = estimate;
        return self();
    }
}