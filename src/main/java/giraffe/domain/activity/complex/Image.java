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
public class Image extends GiraffeEntity<Image> {

    @Column(name = "path", nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "task_uuid", referencedColumnName = "uuid", nullable = false)
    private ComplexTask task;

    public Image() { }

    @Override
    public Image self() {
        return this;
    }

    public String getPath() {
        return path;
    }

    public Image setPath(String path) {
        this.path = path;
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
        if (!(o instanceof Image)) return false;
        if (!super.equals(o)) return false;

        Image image = (Image) o;

        return task.getUuid().equals(image.task.getUuid()) && path.equals(image.path);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + task.getUuid().hashCode();

        return result;
    }

}
