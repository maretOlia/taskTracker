package giraffe.domain.activity.business;

import giraffe.domain.user.BusinessAccount;

import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class ProgrammingTask extends BusinessTask {

    private Type type;


    public ProgrammingTask(final String name,
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
                           final Type type) {
        super(name, comment, references, imgs, taskStatus, priority, estimate, assignedTo, openedBy, project, component);
        this.type = type;
    }

    public enum Type {
        TASK(0), IDEA(1), BUG(2), BLOCKER(3), FEATURE(4);

        private int value;

        Type(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProgrammingTask that = (ProgrammingTask) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }

}
