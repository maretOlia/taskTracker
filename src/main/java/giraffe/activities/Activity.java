package giraffe.activities;

import giraffe.user.User;

import java.util.*;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract public class Activity {

    private int id;

    private String name;

    private String comment;

    private User assignedTo;

    private Map<String, String> links = new HashMap<>();

    private List<String> img = new ArrayList<>();


    public Activity(final int id,
                    final String name,
                    final String comment,
                    final User assignedTo,
                    final Map<String, String> links,
                    final List<String> img) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.assignedTo = assignedTo;
        this.links = links;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(final User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(final Map<String, String> links) {
        this.links = links;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(final List<String> img) {
        this.img = img;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id &&
                Objects.equals(name, activity.name) &&
                Objects.equals(comment, activity.comment) &&
                Objects.equals(assignedTo, activity.assignedTo) &&
                Objects.equals(links, activity.links) &&
                Objects.equals(img, activity.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, comment, assignedTo, links, img);
    }
}
