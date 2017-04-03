package giraffe.domain.activity.simple;

import giraffe.domain.activity.Task;

import javax.persistence.*;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@Entity
public class SimpleTask extends Task<SimpleTask> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_uuid", referencedColumnName = "uuid")
    private SimpleTask parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "simple_to_do_list_uuid", nullable = false)
    private SimpleToDoList simpleToDoList;

    @Column(nullable = false)
    @Enumerated
    private TaskStatus taskStatus = TaskStatus.CREATED;

    public SimpleTask() {
    }

    @Override
    public SimpleTask self() {
        return this;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public SimpleTask setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public SimpleTask getParent() {
        return parent;
    }

    public SimpleTask setParent(SimpleTask parent) {
        this.parent = parent;
        return this;
    }

    public SimpleToDoList getSimpleToDoList() {
        return simpleToDoList;
    }

    public SimpleTask setSimpleToDoList(SimpleToDoList simpleToDoList) {
        this.simpleToDoList = simpleToDoList;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleTask)) return false;
        if (!super.equals(o)) return false;

        SimpleTask task = (SimpleTask) o;

        if (parent != null ? !parent.getUuid().equals(task.parent.getUuid()) : task.parent != null) return false;
        if (simpleToDoList != null && !simpleToDoList.getUuid().equals(task.simpleToDoList.getUuid())) return false;

        return taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.getUuid().hashCode() : 0);
        result = 31 * result + (simpleToDoList != null ? simpleToDoList.getUuid().hashCode() : 0);
        result = 31 * result + taskStatus.hashCode();

        return result;
    }

    public enum TaskStatus {
        CREATED(0), DONE(1);

        private int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
