package jeff.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Todo;

/**
 * Parses user input into commands and task information.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Returns the command word from the specified user input.
     *
     * @param input Full user input.
     * @return Command word, or an empty string if the input is empty.
     */
    public static String getCommandWord(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.isEmpty()) {
            return "";
        }

        return trimmedInput.split("\\s+", 2)[0];
    }

    /**
     * Returns the task number from the specified user input.
     *
     * @param input Full user input.
     * @return One-based task number supplied by the user.
     * @throws IllegalArgumentException If a valid task number was not supplied.
     */
    public static int parseTaskNumber(String input) {
        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Please enter a task number.");
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid task number.");
        }
    }

    /**
     * Returns a todo parsed from the specified user input.
     *
     * @param input Full user input.
     * @return Parsed todo.
     * @throws IllegalArgumentException If the todo description is missing.
     */
    public static Todo parseTodo(String input) {
        String description = input.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException("Missing description.");
        }

        return new Todo(description);
    }

    /**
     * Returns a deadline parsed from the specified user input.
     *
     * @param input Full user input.
     * @return Parsed deadline.
     * @throws IllegalArgumentException If the deadline details are invalid.
     */
    public static Deadline parseDeadline(String input) {
        String deadlineDetails = input.substring("deadline".length()).trim();
        int bySeparatorIndex = deadlineDetails.indexOf("/by");

        if (bySeparatorIndex == -1) {
            throw new IllegalArgumentException(
                    "A deadline must use: deadline <description> /by <date>");
        }

        String description = deadlineDetails.substring(0, bySeparatorIndex).trim();
        String dueDateText = deadlineDetails.substring(bySeparatorIndex + 3).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException(
                    "A deadline needs a description.");
        }

        if (dueDateText.isEmpty()) {
            throw new IllegalArgumentException(
                    "A deadline needs a date.");
        }

        try {
            return new Deadline(description, LocalDate.parse(dueDateText));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Please enter the date in YYYY-MM-DD format.");
        }
    }

    /**
     * Returns an event parsed from the specified user input.
     *
     * @param input Full user input.
     * @return Parsed event.
     * @throws IllegalArgumentException If the event details are invalid.
     */
    public static Event parseEvent(String input) {
        String eventDetails = input.substring("event".length()).trim();

        int fromSeparatorIndex = eventDetails.indexOf(" /from ");
        int toSeparatorIndex = eventDetails.indexOf(" /to ");

        if (fromSeparatorIndex == -1
                || toSeparatorIndex == -1
                || fromSeparatorIndex >= toSeparatorIndex) {
            throw new IllegalArgumentException(
                    "An event must use: "
                            + "event <description> /from <time> /to <time>");
        }

        String description =
                eventDetails.substring(0, fromSeparatorIndex).trim();
        String startTime =
                eventDetails.substring(fromSeparatorIndex + 7, toSeparatorIndex).trim();
        String endTime =
                eventDetails.substring(toSeparatorIndex + 5).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs a description.");
        }

        if (startTime.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs a start time.");
        }

        if (endTime.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs an end time.");
        }

        return new Event(description, startTime, endTime);
    }

    /**
     * Extracts the keyword from a find command.
     *
     * @param input Full user input.
     * @return Keyword to search for.
     * @throws IllegalArgumentException If no keyword was provided.
     */
    public static String parseFindKeyword(String input) {
        String keyword = input.substring("find".length()).trim();

        if (keyword.isEmpty()) {
            throw new IllegalArgumentException(
                    "A find command needs a keyword.");
        }

        return keyword;
    }
}
