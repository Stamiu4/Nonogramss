package sk.tuke.gamestudio;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entities.Rating;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.Date;

public class TestRating {
    @Test
    public void testRatingGettersAndSetters() {
        Date now = new Date();
        //Rating rating = new Rating("Nonogram", "Alice", 4, now);
        Rating rating = new Rating();
        rating.setGame("Nonograms");
        rating.setPlayer("Alice");
        rating.setRating(4);
        rating.setRatedOn(ZonedDateTime.now());

        assertEquals("Nonograms", rating.getGame());
        assertEquals("Artem", rating.getPlayer());
        assertEquals(4, rating.getRating());
        assertEquals(now, rating.getRatedOn());


        Date later = new Date(now.getTime() + 1000);
        rating.setGame("Puzzle");
        rating.setPlayer("Nikita");
        rating.setRating(5);
        rating.setRatedOn(ZonedDateTime.now());

        assertEquals("Puzzle", rating.getGame());
        assertEquals("Nikita", rating.getPlayer());
        assertEquals(5, rating.getRating());
        assertEquals(later, rating.getRatedOn());
    }

    @Test
    public void testRatingToString() {
        Date now = new Date();
        //Rating rating = new Rating("Nonogram", "Artem", 3, now);
        Rating rating = new Rating();
        rating.setGame("Nonograms");
        rating.setPlayer("Artem");
        rating.setRating(3);
        rating.setRatedOn(ZonedDateTime.now());
        String str = rating.toString();
        assertTrue(str.contains("Nonograms"));
        assertTrue(str.contains("Artem"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains(now.toString()));
    }
}