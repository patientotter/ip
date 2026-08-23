import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Jeff {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks;

        try {
            tasks = new TaskList(Storage.loadTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to load saved tasks. Starting with an empty list.");
            tasks = new TaskList();
        }

        while (true) {
            String input = ui.readCommand();

            ui.showLine();

            if (input.equals("bye")) {
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
            } else if (input.startsWith("mark ")) {
                // marks item as done
                try {
                    int taskNumber = Integer.parseInt(input.substring(5).trim());
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("OOPS! That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.markAsDone();
                        saveTasks(tasks, ui);

                        ui.showMessage("Nice! I've marked this task as done:");
                        ui.showMessage("  " + task.getStatus() + " " + task.getDesc());
                    }
                } catch (NumberFormatException e) {
                    ui.showMessage("Please enter a valid task number.");
                }
            } else if (input.startsWith("unmark ")) {
                // marks item as undone
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.unmarkAsDone();
                        saveTasks(tasks, ui);

                        ui.showMessage("I've marked this task as undone:");
                        ui.showMessage("  " + task.getStatus() + " " + task.getDesc());
                    }
                } catch (NumberFormatException e) {
                    ui.showMessage("Please enter a valid task number.");
                }
            } else if (input.startsWith("todo")) {
                //mark as todo
                String description = input.substring(4).trim();
                if (description.isEmpty()) {
                    ui.showMessage("Missing description.");
                } else {
                    tasks.add(new Todo(description));
                    saveTasks(tasks, ui);
                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + tasks.get(tasks.size() - 1));
                    ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                }
            } else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9).trim();
                int separator = remaining.indexOf(" /by ");

                if (separator == -1) {
                    ui.showMessage("A deadline must use: deadline <description> /by <date>");
                } else {
                    String description = remaining.substring(0, separator).trim();
                    String by = remaining.substring(separator + 5).trim();

                    if (description.isEmpty()) {
                        ui.showMessage("A deadline needs a description.");
                    } else if (by.isEmpty()) {
                        ui.showMessage("A deadline needs a date.");
                    } else {
                        try {
                            LocalDate date = LocalDate.parse(by);
                            tasks.add(new Deadline(description, date));
                            saveTasks(tasks, ui);

                            ui.showMessage("Got it. I've added this task:");
                            ui.showMessage("  " + tasks.get(tasks.size() - 1));
                            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                        } catch (DateTimeParseException e) {
                            ui.showMessage("Please enter the date in YYYY-MM-DD format.");
                        }
                    }
                }
            } else if (input.startsWith("event ")) {
                String remaining = input.substring(6).trim();

                int fromSeparator = remaining.indexOf(" /from ");
                int toSeparator = remaining.indexOf(" /to ");

                if (fromSeparator == -1 || toSeparator == -1 || fromSeparator >= toSeparator) {
                    ui.showMessage("An event must use: event <description> /from <time> /to <time>");
                } else {
                    String description = remaining.substring(0, fromSeparator).trim();
                    String from = remaining.substring(fromSeparator + 7, toSeparator).trim();
                    String to = remaining.substring(toSeparator + 5).trim();

                    if (description.isEmpty()) {
                        ui.showMessage("An event needs a description.");
                    } else if (from.isEmpty()) {
                        ui.showMessage("An event needs a start time.");
                    } else if (to.isEmpty()) {
                        ui.showMessage("An event needs an end time.");
                    } else {
                        tasks.add(new Event(description, from, to));
                        saveTasks(tasks, ui);
                        ui.showMessage("Got it. I've added this task:");
                        ui.showMessage("  " + tasks.get(tasks.size() - 1));
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                    }
                }
            } else if (input.startsWith("delete ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task removedTask = tasks.delete(taskNumber - 1);
                        saveTasks(tasks, ui);
                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + removedTask);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                    }

                } catch (NumberFormatException e) {
                    ui.showMessage("Please enter a valid task number.");
                }
            } else {
                ui.showMessage("Invalid command.");
            }

            ui.showLine();
        }

        ui.close();
    }

    private static void saveTasks(TaskList tasks, Ui ui) {
        try {
            Storage.saveTasks(tasks.getAllTasks());
        } catch (IOException e) {
            ui.showMessage("Unable to save tasks.");
        }
    }
}