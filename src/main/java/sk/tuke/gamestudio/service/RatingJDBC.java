package sk.tuke.gamestudio.service;
import sk.tuke.gamestudio.entities.Rating;

import java.sql.*;
import java.time.ZonedDateTime;

public class RatingJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "200601";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
    }

    public void addRating(Rating rating) {
        String sql = "INSERT INTO public.rating (player, game, rating, rated_on) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rating.getPlayer());
            ps.setString(2, rating.getGame());
            ps.setInt(3, rating.getRating());
            if (rating.getRatedOn() == null) {
                rating.setRatedOn(ZonedDateTime.now());
            }
            ps.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Error adding Rating to Database");
        }
    }

}