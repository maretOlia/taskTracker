package giraffe.domain.activity.utils;

import giraffe.domain.activity.household.PrivateTask;
import giraffe.domain.user.PrivateAccount;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class PrivateTaskBuilder extends ActivityBuilder<PrivateTaskBuilder> {

    private Long term;

    private PrivateAccount openedBy;

    private PrivateAccount assignedTo;

    private PrivateTask.TaskStatus taskStatus = PrivateTask.TaskStatus.OPEN;

    private PrivateTask.Type type = PrivateTask.Type.TASK;

    private PrivateTask parentTask;


    @Override
    public PrivateTaskBuilder self() { return this; }

    @Override
    public PrivateTask build() {
        return new PrivateTask(name, comment, references, img, taskStatus, type, term, assignedTo, openedBy, parentTask);
    }

    public PrivateTaskBuilder assignedTo(final PrivateAccount account){
        this.assignedTo = account;
        return self();
    }

    public PrivateTaskBuilder openedBy(final PrivateAccount account){
        this.openedBy = account;
        return self();
    }

    public PrivateTaskBuilder parentTask(final PrivateTask parentTask) {
       this.parentTask = parentTask;
        return this;
    }

    public PrivateTaskBuilder term(final Long term) {
        this.term = term;
        return this;
    }

    public PrivateTaskBuilder status(final PrivateTask.TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public PrivateTaskBuilder type(final PrivateTask.Type type) {
        this.type = type;
        return this;
    }
}