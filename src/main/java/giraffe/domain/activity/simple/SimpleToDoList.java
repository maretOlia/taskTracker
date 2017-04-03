package giraffe.domain.activity.simple;

import giraffe.domain.activity.Activity;

import javax.persistence.Entity;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class SimpleToDoList extends Activity<SimpleToDoList> {

    public SimpleToDoList() { }

    @Override
    public SimpleToDoList self() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
