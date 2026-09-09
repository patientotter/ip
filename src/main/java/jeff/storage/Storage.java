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
    private static final String TODO_TYPE_CODE = "T";
    private static final String DEADLINE_TYPE_CODE = "D";
    private static final String EVENT_TYPE_CODE = "E";

    private static final String INCOMPLETE_STATUS_CODE = "0";
    private static final String COMPLETE_STATUS_CODE = "1";

    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";

    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int FIRST_DETAIL_INDEX = 3;
    private static final int SECOND_DETAIL_INDEX = 4;

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
        String status = task.isDone()
                ? COMPLETE_STATUS_CODE
                : INCOMPLETE_STATUS_CODE;

        if (task instanceof Todo) {
            return TODO_TYPE_CODE + FIELD_SEPARATOR
                    + status + FIELD_SEPARATOR
                    + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return DEADLINE_TYPE_CODE + FIELD_SEPARATOR
                    + status + FIELD_SEPARATOR
                    + deadline.getDescription() + FIELD_SEPARATOR
                    + deadline.getDueDate();
        }

        if (task instanceof Event) {
            Event event = (Event) task;
            return EVENT_TYPE_CODE + FIELD_SEPARATOR
                    + status + FIELD_SEPARATOR
                    + event.getDescription() + FIELD_SEPARATOR
                    + event.getStartTime() + FIELD_SEPARATOR
                    + event.getEndTime();
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
        String[] parts = line.split(FIELD_SEPARATOR_REGEX);
        Task task;

        switch (parts[TYPE_INDEX]) {
            case TODO_TYPE_CODE:
                task = new Todo(parts[DESCRIPTION_INDEX]);
                break;
            case DEADLINE_TYPE_CODE:
                task = new Deadline(
                        parts[DESCRIPTION_INDEX],
                        LocalDate.parse(parts[FIRST_DETAIL_INDEX]));
                break;
            case EVENT_TYPE_CODE:
                task = new Event(
                        parts[DESCRIPTION_INDEX],
                        parts[FIRST_DETAIL_INDEX],
                        parts[SECOND_DETAIL_INDEX]);
                break;
            default:
                return null;
        }

        if (parts[STATUS_INDEX].equals(COMPLETE_STATUS_CODE)) {
            task.markAsDone();
        }

        return task;
    }
}
