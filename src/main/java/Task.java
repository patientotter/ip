public class Task {
    protected String description;
    protected boolean isDone;
    protected boolean istodo;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.istodo = false;
    }

    public String getStatus() {
        return (isDone ? "[X] " : "[ ] ");
    }

    public String getDesc() {
        return this.description;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return getStatus() + " " + description;
    }
}