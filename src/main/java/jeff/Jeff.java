package jeff;

import jeff.parser.Parser;
import jeff.storage.Storage;
import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.TaskList;
import jeff.task.Todo;
import jeff.ui.Ui;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Runs the Jeff chatbot and coordinates its main components.
 */
public class Jeff {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Jeff(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;

        try {
            loadedTasks = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showMessage(
                    "Unable to load saved tasks. "
                            + "Starting with an empty list.");
            loadedTasks = new TaskList();
        }

        tasks = loadedTasks;
    }

    public void run() {
        ui.showWelcome();

        label:
        while (true) {
            String input = ui.readCommand();
            String command = Parser.getCommandWord(input);

            ui.showLine();

            switch (command) {
                case "bye":
                    ui.showGoodbye();
                    ui.showLine();
                    break label;
                case "list":
                    showTaskList();
                    break;
                case "mark":
                    markTask(input);
                    break;
                case "unmark":
                    unmarkTask(input);
                    break;
                case "todo":
                    addTodo(input);
                    break;
                case "deadline":
                    addDeadline(input);
                    break;
                case "event":
                    addEvent(input);
                    break;
                case "delete":
                    deleteTask(input);
                    break;
                case "find":
                    findTasks(input);
                    break;
                default:
                    ui.showMessage("Invalid command.");
                    break;
            }

            ui.showLine();
        }

        ui.close();
    }

    private void showTaskList() {
        ui.showMessage("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    private void markTask(String input) {
        try {
            int taskNumber = Parser.parseTaskNumber(input);

            if (!isValidTaskNumber(taskNumber)) {
                ui.showMessage(
                        "OOPS! That task number does not exist.");
                return;
            }

            Task task = tasks.getTask(taskNumber - 1);
            task.markAsDone();
            saveTasks();

            ui.showMessage(
                    "Nice! I've marked this task as done:");
            ui.showMessage(
                    "  " + task.getStatus()
                            + " " + task.getDescription());
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void unmarkTask(String input) {
        try {
            int taskNumber = Parser.parseTaskNumber(input);

            if (!isValidTaskNumber(taskNumber)) {
                ui.showMessage(
                        "That task number does not exist.");
                return;
            }

            Task task = tasks.getTask(taskNumber - 1);
            task.unmarkAsDone();
            saveTasks();

            ui.showMessage(
                    "I've marked this task as undone:");
            ui.showMessage(
                    "  " + task.getStatus()
                            + " " + task.getDescription());
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addTodo(String input) {
        try {
            Todo todo = Parser.parseTodo(input);
            tasks.addTask(todo);
            saveTasks();

            showTaskAdded(todo);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addDeadline(String input) {
        try {
            Deadline deadline = Parser.parseDeadline(input);
            tasks.addTask(deadline);
            saveTasks();

            showTaskAdded(deadline);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addEvent(String input) {
        try {
            Event event = Parser.parseEvent(input);
            tasks.addTask(event);
            saveTasks();

            showTaskAdded(event);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void deleteTask(String input) {
        try {
            int taskNumber = Parser.parseTaskNumber(input);

            if (!isValidTaskNumber(taskNumber)) {
                ui.showMessage(
                        "That task number does not exist.");
                return;
            }

            Task removedTask = tasks.deleteTask(taskNumber - 1);
            saveTasks();

            ui.showMessage("Noted. I've removed this task:");
            ui.showMessage("  " + removedTask);
            showTaskCount();
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    private void showTaskAdded(Task task) {
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        showTaskCount();
    }

    private void showTaskCount() {
        ui.showMessage(
                "Now you have " + tasks.size()
                        + " tasks in the list.");
    }

    private void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to save tasks.");
        }
    }

    private void findTasks(String input) {
        try {
            String keyword = Parser.parseFindKeyword(input);
            ArrayList<Task> matchingTasks = tasks.findTasks(keyword);

            ui.showMessage("Here are the matching tasks in your list:");

            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage(
                        " " + (i + 1) + "." + matchingTasks.get(i));
            }
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Jeff("data/duke.txt").run();
    }
}