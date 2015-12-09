package giraffe.domain.activity.business;

import giraffe.domain.activity.Activity;
import giraffe.domain.user.BusinessAccount;

import java.util.List;
import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public abstract class BusinessTask extends Activity {

    protected TaskStatus taskStatus;

    protected Priority priority;

    protected Integer estimate;

    protected BusinessAccount assignedTo;

    protected BusinessAccount openedBy;

    protected Component component;

    protected Project project;


    private BusinessTask() { }

    public BusinessTask(final String name,
                        final String comment,
                        final List<String> references,
                        final List<String> imgs,
                        final TaskStatus taskStatus,
                        final Priority priority,
                        final Integer estimate,
                        final BusinessAccount assignedTo,
                        final BusinessAccount openedBy,
                        final Project project,
                        final Component component) {
        super(name, comment, references, imgs);
        this.openedBy = openedBy;
        this.taskStatus = taskStatus;
        this.priority = priority;
        this.estimate = estimate;
        this.openedBy = openedBy;
        this.assignedTo = assignedTo;
        this.project = project;
        this.component = component;
    }

    public enum TaskStatus {
        OPEN(0), IN_PROGRESS(1), DELAYED(2), DONE(3), NEEDS_REVIEW(4), CLOSED(5);

        private int value;

        TaskStatus(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Priority {
        TRIVIAL(0), MINOR(1), MAJOR(2), CRITICAL(3), BLOCKER(4);

        private int value;

        Priority(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Component getComponent() {
        return component;
    }

    public void component(Component component) {
        this.component = component;
    }

    public Project getProject() {
        return project;
    }

    public void project(Project project) {
        this.project = project;
    }

    public BusinessAccount getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(final BusinessAccount openedBy) {
        this.openedBy = openedBy;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(final TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(final Integer estimate) {
        this.estimate = estimate;
    }

    public BusinessAccount getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(final BusinessAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BusinessTask that = (BusinessTask) o;
        return taskStatus == that.taskStatus &&
                priority == that.priority &&
                Objects.equals(estimate, that.estimate) &&
                Objects.equals(assignedTo, that.assignedTo) &&
                Objects.equals(openedBy, that.openedBy) &&
                Objects.equals(project, that.project) &&
                Objects.equals(component, that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), taskStatus, priority, estimate, assignedTo, openedBy, project, component);
    }
}
