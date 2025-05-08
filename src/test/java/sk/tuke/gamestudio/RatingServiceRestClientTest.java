package sk.tuke.gamestudio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import sk.tuke.gamestudio.entities.Rating;
import sk.tuke.gamestudio.service.RatingServiceRestClient;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class RatingServiceRestClientTest {

    private RatingServiceRestClient ratingService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory());
        ratingService = new RatingServiceRestClient();
        ratingService.restTemplate = restTemplate;

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSetRating() {
       // Rating rating = new Rating("Nonograms", "Player1", 5, ZonedDateTime.now());
         Rating rating = new Rating();
        rating.setGame("Nonograms");
        rating.setPlayer("Player1");
        rating.setRating(5);
        rating.setRatedOn(ZonedDateTime.now());
        mockServer.expect(requestTo("http://localhost:8080/api/rating"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andRespond(withSuccess());

        ratingService.setRating(rating);
        mockServer.verify();
    }

    @Test
    public void testGetRating() {
        mockServer.expect(requestTo("http://localhost:8080/api/rating/Nonograms/Player1"))
                .andRespond(withSuccess("5", MediaType.APPLICATION_JSON));

        int rating = ratingService.getRating("Nonograms", "Player1");
        assertEquals(5, rating);

        mockServer.verify();
    }

    @Test
    public void testGetAverageRating() {
        mockServer.expect(requestTo("http://localhost:8080/api/rating/average/Nonograms"))
                .andRespond(withSuccess("4", MediaType.APPLICATION_JSON));

        int average = ratingService.getAverageRating("Nonograms");
        assertEquals(4, average);

        mockServer.verify();
    }
}
