package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gameclassses.SolverN;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestSolverN {

    @Test
    public void testSolverSimplePuzzle() throws IOException {

        String content = "1,1\n#,1\n#,1";
        Path tempFile = Files.createTempFile("dino", ".txt");
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(content);
        writer.close();

        SolverN solver = new SolverN();
        solver.solveTaskFromFile(tempFile.toString());
        String solution = solver.getFirstSolution();
        assertNotNull(solution, "Solution should not be null");

        assertTrue(solution.contains("#"), "Solution should contain '#'");

        Files.delete(tempFile);
    }
}
