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
public class Component extends GiraffeEntity<Component> {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_uuid")
    private Project project;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "component", cascade = CascadeType.ALL)
    private Set<BusinessTask> businessTasks = Sets.newHashSet();


    Component() { }

    @Override
    public Component self() {
        return this;
    }


    public String getName() {
        return name;
    }

    public Component setName(String name) {
        this.name = name;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public Component setProject(Project project) {
        this.project = project;
        return this;
    }

    public Set<BusinessTask> getBusinessTasks() {
        return businessTasks;
    }

    public Component addBusinessTask(BusinessTask businessTask) {
        if (businessTasks.contains(businessTask)) {
            businessTasks.add(businessTask);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Component)) return false;
        if (!super.equals(o)) return false;

        Component component = (Component) o;

        if (!name.equals(component.name)) return false;
        return project.getUuid().equals(component.project.getUuid());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + project.getUuid().hashCode();
        return result;
    }
}
