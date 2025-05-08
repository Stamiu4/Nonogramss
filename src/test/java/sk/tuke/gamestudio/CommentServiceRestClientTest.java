package sk.tuke.gamestudio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.client.support.HttpAccessor;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entities.Comment;
import sk.tuke.gamestudio.service.CommentServiceRestClient;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class CommentServiceRestClientTest {

    private CommentServiceRestClient client;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        client = new CommentServiceRestClient();
        client.restTemplate = restTemplate;
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        comment.setGame("Nonograms");
        comment.setPlayer("TestPlayer");
        comment.setComment("Great!");
        comment.setCommentedOn(LocalDateTime.now());

        mockServer.expect(requestTo("http://localhost:8080/api/comment"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        client.addComment(comment);
        mockServer.verify();
    }

    @Test
    public void testGetComments() {
        String json = """
                [{
                    "game":"Nonograms",
                    "player":"TestPlayer",
                    "comment":"Awesome!",
                    "commentedOn":"2025-04-23T00:00:00Z"
                }]
                """;

        mockServer.expect(requestTo("http://localhost:8080/api/comment/Nonograms"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<Comment> comments = client.getComments("Nonograms");
        assertEquals(1, comments.size());
        assertEquals("TestPlayer", comments.get(0).getPlayer());
    }
}
