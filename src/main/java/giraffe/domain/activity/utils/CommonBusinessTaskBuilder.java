package giraffe.domain.activity.utils;

import giraffe.domain.activity.business.CommonBusinessTask;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class CommonBusinessTaskBuilder extends BusinessTaskBuilder<CommonBusinessTaskBuilder> {

    private Long term;


    @Override
    public CommonBusinessTaskBuilder self() {
        return this;
    }

    public CommonBusinessTaskBuilder term(final Long finalTerm) {
        this.term = finalTerm;
        return self();
    }

    @Override
    public CommonBusinessTask build() {
        return new CommonBusinessTask(name, comment, references, img, taskStatus, priority, estimate, assignedTo, openedBy, project, component, term);
    }
}
