package sk.tuke.gamestudio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import sk.tuke.gamestudio.entities.Score;
import sk.tuke.gamestudio.service.ScoreServiceRestClient;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class ScoreServiceRestClientTest {

    private ScoreServiceRestClient scoreService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory());
        scoreService = new ScoreServiceRestClient();
        scoreService.restTemplate = restTemplate;

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetTopScores() {
        String game = "Nonograms";
        String responseJson = "[{\"game\":\"Nonograms\",\"player\":\"Player1\",\"points\":100,\"playedOn\":\"2025-04-22T00:00:00Z\"}]";

        mockServer.expect(requestTo("http://localhost:8080/api/score/" + game))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        List<Score> scores = scoreService.getTopScores(game);
        assertNotNull(scores);
        assertEquals(1, scores.size());
        assertEquals("Player1", scores.get(0).getPlayer());

        mockServer.verify();
    }

    @Test
    public void testAddScore() {
        //Score score = new Score("Nonograms", "Player1", 100, ZonedDateTime.now());
         Score score = new Score();
         score.setGame("Nonograms");
         score.setPlayer("Player1");
         score.setScore(100);
         score.setPlayedOn(ZonedDateTime.now());
        mockServer.expect(requestTo("http://localhost:8080/api/score"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andRespond(withSuccess());

        scoreService.addScore(score);
        mockServer.verify();
    }
}
