package giraffe.activities.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
final public class Component {

    private String name;

    private List<BusinessTask> tasks = new ArrayList<>();


    public Component(final String name) { this.name = name; }

    public String getName() { return name; }

    public void setName(final String name) { this.name = name; }

    public List<BusinessTask> getTasks() { return tasks; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }
}
