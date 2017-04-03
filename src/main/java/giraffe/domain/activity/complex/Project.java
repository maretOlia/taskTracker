package giraffe.domain.activity.complex;

import giraffe.domain.activity.Activity;

import javax.persistence.Entity;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class Project extends Activity<Project> {

    public Project() {
    }

    @Override
    public Project self() {
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

