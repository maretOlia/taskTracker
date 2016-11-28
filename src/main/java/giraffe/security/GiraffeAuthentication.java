package giraffe.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class GiraffeAuthentication implements Authentication {

    private final GiraffePrivateUserDetails userDetails;

    private boolean authenticated = true;


    public GiraffeAuthentication(final GiraffePrivateUserDetails user) {
        this.userDetails = user;
    }


    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public GiraffePrivateUserDetails getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

}
