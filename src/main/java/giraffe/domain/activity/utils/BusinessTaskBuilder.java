package giraffe.domain.activity.utils;

import giraffe.domain.activity.business.BusinessTask;
import giraffe.domain.activity.business.Component;
import giraffe.domain.activity.business.Project;
import giraffe.domain.user.BusinessAccount;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTaskBuilder<T extends BusinessTaskBuilder> extends ActivityBuilder<T> {

    protected BusinessTask.Priority priority;

    protected Integer estimate;

    protected BusinessTask.TaskStatus taskStatus = BusinessTask.TaskStatus.OPEN;

    protected BusinessAccount openedBy;

    protected BusinessAccount assignedTo;

    protected Project project;

    protected Component component;


    public T assignedTo(final BusinessAccount account){
        this.assignedTo = account;
        return self();
    }

    public T openedBy(final BusinessAccount account){
        this.openedBy = account;
        return self();
    }

    public T project(final Project project){
        this.project = project;
        return self();
    }

    public T component(final Component component){
        this.component = component;
        return self();
    }

    public T priority(final BusinessTask.Priority priority) {
        this.priority = priority;
        return self();
    }

    public T status(final BusinessTask.TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return self();
    }

    public T estimate(final Integer estimate) {
        this.estimate = estimate;
        return self();
    }
}