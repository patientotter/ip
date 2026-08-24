package jeff.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}