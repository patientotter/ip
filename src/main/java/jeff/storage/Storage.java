package jeff.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.Todo;

/**
 * Loads tasks from and saves tasks to the hard disk.
 */
public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Saves the supplied tasks to the configured data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the tasks cannot be saved.
     */
    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String serializedTask = serializeTask(task);

            if (serializedTask != null) {
                lines.add(serializedTask);
            }
        }

        Files.write(filePath, lines);
    }

    /**
     * Converts a task into its storage representation.
     *
     * @param task Task to serialize.
     * @return Serialized task, or null for an unsupported task type.
     */
    private String serializeTask(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + status
                    + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + status
                    + " | " + deadline.getDescription()
                    + " | " + deadline.getDueDate();
        }

        if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + status
                    + " | " + event.getDescription()
                    + " | " + event.getStartTime()
                    + " | " + event.getEndTime();
        }

        return null;
    }

    /**
     * Loads tasks from the configured data file.
     *
     * @return tasks loaded from the file
     * @throws IOException if the file cannot be read
     */
    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            try {
                Task task = parseTask(line);

                if (task != null) {
                    tasks.add(task);
                }
            } catch (ArrayIndexOutOfBoundsException
                     | DateTimeParseException e) {
                // Skip corrupted lines instead of crashing the program.
            }
        }

        return tasks;
    }

    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        Task task;

        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                task = new Deadline(
                        parts[2],
                        LocalDate.parse(parts[3]));
                break;
            case "E":
                task = new Event(
                        parts[2],
                        parts[3],
                        parts[4]);
                break;
            default:
                return null;
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}
