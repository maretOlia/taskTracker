package giraffe.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class User extends GiraffeEntity implements Serializable {

    @Column(nullable = false)
    private String login;

    @Column(nullable = false, name="pass_hash")
    @JsonIgnore
    private String passwordHash;

    @ManyToMany
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "authority_uuid", referencedColumnName = "uuid"))
    private Set<GiraffeAuthority> authorities = Sets.newHashSet();


    User() { }

    public User(String login, String passwordHash) {
        Assert.notNull(login, "Login Hash must not be null");
        Assert.notNull(passwordHash, "Password Hash must not be null");
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<GiraffeAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(GiraffeAuthority authority) {
        if(!authorities.contains(authority)){
            authorities.add(authority);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        return passwordHash.equals(user.passwordHash);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + passwordHash.hashCode();
        return result;
    }
}
