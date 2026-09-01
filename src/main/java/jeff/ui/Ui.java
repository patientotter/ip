package jeff.ui;

import java.util.Scanner;

/**
 * Handles interactions between Jeff and the user.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;
    private final StringBuilder capturedOutput;
    private boolean isCapturing;

    /**
     * Creates a console user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
        capturedOutput = new StringBuilder();
    }

    /**
     * Shows the application's welcome message.
     */
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

    /**
     * Returns the next command entered by the user.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows or captures the specified message.
     *
     * @param message Message to show.
     */
    public void showMessage(String message) {
        if (isCapturing) {
            if (capturedOutput.length() > 0) {
                capturedOutput.append(System.lineSeparator());
            }

            capturedOutput.append(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * Starts collecting displayed messages instead of printing them.
     */
    public void startCapturing() {
        capturedOutput.setLength(0);
        isCapturing = true;
    }

    /**
     * Stops collecting messages and returns the collected output.
     *
     * @return Collected messages.
     */
    public String stopCapturing() {
        isCapturing = false;
        return capturedOutput.toString();
    }

    /**
     * Shows a separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows the application's farewell message.
     */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}