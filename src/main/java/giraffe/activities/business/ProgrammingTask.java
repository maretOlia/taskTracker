package giraffe.activities.business;

import giraffe.user.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class ProgrammingTask extends BusinessTask {

    private Type type;


    public ProgrammingTask(final int id,
                           final String name,
                           final String comment,
                           final User assignedTo,
                           final Map<String, String> links,
                           final List<String> img,
                           final User openedBy,
                           final Status status,
                           final Priority priority,
                           final int estimate,
                           final Type type) {
        super(id, name, comment, assignedTo, links, img, openedBy, status, priority, estimate);
        this.type = type;
    }

    public enum Type {
        TASK(0), IDEA(1), BUG(2), BLOCKER(3), FEATURE(4);

        private int value;

        Type(final int value) { this.value = value; }

        public int getValue() { return value; }
    }

    public Type getType() { return type; }

    public void setType(final Type type) { this.type = type; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProgrammingTask that = (ProgrammingTask) o;
        return type == that.type;
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), type); }

}
