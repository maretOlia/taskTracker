package giraffe.domain.activity.business;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class Project extends GiraffeEntity<Project> {

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Component> components = Sets.newHashSet();


    Project() { }

    @Override
    public Project self() {
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public Project addComponent(Component component) {
        if (components.contains(component)) {
            components.add(component);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        if (!super.equals(o)) return false;

        Project project = (Project) o;

        return name.equals(project.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}

