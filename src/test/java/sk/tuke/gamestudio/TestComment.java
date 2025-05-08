package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entities.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestComment {

    @Test
    public void testCommentGettersAndSetters() {
        // Инициализируем текущее время в виде LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Создаем объект Comment с начальными значениями
        //Comment comment = new Comment("Nonogram", "Alice", "Great game!", now);
        Comment comment = new Comment();
        comment.setGame("Nonogramss");
        comment.setPlayer("Alice");
        comment.setComment("Great game!");
        comment.setCommentedOn(LocalDateTime.now());

        // Проверяем начальные значения
        assertEquals("Nonogramss", comment.getGame());
        assertEquals("Alice", comment.getPlayer());
        assertEquals("Great game!", comment.getComment());
        assertEquals(now, comment.getCommentedOn());

        // Зададим новые значения и новое время
        LocalDateTime later = now.plusSeconds(3);
        comment.setGame("Puzzle");
        comment.setPlayer("Nikita");
        comment.setComment("Not bad!");
        comment.setCommentedOn(later);

        // Проверяем, что новые значения установлены правильно
        assertEquals("Puzzle", comment.getGame());
        assertEquals("Nikita", comment.getPlayer());
        assertEquals("Not bad!", comment.getComment());
        assertEquals(later, comment.getCommentedOn());
    }

    @Test
    public void testCommentToString() {
        LocalDateTime now = LocalDateTime.now();
       // Comment comment = new Comment("Nonogram", "Artem", "Awesome puzzle", now);
        Comment comment = new Comment();
        comment.setGame("Nonogramss");
        comment.setPlayer("Artem");
        comment.setComment("Awesome puzzle");
        comment.setCommentedOn(now);
        String str = comment.toString();
        // Проверяем, что строковое представление содержит ключевые слова
        assertTrue(str.contains("Nonogramss"));
        assertTrue(str.contains("Artem"));
        assertTrue(str.contains("Awesome puzzle"));
        assertTrue(str.contains(now.toString()));
    }
}