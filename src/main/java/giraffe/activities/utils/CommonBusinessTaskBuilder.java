package giraffe.activities.utils;

import giraffe.activities.business.CommonBusinessTask;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class CommonBusinessTaskBuilder extends BusinessTaskBuilder<CommonBusinessTaskBuilder> {

    private long finalTerm;

    @Override
    public CommonBusinessTaskBuilder self() {
        return this;
    }

    public CommonBusinessTaskBuilder finalTerm(final long finalTerm) {
        this.finalTerm = finalTerm;
        return self();
    }

    @Override
    public CommonBusinessTask build() {
        return new CommonBusinessTask(id, name, comment, assignedTo, links, img, openedBy, status, priority, estimate, finalTerm);
    }
}
