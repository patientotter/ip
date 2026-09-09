package jeff.task;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Stores and manages the user's tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks initial tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Task list must not be null";
        this.tasks = tasks;
    }
    /**
     * Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index zero-based index of the task
     * @return task at the specified index
     */
    public Task getTask(int index) {
        assert index >= 0 && index < tasks.size()
                : "Task index must be within the task list";
        return tasks.get(index);
    }
    /**
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        assert task != null : "Task to add must not be null";
        tasks.add(task);
    }
    /**
     * Deletes and returns the task at the specified index.
     *
     * @param index zero-based index of the task
     * @return deleted task
     */
    public Task deleteTask(int index) {
        assert index >= 0 && index < tasks.size()
                : "Task index must be within the task list";
        return tasks.remove(index);
    }
    /**
     * Returns all tasks in the list.
     *
     * @return underlying collection of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks whose descriptions contain the specified keyword.
     * The search is case-insensitive.
     *
     * @param keyword keyword to search for
     * @return tasks with descriptions containing the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Search keyword must not be null";
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (Task task : tasks) {
            String normalizedDescription =
                    task.getDescription().toLowerCase(Locale.ROOT);

            if (normalizedDescription.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
