import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String status = task.isDone() ? "1" : "0";

            if (task instanceof Todo) {
                lines.add("T | " + status
                        + " | " + task.getDesc());
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                lines.add("D | " + status
                        + " | " + deadline.getDesc()
                        + " | " + deadline.getBy());
            } else if (task instanceof Event) {
                Event event = (Event) task;
                lines.add("E | " + status
                        + " | " + event.getDesc()
                        + " | " + event.getFrom()
                        + " | " + event.getTo());
            }
        }

        Files.write(filePath, lines);
    }

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