package jeff.parser;

import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static String getCommandWord(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.isEmpty()) {
            return "";
        }

        return trimmedInput.split("\\s+", 2)[0];
    }

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

    public static Todo parseTodo(String input) {
        String description = input.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException("Missing description.");
        }

        return new Todo(description);
    }

    public static Deadline parseDeadline(String input) {
        String remaining = input.substring("deadline".length()).trim();
        int separator = remaining.indexOf(" /by ");

        if (separator == -1) {
            throw new IllegalArgumentException(
                    "A deadline must use: deadline <description> /by <date>");
        }

        String description = remaining.substring(0, separator).trim();
        String by = remaining.substring(separator + 5).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException(
                    "A deadline needs a description.");
        }

        if (by.isEmpty()) {
            throw new IllegalArgumentException(
                    "A deadline needs a date.");
        }

        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Please enter the date in YYYY-MM-DD format.");
        }
    }

    public static Event parseEvent(String input) {
        String remaining = input.substring("event".length()).trim();

        int fromSeparator = remaining.indexOf(" /from ");
        int toSeparator = remaining.indexOf(" /to ");

        if (fromSeparator == -1
                || toSeparator == -1
                || fromSeparator >= toSeparator) {
            throw new IllegalArgumentException(
                    "An event must use: "
                            + "event <description> /from <time> /to <time>");
        }

        String description =
                remaining.substring(0, fromSeparator).trim();
        String from =
                remaining.substring(fromSeparator + 7, toSeparator).trim();
        String to =
                remaining.substring(toSeparator + 5).trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs a description.");
        }

        if (from.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs a start time.");
        }

        if (to.isEmpty()) {
            throw new IllegalArgumentException(
                    "An event needs an end time.");
        }

        return new Event(description, from, to);
    }
}