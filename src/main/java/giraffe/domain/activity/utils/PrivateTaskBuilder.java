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

    private PrivateTask.Status status = PrivateTask.Status.OPEN;

    private PrivateTask.Type type = PrivateTask.Type.TASK;


    @Override
    public PrivateTaskBuilder self() { return this; }

    @Override
    public PrivateTask build() {
        return new PrivateTask(name, comment, references, img, status, type, term, assignedTo, openedBy);
    }

    public PrivateTaskBuilder assignedTo(final PrivateAccount account){
        this.assignedTo = account;
        return self();
    }

    public PrivateTaskBuilder openedBy(final PrivateAccount account){
        this.openedBy = account;
        return self();
    }

    public PrivateTaskBuilder term(final Long term) {
        this.term = term;
        return this;
    }

    public PrivateTaskBuilder status(final PrivateTask.Status status) {
        this.status = status;
        return this;
    }

    public PrivateTaskBuilder type(final PrivateTask.Type type) {
        this.type = type;
        return this;
    }
}