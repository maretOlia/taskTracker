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
                              final Status status,
                              final Priority priority,
                              final int estimate,
                              final long term,
                              final BusinessAccount assignedTo,
                              final BusinessAccount openedBy) {
        super(name, comment, references, imgs, status, priority, estimate, openedBy, assignedTo);
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
        return term == that.term;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), term);
    }
}
