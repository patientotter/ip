package jeff.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void find_matchingKeyword_returnsMatchingTasks() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Deadline(
                "return book",
                LocalDate.of(2026, 9, 1)));

        ArrayList<Task> matchingTasks = tasks.find("BOOK");

        assertEquals(2, matchingTasks.size());
        assertEquals(
                "read book",
                matchingTasks.get(0).getDesc());
        assertEquals(
                "return book",
                matchingTasks.get(1).getDesc());
    }

    @Test
    public void find_noMatchingKeyword_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        ArrayList<Task> matchingTasks = tasks.find("groceries");

        assertEquals(0, matchingTasks.size());
    }
}