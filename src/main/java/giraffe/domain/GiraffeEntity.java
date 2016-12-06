package giraffe.domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@MappedSuperclass
public class GiraffeEntity {

    @Id
    @Column(nullable = false)
    protected String uuid = UUID.randomUUID().toString();

    @Column(nullable = false)
    protected Long timeCreated = System.currentTimeMillis();

    protected Long timeDeleted;

    @Column(nullable = false)
    @Enumerated
    protected Status status = Status.ACTIVE;


    public enum Status {
        ACTIVE(1), DELETED(0);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public String getUuid() {
        return uuid;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public Long getTimeDeleted() {
        return timeDeleted;
    }

    public void setTimeDeleted(Long timeDeleted) {
        this.timeDeleted = timeDeleted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiraffeEntity that = (GiraffeEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(status, that.status) &&
                Objects.equals(timeCreated, that.timeCreated) &&
                Objects.equals(timeDeleted, that.timeDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, status, timeCreated, timeDeleted);
    }

}
