package giraffe.activities.business;

import giraffe.user.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class CommonBusinessTask extends BusinessTask {

    private long finalTerm;

    public CommonBusinessTask(final int id,
                              final String name,
                              final String comment,
                              final User assignedTo,
                              final Map<String, String> links,
                              final List<String> img,
                              final User openedBy,
                              final Status status,
                              final Priority priority,
                              final int estimate,
                              final long finalTerm) {
        super(id, name, comment, assignedTo, links, img, openedBy, status, priority, estimate);
        this.finalTerm = finalTerm;
    }

    public long getTerm() { return finalTerm; }

    public void setTerm(final long finalTerm) { this.finalTerm = finalTerm; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommonBusinessTask that = (CommonBusinessTask) o;
        return finalTerm == that.finalTerm;
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), finalTerm); }
}
