package giraffe.activities;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class Term {

    private long startTime;

    private long endTime;

    /*TODO  create different constructors
            - start time to end time
            - start time + period
            - start time = end time
    */

    public long getStartTime() { return startTime; }

    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return startTime == term.startTime &&
                endTime == term.endTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}
