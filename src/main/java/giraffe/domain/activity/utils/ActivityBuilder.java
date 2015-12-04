package giraffe.domain.activity.utils;

import com.google.common.collect.Lists;
import giraffe.domain.activity.Activity;

import java.util.List;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
abstract class ActivityBuilder<T extends ActivityBuilder> {

    protected String name;

    protected String comment;

    protected List<String> references = Lists.newArrayList();

    protected List<String> img = Lists.newArrayList();


    abstract public T self();

    abstract public Activity build();

    public T name(final String name) {
        this.name = name;
        return self();
    }

    public T comment(final String comment) {
        this.comment = comment;
        return self();
    }

    public T links(final  List<String> links) {
        this.references = links;
        return self();
    }

    public T img(final List<String> img) {
        this.img = img;
        return self();
    }
}
