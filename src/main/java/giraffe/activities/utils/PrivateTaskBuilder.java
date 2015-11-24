package giraffe.activities.utils;

import giraffe.activities.household.PrivateTask;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class PrivateTaskBuilder extends ActivityBuilder<PrivateTaskBuilder> {

    private PrivateTask.Status status = PrivateTask.Status.OPEN;

    private PrivateTask.Type type = PrivateTask.Type.TASK;


    @Override
    public PrivateTaskBuilder self() { return this; }

    @Override
    public PrivateTask build() {
        return new PrivateTask(id, name, comment, term, assignedTo, links, img, status, type);
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