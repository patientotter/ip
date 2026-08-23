import java.util.Scanner;

public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String banner = "     _ _____ _____ _____ \n"
                + "    | | ____|  ___|  ___|\n"
                + " _  | |  _| | |_  | |_   \n"
                + "| |_| | |___|  _| |  _|  \n"
                + " \\___/|_____|_|   |_|    \n";

        showLine();
        System.out.print(banner);
        showMessage("Hello! I'm Jeff.");
        showMessage("What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    public void close() {
        scanner.close();
    }
}