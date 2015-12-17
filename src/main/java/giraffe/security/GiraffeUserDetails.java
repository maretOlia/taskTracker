package giraffe.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import giraffe.domain.GiraffeAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class GiraffeUserDetails implements UserDetails {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Set<GiraffeAuthority> authorities;

    @NotNull
    private long expires;

    @NotNull
    @JsonProperty("accountExpired")
    private boolean accountExpired;

    @NotNull
    @JsonProperty("accountLocked")
    private boolean accountLocked;

    @NotNull
    @JsonProperty("credentialsExpired")
    private boolean credentialsExpired;

    @NotNull
    @JsonProperty("enabled")
    private boolean enabled;


    public GiraffeUserDetails() { }

    public GiraffeUserDetails(final String username,
                              final String password,
                              final Set<GiraffeAuthority> authorities,
                              final boolean accountExpired,
                              final boolean accountLocked,
                              final boolean credentialsExpired,
                              final boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
    }

    @Override
    public Set<GiraffeAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    public void setAuthorities(Set<GiraffeAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(final boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(final boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiraffeUserDetails that = (GiraffeUserDetails) o;
        return expires == that.expires &&
                accountExpired == that.accountExpired &&
                accountLocked == that.accountLocked &&
                credentialsExpired == that.credentialsExpired &&
                enabled == that.enabled &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, authorities, expires, accountExpired, accountLocked, credentialsExpired, enabled);
    }

}