package giraffe.domain;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class GiraffeEntity {

    private Long id; // neo4j internal id

    protected String uuid;

    protected Long timeCreated;


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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiraffeEntity that = (GiraffeEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(timeCreated, that.timeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, timeCreated);
    }
}
