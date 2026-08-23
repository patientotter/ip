import java.io.IOException;

public class Jeff {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/duke.txt");
        ui.showWelcome();

        TaskList tasks;

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to load saved tasks. Starting with an empty list.");
            tasks = new TaskList();
        }

        while (true) {
            String input = ui.readCommand();
            String command = Parser.getCommandWord(input);

            ui.showLine();

            if (command.equals("bye")) {
                //says goodbye (task 0)
                ui.showGoodbye();
                ui.showLine();
                break;
            } else if (input.equals("list")) {
                //task 4
                ui.showMessage("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    ui.showMessage(" " + (i + 1) + "." + tasks.get(i));
                }
            } else if (command.equals("mark")) {
                try {
                    int taskNumber = Parser.parseTaskNumber(input);

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("OOPS! That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.markAsDone();
                        saveTasks(tasks, ui, storage);

                        ui.showMessage("Nice! I've marked this task as done:");
                        ui.showMessage("  " + task.getStatus()
                                + " " + task.getDesc());
                    }
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else if (command.equals("unmark")) {
                try {
                    int taskNumber = Parser.parseTaskNumber(input);

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.unmarkAsDone();
                        saveTasks(tasks, ui, storage);

                        ui.showMessage("I've marked this task as undone:");
                        ui.showMessage("  " + task.getStatus()
                                + " " + task.getDesc());
                    }
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else if (command.equals("todo")) {
                try {
                    Todo todo = Parser.parseTodo(input);
                    tasks.add(todo);
                    saveTasks(tasks, ui, storage);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + todo);
                    ui.showMessage("Now you have " + tasks.size()
                            + " tasks in the list.");
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else if (command.equals("deadline")) {
                try {
                    Deadline deadline = Parser.parseDeadline(input);
                    tasks.add(deadline);
                    saveTasks(tasks, ui, storage);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + deadline);
                    ui.showMessage("Now you have " + tasks.size()
                            + " tasks in the list.");
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else if (command.equals("event")) {
                try {
                    Event event = Parser.parseEvent(input);
                    tasks.add(event);
                    saveTasks(tasks, ui, storage);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + event);
                    ui.showMessage("Now you have " + tasks.size()
                            + " tasks in the list.");
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else if (command.equals("delete")) {
                try {
                    int taskNumber = Parser.parseTaskNumber(input);

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task removedTask = tasks.delete(taskNumber - 1);
                        saveTasks(tasks, ui, storage);

                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + removedTask);
                        ui.showMessage("Now you have " + tasks.size()
                                + " tasks in the list.");
                    }
                } catch (IllegalArgumentException e) {
                    ui.showMessage(e.getMessage());
                }
            } else {
                ui.showMessage("Invalid command.");
            }

            ui.showLine();
        }

        ui.close();
    }

    private static void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasks(tasks.getAllTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to save tasks.");
        }
    }
}