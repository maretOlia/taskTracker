package giraffe.activities.utils;

import giraffe.activities.business.ProgrammingTask;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class ProgrammingTaskBuilder extends BusinessTaskBuilder<ProgrammingTaskBuilder> {

    private ProgrammingTask.Type type = ProgrammingTask.Type.TASK;

    @Override
    public ProgrammingTaskBuilder self() {
        return this;
    }

    @Override
    public ProgrammingTask build() {
        return new ProgrammingTask(id, name, comment, assignedTo, links, img, openedBy, status, priority, estimate, type);
    }

    public ProgrammingTaskBuilder type(final ProgrammingTask.Type type) {
        this.type = type;
        return self();
    }
}
