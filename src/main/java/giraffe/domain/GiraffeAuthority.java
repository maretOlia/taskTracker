package giraffe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NodeEntity
public class GiraffeAuthority extends GiraffeEntity implements GrantedAuthority {

    @NotNull
    private Role role;


    public GiraffeAuthority() { }

    public GiraffeAuthority(final Role role) {
        this.role = role;
    }


    public enum Role {
        USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), ANONIMOUS("ROLE_ANONYMOUS");

        private String value;

        Role(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    @Override
    @JsonIgnore
    public String getAuthority() {
        return role.getValue();
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

}
