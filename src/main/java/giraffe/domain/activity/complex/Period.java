package giraffe.domain.activity.complex;

import giraffe.domain.activity.Activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Duration of time for specific set of tasks.
 * Analog of 'Stream' in Scrum methodology adopted for simple use case scenarios
 *
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class Period extends Activity<Period> {

    @ManyToOne
    @JoinColumn(name = "project_uuid", referencedColumnName = "uuid", nullable = false)
    private Project project;

    @Column(nullable = false)
    private long timeScheduledToStart; // UTC time

    @Column(nullable = false)
    private long timeScheduledToFinish; // UTC time

    public Period() {
    }

    @Override
    public Period self() {
        return this;
    }

    public Project getProject() {
        return project;
    }

    public Period setProject(Project project) {
        this.project = project;
        return this;
    }

    public Long getTimeScheduledToStart() {
        return timeScheduledToStart;
    }

    public Period setTimeScheduledToStart(Long timeScheduledToStart) {
        this.timeScheduledToStart = timeScheduledToStart;
        return self();
    }

    public Long getTimeScheduledToFinish() {
        return timeScheduledToFinish;
    }

    public Period setTimeScheduledToFinish(Long timeScheduledToFinish) {
        this.timeScheduledToFinish = timeScheduledToFinish;
        return self();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period)) return false;
        if (!super.equals(o)) return false;

        Period period = (Period) o;

        if (timeScheduledToStart != period.timeScheduledToStart) return false;
        if (timeScheduledToFinish != period.timeScheduledToFinish) return false;

        return project.getUuid().equals(period.project.getUuid());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (timeScheduledToStart ^ (timeScheduledToStart >>> 32));
        result = 31 * result + (int) (timeScheduledToFinish ^ (timeScheduledToFinish >>> 32));

        return result;
    }

}
