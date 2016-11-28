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
//TODO: review inheritance
public class GiraffeAuthority extends GiraffeEntity implements GrantedAuthority {

    @NotNull
    private Role role;


    public GiraffeAuthority(final Role role) {
        this.role = role;
    }


    public enum Role {
        ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), ANONIMOUS("ROLE_ANONYMOUS");

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
