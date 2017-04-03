package giraffe.domain.activity.complex;

import giraffe.domain.activity.Task;

import javax.persistence.*;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class ComplexTask extends Task<ComplexTask> {

    @ManyToOne
    @JoinColumn(name = "parent_uuid", referencedColumnName = "uuid")
    private ComplexTask parent;

    @ManyToOne
    @JoinColumn(name = "project_uuid", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "period_uuid")
    private Period period;

    @Enumerated
    @Column(nullable = false)
    private Priority priority = Priority.NORMAL;

    private Double estimate; // hours

    private Double progress = 0.0; // hours

    @Enumerated
    @Column(name = "task_status", nullable = false)
    private TaskStatus taskStatus = TaskStatus.CREATED;

    private Long timeStatusChanged = super.timeCreated;  // UTC time

    public ComplexTask() {
    }

    @Override
    public ComplexTask self() {
        return this;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public ComplexTask updateTaskStatus(TaskStatus status){
        this.taskStatus = status;
        this.timeStatusChanged = System.currentTimeMillis();

        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public ComplexTask setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public Double getEstimate() {
        return estimate;
    }

    public ComplexTask setEstimate(Double estimate) {
        this.estimate = estimate;
        return this;
    }

    public Double getProgress() {
        return progress;
    }

    public ComplexTask addProgress(Double progressToAdd) {
        this.progress += progressToAdd;
        return this;
    }

    public Period getPeriod() {
        return period;
    }

    public ComplexTask setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public ComplexTask setProject(Project project) {
        this.project = project;
        return self();
    }

    public ComplexTask getParent() {
        return parent;
    }

    public ComplexTask setParent(ComplexTask parent) {
        this.parent = parent;
        return this;
    }

    public Long getTimeStatusChanged() {
        return timeStatusChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexTask)) return false;
        if (!super.equals(o)) return false;

        ComplexTask task = (ComplexTask) o;

        if (taskStatus != task.taskStatus) return false;
        if (priority != task.priority) return false;
        if (project != null ? !project.getUuid().equals(task.project.getUuid()) : task.getProject() != null) return false;
        if (parent != null ? !parent.getUuid().equals(task.parent.getUuid()) : task.parent != null) return false;
        if (estimate != null ? !estimate.equals(task.estimate) : task.estimate != null) return false;
        if (progress != null ? !progress.equals(task.progress) : task.progress != null) return false;
        if (timeStatusChanged != null ? !timeStatusChanged.equals(task.timeStatusChanged) : task.timeStatusChanged != null) return false;

        return period != null ? period.getUuid().equals(task.period.getUuid()) : task.period == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + taskStatus.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + (period != null ? period.getUuid().hashCode() : 0);
        result = 31 * result + (project != null ? project.getUuid().hashCode() : 0);
        result = 31 * result + (parent != null ? parent.getUuid().hashCode() : 0);
        result = 31 * result + (estimate != null ? estimate.hashCode() : 0);
        result = 31 * result + (progress != null ? progress.hashCode() : 0);
        result = 31 * result + (timeStatusChanged != null ? timeStatusChanged.hashCode() : 0);

        return result;
    }

    public enum TaskStatus {
        CREATED(0), IN_PROGRESS(1), DELAYED(2), DONE(3);

        private int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Priority {
        MINOR(0), NORMAL(1), CRITICAL(2);

        private int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
