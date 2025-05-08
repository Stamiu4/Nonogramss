package sk.tuke.gamestudio;



import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.Server.GameStudioServer;
import sk.tuke.gamestudio.entities.Comment;
import sk.tuke.gamestudio.service.CommentServiceJPA;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

public class CommentJPATest {

    @Autowired
    private EntityManager em;

    @Test
    public void testAddAndGetComments() {
        CommentServiceJPA commentService = new CommentServiceJPA();
        commentService.entityManager = em;

        // Используем LocalDateTime.now() для установки текущего времени
        //Comment comment1 = new Comment("Nonogram", "Player1", "Great game!", LocalDateTime.now());
        //Comment comment2 = new Comment("Nonogram", "Player2", "Really enjoyed it.", LocalDateTime.now());
         Comment comment1 = new Comment();
         comment1.setGame("Nonograms");
         comment1.setPlayer("Player1");
         comment1.setComment("Great game!");
         comment1.setCommentedOn(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setGame("Nonograms");
        comment2.setPlayer("Player2");
        comment2.setComment("Really enjoyed it.");
        comment2.setCommentedOn(LocalDateTime.now());

        commentService.addComment(comment1);
        commentService.addComment(comment2);

        em.flush();
        em.clear();

        List<Comment> comments = commentService.getComments("Nonograms");
        assertNotNull(comments, "List of comments shouldn't be null");
        assertEquals(2, comments.size(), "Expected 2 comments");

        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("Player1") && c.getComment().contains("Great")),
                "Comment from Player1 not found");
        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("Player2") && c.getComment().contains("enjoyed")),
                "Comment from Player2 not found");
    }

    @Test
    public void testReset() {
        CommentServiceJPA commentService = new CommentServiceJPA();
        commentService.entityManager = em;

        //Comment comment1 = new Comment("Nonogram", "Player1", "Awesome!", LocalDateTime.now());
        Comment comment1 = new Comment();
        comment1.setGame("Nonograms");
        comment1.setPlayer("Player1");
        comment1.setComment("Great game!");
        comment1.setCommentedOn(LocalDateTime.now());
        commentService.addComment(comment1);
        em.flush();

        commentService.reset();
        em.flush();
        em.clear();

        List<Comment> comments = commentService.getComments("Nonograms");
        assertNotNull(comments);
        assertTrue(comments.isEmpty(), "No comments should be present after reset");
    }
}
