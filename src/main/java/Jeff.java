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

        Task[] tasks = new Task[100];
        int count = 0;
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
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                //marks item as done (task 3)
                int taskNumber = Integer.parseInt(input.substring(5));
                Task task = tasks[taskNumber - 1];
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task.getStatus() + " " + task.getDesc());

            } else if (input.startsWith("unmark ")) {
                //marks item as undone (task 3)
                int taskNumber = Integer.parseInt(input.substring(7));
                Task task = tasks[taskNumber - 1];
                task.unmarkAsDone();
                System.out.println("I've marked this task as undone:");
                System.out.println("  " + task.getStatus() + " " + task.getDesc());

            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);

                tasks[count] = new Todo(description);
                count++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9);

                int separator = remaining.indexOf(" /by ");

                String description = remaining.substring(0, separator);
                String by = remaining.substring(separator + 5);

                tasks[count] = new Deadline(description, by);
                count++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String remaining = input.substring(6);

                int fromSeparator = remaining.indexOf(" /from ");
                int toSeparator = remaining.indexOf(" /to ");

                String description = remaining.substring(0, fromSeparator);
                String from = remaining.substring(fromSeparator + 7, toSeparator);
                String to = remaining.substring(toSeparator + 4);

                tasks[count] = new Event(description, from, to);
                count++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else {
                tasks[count] = new Task(input);
                count++;
                System.out.println(" added: " + input);
            }

            System.out.println("____________________________________________________________");
        }

        scanner.close();
    }
}