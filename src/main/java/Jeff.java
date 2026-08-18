import java.util.Scanner;
import java.util.ArrayList;
import java.util.Scanner;

public class Jeff {
    public static void main(String[] args) {
        String banner = "     _ _____ _____ _____ \n"
                + "    | | ____|  ___|  ___|\n"
                + " _  | |  _| | |_  | |_   \n"
                + "| |_| | |___|  _| |  _|  \n"
                + " \\___/|_____|_|   |_|    \n";

        System.out.println("____________________________________________________________");
        System.out.print(banner);
        System.out.println("Hello! I'm Jeff.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            System.out.println("____________________________________________________________");

            if (input.equals("bye")) {
                //says goodbye (task 0)
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                //task 4
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
            } else if (input.startsWith("mark ")) {
                // marks item as done
                try {
                    int taskNumber = Integer.parseInt(input.substring(5).trim());
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println("OOPS! That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.markAsDone();

                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + task.getStatus() + " " + task.getDesc());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid task number.");
                }
            } else if (input.startsWith("unmark ")) {
                // marks item as undone
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        Task task = tasks.get(taskNumber - 1);
                        task.unmarkAsDone();

                        System.out.println("I've marked this task as undone:");
                        System.out.println("  " + task.getStatus() + " " + task.getDesc());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid task number.");
                }
            } else if (input.startsWith("todo")) {
                //mark as todo
                String description = input.substring(4).trim();
                if (description.isEmpty()) {
                    System.out.println("Missing description.");
                } else {
                    tasks.add(new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
            } else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9).trim();
                int separator = remaining.indexOf(" /by ");

                if (separator == -1) {
                    System.out.println("A deadline must use: deadline <description> /by <date>");
                } else {
                    String description = remaining.substring(0, separator).trim();
                    String by = remaining.substring(separator + 5).trim();

                    if (description.isEmpty()) {
                        System.out.println("A deadline needs a description.");
                    } else if (by.isEmpty()) {
                        System.out.println("A deadline needs a date.");
                    } else {
                        tasks.add(new Deadline(description, by));
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + tasks.get(tasks.size() - 1));
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    }
                }
            } else if (input.startsWith("event ")) {
                String remaining = input.substring(6).trim();

                int fromSeparator = remaining.indexOf(" /from ");
                int toSeparator = remaining.indexOf(" /to ");

                if (fromSeparator == -1 || toSeparator == -1 || fromSeparator >= toSeparator) {
                    System.out.println("An event must use: event <description> /from <time> /to <time>");
                } else {
                    String description = remaining.substring(0, fromSeparator).trim();
                    String from = remaining.substring(fromSeparator + 7, toSeparator).trim();
                    String to = remaining.substring(toSeparator + 5).trim();

                    if (description.isEmpty()) {
                        System.out.println("An event needs a description.");
                    } else if (from.isEmpty()) {
                        System.out.println("An event needs a start time.");
                    } else if (to.isEmpty()) {
                        System.out.println("An event needs an end time.");
                    } else {
                        tasks.add(new Event(description, from, to));
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + tasks.get(tasks.size() - 1));
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    }
                }
            } else if (input.startsWith("delete ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());

                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        Task removedTask = tasks.remove(taskNumber - 1);
                        System.out.println("Noted. I've removed this task:");
                        System.out.println("  " + removedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid task number.");
                }
            } else {
                System.out.println("Invalid command.");
            }

            System.out.println("____________________________________________________________");
        }

        scanner.close();
    }
}