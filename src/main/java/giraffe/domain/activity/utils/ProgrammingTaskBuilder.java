package giraffe.domain.activity.utils;

import giraffe.domain.activity.business.ProgrammingTask;

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
        return new ProgrammingTask(name, comment, references, img, status, priority, estimate, type, openedBy, assignedTo);
    }

    public ProgrammingTaskBuilder type(final ProgrammingTask.Type type) {
        this.type = type;
        return self();
    }
}
