package jeff.task;

import java.util.ArrayList;
import java.util.Locale;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Finds tasks whose descriptions contain the specified keyword.
     * The search is case-insensitive.
     *
     * @param keyword keyword to search for
     * @return tasks with descriptions containing the keyword
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (Task task : tasks) {
            String normalizedDescription =
                    task.getDesc().toLowerCase(Locale.ROOT);

            if (normalizedDescription.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}