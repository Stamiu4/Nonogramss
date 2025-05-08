package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entities.Comment;


import java.sql.*;

public class CommentJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "200601";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Если драйвер не найден, логируем ошибку
            e.printStackTrace();
        }
    }

    public void addComment(Comment comment) {
        // Обновлённый SQL-запрос, теперь он вставляет значения в столбец "commented_on"
        String sql = "INSERT INTO public.comment (player, game, comment, commented_on) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comment.getPlayer());
            ps.setString(2, comment.getGame());
            ps.setString(3, comment.getComment());

            // Если значение commentedOn ещё не установлено (т.е. равно null), можно установить его здесь
            if (comment.getCommentedOn() == null) {
                // Присваиваем текущее время, если поле равно null
                comment.setCommentedOn(java.time.LocalDateTime.now());
            }
            // Преобразуем LocalDateTime в java.sql.Timestamp
            ps.setTimestamp(4, Timestamp.valueOf(comment.getCommentedOn()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Error adding comment to DB");
        }
    }
}
