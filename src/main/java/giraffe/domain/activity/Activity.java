package giraffe.domain.activity;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import giraffe.domain.account.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@MappedSuperclass
public class Activity extends GiraffeEntity {

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


    protected Activity() {
    }

    protected Activity(String name, User openedBy) {
        super();
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(openedBy, "Opened By must not be null");
        this.name = name;
        this.openedBy = openedBy;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Image> getImgs() {
        return imgs;
    }

    public void addImg(Image img) {
        if (imgs.contains(img)) {
            imgs.add(img);
        }
    }

    public User getOpenedBy() {
        return openedBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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

