package giraffe.activities.business;

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
public class Project extends Activity {

    private List<BusinessTask> backlog = new ArrayList<>();

    private List<Stream> streams = new ArrayList<>();

    private List<Component> components = new ArrayList<>();


    public Project(final int id,
                   final String name,
                   final String comment,
                   final Term term,
                   final User assignedTo,
                   final Map<String, String> links,
                   final List<String> img) {
        super(id, name, comment, term, assignedTo, links, img);
    }

    public List<BusinessTask> getBacklog() { return backlog; }

    public List<Stream> getStreams() { return streams; }

    public List<Component> getComponents() { return components; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Project project = (Project) o;
        return Objects.equals(backlog, project.backlog) &&
                Objects.equals(streams, project.streams) &&
                Objects.equals(components, project.components);
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), backlog, streams, components); }
}
