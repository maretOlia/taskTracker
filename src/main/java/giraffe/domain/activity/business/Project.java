package giraffe.domain.activity.business;

import giraffe.domain.activity.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class Project extends Activity {

    private List<BusinessTask> backlog = new ArrayList<>();

    private List<Stream> streams = new ArrayList<>();

    private List<Component> components = new ArrayList<>();


    public Project(final String name,
                   final String comment,
                   final List<String> references,
                   final List<String> img) {
        super(name, comment, references, img);
    }

    public List<BusinessTask> getBacklog() {
        return backlog;
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public List<Component> getComponents() {
        return components;
    }

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
    public int hashCode() {
        return Objects.hash(super.hashCode(), backlog, streams, components);
    }
}
