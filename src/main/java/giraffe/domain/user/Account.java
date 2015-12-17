package giraffe.domain.user;

import giraffe.domain.GiraffeEntity;
import giraffe.domain.GiraffeAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */

public class Account extends GiraffeEntity {

    @NotNull
    protected String login;

    @NotNull
    @JsonIgnore
    protected String password;

    @JsonIgnore
    protected String salt;


    @NotNull
    @Relationship(type = "AUTHORITY", direction = Relationship.OUTGOING)
    protected Set<GiraffeAuthority> authorities;


    public Account(final String login, final String password, final Set<GiraffeAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }


    public void grantAuthorities(final GiraffeAuthority authority) {
        authorities.add(authority);
    }


    public Set<GiraffeAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(final Set<GiraffeAuthority> authorities) {
        this.authorities = authorities;
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
