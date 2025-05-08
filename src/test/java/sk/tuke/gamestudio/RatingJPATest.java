package sk.tuke.gamestudio;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sk.tuke.gamestudio.entities.Rating;
import sk.tuke.gamestudio.service.RatingServiceJPA;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RatingJPATest {
    @Autowired
    private EntityManager em;

    @Test
    public void testAddAndGetRatings() {
        RatingServiceJPA ratingService = new RatingServiceJPA();
        ratingService.entityManager = em;

//        Rating rating1 = new Rating("Nonogram", "Player1", 5, new Date());
//        Rating rating2 = new Rating("Nonogram", "Player2", 4, new Date());
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        rating1.setGame("Nongrams");
        rating2.setGame("Nongrams");
        rating1.setPlayer("Player1");
        rating2.setPlayer("Player2");
        rating1.setRating(5);
        rating2.setRating(4);
        rating1.setRatedOn(ZonedDateTime.now());
        rating2.setRatedOn(ZonedDateTime.now());

        ratingService.setRating(rating1);
        ratingService.setRating(rating2);

        em.flush();
        em.clear();

        int ratingFromPlayer1 = ratingService.getRating("Nongrams", "Player1");
        int ratingFromPlayer2 = ratingService.getRating("Nongrams", "Player2");
        int averageRating = ratingService.getAverageRating("Nongrams");

        assertEquals(5, ratingFromPlayer1);
        assertEquals(4, ratingFromPlayer2);
        assertEquals(5 - (5 - 4) / 2.0, averageRating, 1); // приближённое сравнение, если что
    }

    @Test
    public void testResetRatings() {
        RatingServiceJPA ratingService = new RatingServiceJPA();
        ratingService.entityManager = em;

        //Rating rating = new Rating("Nonogram", "Player1", 5, new Date());
        Rating rating = new Rating();
        rating.setGame("Nongrams");
        rating.setPlayer("Player1");
        rating.setRating(5);
        rating.setRatedOn(ZonedDateTime.now());
        ratingService.setRating(rating);
        em.flush();

        ratingService.reset();
        em.flush();
        em.clear();

        int ratingValue = ratingService.getRating("Nongrams", "Player1");
        int avgRating = ratingService.getAverageRating("Nongrams");

        assertEquals(-1, ratingValue, "Expected no rating for player after reset");
        assertEquals(0, avgRating, "Expected average rating to be 0 after reset");
    }
}
