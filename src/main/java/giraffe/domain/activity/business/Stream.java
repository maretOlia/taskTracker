package giraffe.domain.activity.business;

import com.google.common.collect.Sets;
import giraffe.domain.GiraffeEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class Stream extends GiraffeEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stream", cascade = CascadeType.ALL)
    private Set<BusinessTask> backlog = Sets.newHashSet();

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;


    Stream() { }

    public Stream(Long startTime, Long endTime) {
        Assert.notNull(startTime, "Start Time must not be null");
        Assert.notNull(endTime, "End Time must not be null");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Set<BusinessTask> getBacklog() {
        return backlog;
    }

    public void addTaskToBacklog(BusinessTask task) {
        if (!backlog.contains(task)) {
            backlog.add(task);
        }
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stream)) return false;
        if (!super.equals(o)) return false;

        Stream stream = (Stream) o;

        if (!startTime.equals(stream.startTime)) return false;
        return endTime.equals(stream.endTime);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }

}
