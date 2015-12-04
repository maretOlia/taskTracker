package giraffe.domain.activity.utils;

import giraffe.domain.activity.business.BusinessTask;
import giraffe.domain.user.BusinessAccount;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTaskBuilder<T extends BusinessTaskBuilder> extends ActivityBuilder<T> {

    protected BusinessTask.Priority priority;

    protected Integer estimate;

    protected BusinessTask.Status status = BusinessTask.Status.OPEN;

    protected BusinessAccount openedBy;

    protected BusinessAccount assignedTo;


    public T assignedTo(final BusinessAccount account){
        this.assignedTo = account;
        return self();
    }

    public T openedBy(final BusinessAccount account){
        this.openedBy = account;
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

    public T estimate(final Integer estimate) {
        this.estimate = estimate;
        return self();
    }
}