package jeff;

import java.io.IOException;
import java.util.ArrayList;

import jeff.parser.Parser;
import jeff.storage.Storage;
import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.TaskList;
import jeff.task.Todo;
import jeff.ui.Ui;

/**
 * Runs the Jeff chatbot and coordinates its main components.
 */
public class Jeff {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Jeff using the specified task-storage file.
     *
     * @param filePath Path of the task-storage file.
     */
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

    /**
     * Runs the command-processing loop until the user exits the application.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();

            ui.showLine();
            isExit = processCommand(input);
            ui.showLine();
        }

        ui.close();
    }

    /**
     * Returns Jeff's response to a command for use by a graphical interface.
     *
     * @param input User command.
     * @return Jeff's response.
     */
    public String getResponse(String input) {
        ui.startCapturing();
        processCommand(input);
        return ui.stopCapturing();
    }

    /**
     * Processes one user command.
     *
     * @param input User command.
     * @return True if the user requested to exit.
     */
    private boolean processCommand(String input) {
        String command = Parser.getCommandWord(input);

        switch (command) {
            case "bye":
                ui.showGoodbye();
                return true;
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

        return false;
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
            addTask(todo);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addDeadline(String input) {
        try {
            Deadline deadline = Parser.parseDeadline(input);
            addTask(deadline);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addEvent(String input) {
        try {
            Event event = Parser.parseEvent(input);
            addTask(event);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addTask(Task task) {
        tasks.addTask(task);
        saveTasks();
        showTaskAdded(task);
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

    /**
     * Starts the Jeff application.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Jeff("data/duke.txt").run();
    }
}
