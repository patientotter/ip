package jeff.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import jeff.task.Deadline;

public class ParserTest {

    @Test
    public void parseDeadline_validInput_returnsDeadline() {
        Deadline deadline = Parser.parseDeadline(
                "deadline return book /by 2026-09-15");

        assertEquals("return book", deadline.getDescription());
        assertEquals(
                LocalDate.of(2026, 9, 15),
                deadline.getDueDate());
    }

    @Test
    public void parseDeadline_missingSeparator_exceptionThrown() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Parser.parseDeadline(
                                "deadline return book 2026-09-15"));

        assertEquals(
                "A deadline must use: "
                        + "deadline <description> /by <date>",
                exception.getMessage());
    }

    @Test
    public void parseDeadline_missingDescription_exceptionThrown() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Parser.parseDeadline(
                                "deadline /by 2026-09-15"));

        assertEquals(
                "A deadline needs a description.",
                exception.getMessage());
    }

    @Test
    public void parseDeadline_missingDate_exceptionThrown() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Parser.parseDeadline(
                                "deadline return book /by "));

        assertEquals(
                "A deadline needs a date.",
                exception.getMessage());
    }

    @Test
    public void parseDeadline_invalidDate_exceptionThrown() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Parser.parseDeadline(
                                "deadline return book /by 2026-20-50"));

        assertEquals(
                "Please enter the date in YYYY-MM-DD format.",
                exception.getMessage());
    }

    @Test
    public void parseFindKeyword_validInput_returnsKeyword() {
        assertEquals("book", Parser.parseFindKeyword("find book"));
    }

    @Test
    public void parseFindKeyword_missingKeyword_exceptionThrown() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseFindKeyword("find"));

        assertEquals(
                "A find command needs a keyword.",
                exception.getMessage());
    }
}