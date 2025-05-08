package sk.tuke.gamestudio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sk.tuke.gamestudio.entities.Score;
import sk.tuke.gamestudio.service.ScoreServiceJPA;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ScoreJPATest
{
    @Autowired
    private EntityManager em;

    @Test
    public void testAddAndGetTopScores() {

        ScoreServiceJPA scoreService = new ScoreServiceJPA();
        scoreService.entityManager = em;


        //Score score1 = new Score("Player1", "Nonogram", 150, new Date());
       // Score score2 = new Score("Player2", "Nonogram", 200, new Date());
        Score score1 = new Score();
        Score score2 = new Score();
        score1.setGame("Nongrams");
        score2.setGame("Nongrams");
        score1.setPlayer("Player1");
        score2.setPlayer("Player2");
        score1.setScore(150);
        score2.setScore(200);
        score1.setPlayedOn(ZonedDateTime.now());
        score2.setPlayedOn(ZonedDateTime.now());


        scoreService.addScore(score1);
        scoreService.addScore(score2);


        em.flush();
        em.clear();


        List<Score> topScores = scoreService.getTopScores("Nongrams");

        assertNotNull(topScores, "List of scores should not be null");
        assertEquals(2, topScores.size(), "Waiting for 2 scores to be added");

        assertTrue(topScores.get(0).getScore() >= topScores.get(1).getScore(),
                "Scores must be sorted in ascending order");
    }

    @Test
    public void testResetScores() {
        ScoreServiceJPA scoreService = new ScoreServiceJPA();
        scoreService.entityManager = em;


       // Score score = new Score("Player1", "Nonogram", 150, new Date());
        Score score = new Score();
        score.setGame("Nongrams");
        score.setPlayer("Player1");
        score.setScore(150);
        score.setPlayedOn(ZonedDateTime.now());
        scoreService.addScore(score);
        em.flush();


        scoreService.reset();
        em.flush();
        em.clear();


        List<Score> topScores = scoreService.getTopScores("Nongrams");
        assertNotNull(topScores, "List should be null after reset");
        assertTrue(topScores.isEmpty(), "After reset, list should be empty");
    }
}
