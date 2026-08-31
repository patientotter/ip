package jeff.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.Todo;

public class StorageTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    public void saveAndLoadTasks_multipleTaskTypes_restoresTasks()
            throws IOException {
        Path filePath =
                temporaryDirectory.resolve("data").resolve("duke.txt");
        Storage storage = new Storage(filePath.toString());

        ArrayList<Task> originalTasks = new ArrayList<>();

        Todo todo = new Todo("read book");
        todo.markAsDone();

        originalTasks.add(todo);
        originalTasks.add(
                new Deadline(
                        "return book",
                        LocalDate.of(2026, 9, 15)));
        originalTasks.add(
                new Event(
                        "project meeting",
                        "1400",
                        "1600"));

        storage.saveTasks(originalTasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(3, loadedTasks.size());

        assertInstanceOf(Todo.class, loadedTasks.get(0));
        assertEquals("read book", loadedTasks.get(0).getDescription());
        assertTrue(loadedTasks.get(0).isDone());

        assertInstanceOf(Deadline.class, loadedTasks.get(1));
        Deadline loadedDeadline =
                (Deadline) loadedTasks.get(1);
        assertEquals("return book", loadedDeadline.getDescription());
        assertEquals(
                LocalDate.of(2026, 9, 15),
                loadedDeadline.getDueDate());
        assertFalse(loadedDeadline.isDone());

        assertInstanceOf(Event.class, loadedTasks.get(2));
        Event loadedEvent = (Event) loadedTasks.get(2);
        assertEquals("project meeting", loadedEvent.getDescription());
        assertEquals("1400", loadedEvent.getStartTime());
        assertEquals("1600", loadedEvent.getEndTime());
    }

    @Test
    public void loadTasks_missingFile_returnsEmptyList()
            throws IOException {
        Path filePath =
                temporaryDirectory.resolve("missing").resolve("duke.txt");
        Storage storage = new Storage(filePath.toString());

        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertTrue(loadedTasks.isEmpty());
    }

    @Test
    public void saveTasks_missingParentDirectory_createsFile()
            throws IOException {
        Path filePath =
                temporaryDirectory.resolve("new-data").resolve("duke.txt");
        Storage storage = new Storage(filePath.toString());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));

        storage.saveTasks(tasks);

        assertTrue(filePath.toFile().exists());
    }
}