package giraffe.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class GiraffeEntity extends ResourceSupport {

    @JsonIgnore
    private Long id; // neo4j internal id

    protected String uuid;

    protected Long timeCreated;

    protected Status status = Status.ACTIVE;


    public enum Status {
        ACTIVE(1), DELETED(0);

        private int value;

        Status(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(final Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiraffeEntity that = (GiraffeEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(status, that.status) &&
                Objects.equals(timeCreated, that.timeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, status, timeCreated);
    }

}
