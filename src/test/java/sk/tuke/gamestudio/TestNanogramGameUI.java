package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.UI.NanogramGameUI;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

public class TestNanogramGameUI {

    @Test
    public void testUIInitialization() {

        boolean[][] solution = { {true, false}, {false, true} };
        String[] rowHints = {"1", "1"};
        String[] colHints = {"1", "1"};

        SwingUtilities.invokeLater(() -> {
            NanogramGameUI gameUI = new NanogramGameUI(solution, rowHints, colHints);
            assertEquals("Nonogram", gameUI.getTitle());

            assertNotNull(gameUI.getContentPane());

            gameUI.dispose();
        });
    }
}

