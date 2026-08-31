package jeff.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void find_matchingKeyword_returnsMatchingTasks() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("buy groceries"));
        tasks.addTask(new Deadline(
                "return book",
                LocalDate.of(2026, 9, 1)));

        ArrayList<Task> matchingTasks = tasks.findTasks("BOOK");

        assertEquals(2, matchingTasks.size());
        assertEquals(
                "read book",
                matchingTasks.get(0).getDescription());
        assertEquals(
                "return book",
                matchingTasks.get(1).getDescription());
    }

    @Test
    public void find_noMatchingKeyword_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));

        ArrayList<Task> matchingTasks = tasks.findTasks("groceries");

        assertEquals(0, matchingTasks.size());
    }
}