package giraffe.domain.activity.business;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class Component {

    private String name;

    private Project project;


    public Component(final String name, final Project project) {
        this.name = name;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void project(final Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name) &&
                Objects.equals(project, component.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, project);
    }

}
