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
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i].getStatus() + tasks[i].getDesc());
                }
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                Task task = tasks[taskNumber - 1];

                task.markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task.getStatus() + " " + task.getDesc());

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