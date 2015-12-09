package giraffe.domain.user;

import giraffe.domain.GiraffeEntity;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */

public class Account extends GiraffeEntity {

    protected User user;


    public Account(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void user(final User user) {
        this.user = user;
    }
}
