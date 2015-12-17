package giraffe.domain.activity;

import giraffe.domain.GiraffeEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract public class Activity extends GiraffeEntity {

    @NotNull
    protected String name;

    protected String comment;

    protected List<String> references;

    protected List<String> imgs;


    public Activity(final String name,
                    final String comment,
                    final List<String> references,
                    final List<String> imgs) {
        this.name = name;
        this.comment = comment;
        this.references = references;
        this.imgs = imgs;
    }

    protected Activity() {
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

    public List<String> getReferences() {
        return references;
    }

    public List<String> getImgs() {
        return imgs;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;

        return Objects.equals(name, activity.name) &&
                Objects.equals(comment, activity.comment) &&
                Objects.equals(references, activity.references) &&
                Objects.equals(imgs, activity.imgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, comment, references, imgs);
    }
}
