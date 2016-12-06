package giraffe.domain.activity.business;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class Component extends GiraffeEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_uuid")
    private Project project;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "component", cascade = CascadeType.ALL)
    private Set<BusinessTask> businessTasks = Sets.newHashSet();

    Component() { }

    public Component(String name, Project project) {
        Assert.notNull(project, "Project must not be null");
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<BusinessTask> getBusinessTasks() {
        return businessTasks;
    }

    public void addBusinessTask(BusinessTask businessTask) {
        if (businessTasks.contains(businessTask)) {
            businessTasks.add(businessTask);
        }
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
