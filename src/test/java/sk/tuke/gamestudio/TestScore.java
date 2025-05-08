package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entities.Score;


import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.Date;

public class TestScore {
    @Test
    public void testScoreGettersAndSetters() {
        Date now = new Date();
        //Score score = new Score("Stamina", "Nonogram", 1050, now);
        Score score = new Score();
        score.setGame("Nonograms");
        score.setPlayer("Stamina");
        score.setScore(1050);
        score.setPlayedOn(ZonedDateTime.now());

        assertEquals("Nonograms", score.getGame());
        assertEquals("Stamina", score.getPlayer());
        assertEquals(100, score.getScore());
        assertEquals(now, score.getPlayedOn());


        Date later = new Date(now.getTime() + 2000);
        score.setGame("Puzzle");
        score.setPlayer("Artem");
        score.setScore(200);
        score.setPlayedOn(ZonedDateTime.now());

        assertEquals("Puzzle", score.getGame());
        assertEquals("Artem", score.getPlayer());
        assertEquals(200, score.getScore());
        assertEquals(ZonedDateTime.now(), score.getPlayedOn());
    }

    @Test
    public void testScoreToString() {
        Date now = new Date();
        //Score score =new Score("Stamina", "Nonogram", 150, now);
        Score score = new Score();
        score.setGame("Nonograms");
        score.setPlayer("Stamina");
        score.setScore(150);
        score.setPlayedOn(ZonedDateTime.now());
        String str = score.toString();
        assertTrue(str.contains("Nonograms"));
        assertTrue(str.contains("Artem"));
        assertTrue(str.contains("150"));
        assertTrue(str.contains(now.toString()));
    }
}