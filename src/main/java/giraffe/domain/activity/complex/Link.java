package giraffe.domain.activity.complex;

import giraffe.domain.GiraffeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class Link extends GiraffeEntity<Link> {

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "task_uuid", referencedColumnName = "uuid", nullable = false)
    private ComplexTask task;

    public Link() { }

    @Override
    public Link self() {
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Link setPath(String url) {
        this.url = url;
        return self();
    }

    public ComplexTask getTask() {
        return task;
    }

    public void setTask(ComplexTask task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;
        if (!super.equals(o)) return false;

        Link link = (Link) o;

        return task.getUuid().equals(link.task.getUuid()) && url.equals(link.url);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + task.getUuid().hashCode();

        return result;
    }

}

