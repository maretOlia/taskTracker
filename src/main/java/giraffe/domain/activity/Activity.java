package giraffe.domain.activity;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.account.User;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@MappedSuperclass
public abstract class Activity<T extends Activity> extends GiraffeEntity<T> {

    @Column(nullable = false)
    protected String name;

    protected String comment;

    @OneToOne
    @JoinColumn(name = "opened_by_user_uuid", nullable = false)
    protected User openedBy;

    @OneToOne
    @JoinColumn(name = "assigned_to_user_uuid")
    protected User assignedTo;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    protected Set<Image> imgs = Sets.newHashSet();


    protected Activity() { }


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

    public T setOpenedBy(User openedBy) {
        this.openedBy = openedBy;
        return self();
    }

    public Set<Image> getImgs() {
        return imgs;
    }

    public T addImg(Image img) {
        if (imgs.contains(img)) {
            imgs.add(img);
        }
        return self();
    }

    public User getOpenedBy() {
        return openedBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public T setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
        return self();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        if (!super.equals(o)) return false;

        Activity activity = (Activity) o;

        if (!name.equals(activity.name)) return false;
        if (comment != null ? !comment.equals(activity.comment) : activity.comment != null) return false;
        if (!openedBy.getUuid().equals(activity.openedBy.getUuid())) return false;
        return assignedTo != null ? assignedTo.getUuid().equals(activity.assignedTo.getUuid()) : activity.assignedTo == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + openedBy.getUuid().hashCode();
        result = 31 * result + (assignedTo != null ? assignedTo.getUuid().hashCode() : 0);
        return result;
    }
}

