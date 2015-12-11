package giraffe.domain.user;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NodeEntity
final public class BusinessAccount extends Account {

    public BusinessAccount(final String login, final String password) {
        super(login, password);
    }
}
