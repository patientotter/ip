package jeff;

import jeff.parser.Parser;
import jeff.storage.Storage;
import jeff.task.*;
import jeff.ui.Ui;

import java.io.IOException;

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

        while (true) {
            String input = ui.readCommand();
            String command = Parser.getCommandWord(input);

            ui.showLine();

            if (command.equals("bye")) {
                ui.showGoodbye();
                ui.showLine();
                break;
            } else if (command.equals("list")) {
                showTaskList();
            } else if (command.equals("mark")) {
                markTask(input);
            } else if (command.equals("unmark")) {
                unmarkTask(input);
            } else if (command.equals("todo")) {
                addTodo(input);
            } else if (command.equals("deadline")) {
                addDeadline(input);
            } else if (command.equals("event")) {
                addEvent(input);
            } else if (command.equals("delete")) {
                deleteTask(input);
            } else {
                ui.showMessage("Invalid command.");
            }

            ui.showLine();
        }

        ui.close();
    }

    private void showTaskList() {
        ui.showMessage("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.get(i));
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

            Task task = tasks.get(taskNumber - 1);
            task.markAsDone();
            saveTasks();

            ui.showMessage(
                    "Nice! I've marked this task as done:");
            ui.showMessage(
                    "  " + task.getStatus()
                            + " " + task.getDesc());
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

            Task task = tasks.get(taskNumber - 1);
            task.unmarkAsDone();
            saveTasks();

            ui.showMessage(
                    "I've marked this task as undone:");
            ui.showMessage(
                    "  " + task.getStatus()
                            + " " + task.getDesc());
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addTodo(String input) {
        try {
            Todo todo = Parser.parseTodo(input);
            tasks.add(todo);
            saveTasks();

            showTaskAdded(todo);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addDeadline(String input) {
        try {
            Deadline deadline = Parser.parseDeadline(input);
            tasks.add(deadline);
            saveTasks();

            showTaskAdded(deadline);
        } catch (IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void addEvent(String input) {
        try {
            Event event = Parser.parseEvent(input);
            tasks.add(event);
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

            Task removedTask = tasks.delete(taskNumber - 1);
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
            storage.saveTasks(tasks.getAllTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to save tasks.");
        }
    }

    public static void main(String[] args) {
        new Jeff("data/duke.txt").run();
    }
}