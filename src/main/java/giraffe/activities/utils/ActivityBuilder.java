package giraffe.activities.utils;

import giraffe.activities.Activity;
import giraffe.user.User;

import java.util.List;
import java.util.Map;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract class ActivityBuilder<T extends ActivityBuilder> {

    protected int id;

    protected String name;

    protected String comment;

    protected User assignedTo;

    protected Map<String, String> links;

    protected List<String> img;


    abstract public T self();

    abstract public Activity build();


    public T id(final int id) {
        this.id = id;
        return self();
    }

    public T name(final String name) {
        this.name = name;
        return self();
    }

    public T comment(final String comment) {
        this.comment = comment;
        return self();
    }

    public T assignedTo(final User assignedTo) {
        this.assignedTo = assignedTo;
        return self();
    }

    public T links(final Map<String, String> links) {
        this.links = links;
        return self();
    }

    public T img(final List<String> img) {
        this.img = img;
        return self();
    }
}
