package giraffe.domain.activity.business;

import giraffe.domain.user.BusinessAccount;

import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class CommonBusinessTask extends BusinessTask {

    private Long term;


    public CommonBusinessTask(final String name,
                           final String comment,
                           final List<String> references,
                           final List<String> imgs,
                           final TaskStatus taskStatus,
                           final Priority priority,
                           final Integer estimate,
                           final BusinessAccount assignedTo,
                           final BusinessAccount openedBy,
                           final Project project,
                           final Component component,
                           final Long term) {
        super(name, comment, references, imgs, taskStatus, priority, estimate, assignedTo, openedBy, project, component);
        this.term = term;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(final Long finalTerm) {
        this.term = finalTerm;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommonBusinessTask that = (CommonBusinessTask) o;
        return Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), term);
    }
}
