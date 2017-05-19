package giraffe.domain.activity;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@MappedSuperclass
public abstract class Activity<T extends Activity> extends GiraffeEntity<T> {

    @NotNull
    @Size(min=2, max=30)
    @Column(nullable = false)
    protected String name;

    protected String comment; // TODO separate with additional DB storage

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_user_uuid", referencedColumnName = "uuid", nullable = false)
    protected User createdBy;

    protected Activity() {
    }

    public String getName() {
        return name;
    }

    public T setName(String name) {
        this.name = name;
        return self();
    }

    public String getComment() {
        return comment;
    }

    public T setComment(String comment) {
        this.comment = comment;
        return self();
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public T setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return self();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        if (!super.equals(o)) return false;

        Activity activity = (Activity) o;

        if (name != null && !name.equals(activity.name)) return false;
        if (createdBy != null && !createdBy.getUuid().equals(activity.createdBy.getUuid())) return false;

        return comment != null ? comment.equals(activity.comment) : activity.comment == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.getUuid().hashCode() : 0);

        return result;
    }

}

