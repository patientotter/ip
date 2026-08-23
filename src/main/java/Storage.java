import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Storage {
    private static final Path FILE_PATH = Path.of("data", "duke.txt");

    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());

        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String status = task.isDone() ? "1" : "0";

            if (task instanceof Todo) {
                lines.add("T | " + status + " | " + task.getDesc());
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                lines.add("D | " + status + " | "
                        + deadline.getDesc() + " | " + deadline.getBy());
            } else if (task instanceof Event) {
                Event event = (Event) task;
                lines.add("E | " + status + " | "
                        + event.getDesc() + " | "
                        + event.getFrom() + " | " + event.getTo());
            }
        }

        Files.write(FILE_PATH, lines);
    }

    public static ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(FILE_PATH);

        for (String line : lines) {
            try {
                String[] parts = line.split(" \\| ");

                Task task;

                switch (parts[0]) {
                    case "T":
                        task = new Todo(parts[2]);
                        break;
                    case "D":
                        task = new Deadline(parts[2], LocalDate.parse(parts[3]));
                        break;
                    case "E":
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                    default:
                        continue;
                }

                if (parts[1].equals("1")) {
                    task.markAsDone();
                }

                tasks.add(task);
            } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
                System.out.println("Warning: A corrupted task was skipped.");
            }
        }

        return tasks;
    }
}