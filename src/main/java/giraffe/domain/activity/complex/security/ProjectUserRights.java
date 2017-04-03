package giraffe.domain.activity.complex.security;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;
import giraffe.domain.activity.complex.Project;

import javax.persistence.*;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class ProjectUserRights extends GiraffeEntity<ProjectUserRights> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_uuid", referencedColumnName = "uuid", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;

    @Enumerated
    @Column(nullable = false)
    private Rights rights = Rights.READ;

    public ProjectUserRights() {
    }

    @Override
    public ProjectUserRights self() {
        return this;
    }

    public Project getProject() {
        return project;
    }

    public ProjectUserRights setProject(Project project) {
        this.project = project;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ProjectUserRights setUser(User user) {
        this.user = user;
        return this;
    }

    public Rights getRights() {
        return rights;
    }

    public ProjectUserRights setRights(Rights rights) {
        this.rights = rights;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectUserRights)) return false;
        if (!super.equals(o)) return false;

        ProjectUserRights projectUserRights = (ProjectUserRights) o;

        if (project != null ? !project.getUuid().equals(projectUserRights.project.getUuid()) : projectUserRights.project.getUuid() != null) return false;
        if (user != null ? !user.getUuid().equals(projectUserRights.user.getUuid()) : projectUserRights.user.getUuid() != null) return false;

        return rights == projectUserRights.rights;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (project != null ? project.getUuid().hashCode() : 0);
        result = 31 * result + (user != null ? user.getUuid().hashCode() : 0);
        result = 31 * result + (rights != null ? rights.hashCode() : 0);

        return result;
    }

    public enum Rights {
        READ(1), READ_WRITE(2);

        private int value;

        Rights(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
