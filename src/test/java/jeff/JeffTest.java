package jeff;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class JeffTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    public void updateCommand_validUpdate_preservesStatusAndPersistsChange() {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        Jeff jeff = new Jeff(filePath.toString());
        jeff.getResponse(
                "event project meeting /from 1400 /to 1600");
        jeff.getResponse("mark 1");

        String updateResponse =
                jeff.getResponse("update 1 /to 1800");

        assertTrue(
                updateResponse.contains(
                        "I've updated this task:"));
        assertTrue(updateResponse.contains("[E][X]"));
        assertTrue(
                updateResponse.contains(
                        "from: 1400 to: 1800"));

        Jeff reloadedJeff = new Jeff(filePath.toString());
        String listResponse =
                reloadedJeff.getResponse("list");

        assertTrue(listResponse.contains("[E][X]"));
        assertTrue(
                listResponse.contains(
                        "from: 1400 to: 1800"));
    }
}
