package giraffe.activities.household;

import giraffe.activities.Activity;
import giraffe.activities.Term;
import giraffe.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class PrivateTask extends Activity {

    private List<PrivateTask> subTasks = new ArrayList<>();

    private Status status;

    private Type type;

    private Term term;

    public PrivateTask(final int id,
                       final String name,
                       final String comment,
                       final User assignedTo,
                       final Map<String, String> links,
                       final List<String> img,
                       final Status status,
                       final Type type,
                       final Term term) {
        super(id, name, comment, assignedTo, links, img);
        this.status = status;
        this.type = type;
        this.term = term;
    }

    public enum Type  {
        TASK(0), EVENT(1), PURCHASE(2), GIFT(3), EDUCATION(4), JOURNEY(5);

        private int value;

        Type(final int value) { this.value = value; }

        public int getValue() { return value; }
    }

    public enum Status {
        OPEN(0), DELAYED(1), DONE(2);

        private int value;

        Status(final int value) { this.value = value;}

        public int getValue() { return value; }
    }

    public List<PrivateTask> getSubTasks() {
        return subTasks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Term getTerm() { return term; }

    public void setTerm(final Term term) { this.term = term; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrivateTask that = (PrivateTask) o;
        return Objects.equals(subTasks, that.subTasks) &&
                status == that.status &&
                type == that.type &&
                Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), subTasks, status, type, term); }
}
