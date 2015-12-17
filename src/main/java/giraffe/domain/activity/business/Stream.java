package giraffe.domain.activity.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class Stream {

    private List<BusinessTask> backlog = new ArrayList<>();

    private Long term;


    public Stream(final Long term) {
        this.term = term;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(final Long term) {
        this.term = term;
    }

    public List<BusinessTask> getBacklog() {
        return backlog;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return Objects.equals(backlog, stream.backlog) &&
                Objects.equals(term, stream.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backlog, term);
    }

}
