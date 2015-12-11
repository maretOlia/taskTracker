package giraffe.domain.user;

import giraffe.domain.GiraffeEntity;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */

public class Account extends GiraffeEntity {

    protected String login;

    protected String password;

    protected String salt;

    public Account(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return Objects.equals(login, account.login) &&
                Objects.equals(password, account.password) &&
                Objects.equals(salt, account.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, salt);
    }
}
