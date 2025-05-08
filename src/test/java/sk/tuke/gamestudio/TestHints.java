package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gameclassses.Hints;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestHints {

    @Test
    public void testReadFromFile() throws IOException {

        String content = "2,3\n#,1\n#,2\n#,3\n#,4\n#,5";
        Path tempFile = Files.createTempFile("dino", ".txt");
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(content);
        writer.close();


        Hints hints = Hints.readFromFile(tempFile.toString());
        assertEquals(2, hints.getNumRows());
        assertEquals(3, hints.getNumCols());

        assertEquals("1", hints.getRowHints()[0]);

        assertEquals("2", hints.getRowHints()[1]);


        assertEquals("3", hints.getColHints()[0]);
        assertEquals("4", hints.getColHints()[1]);
        assertEquals("5", hints.getColHints()[2]);

        Files.delete(tempFile);
    }
}