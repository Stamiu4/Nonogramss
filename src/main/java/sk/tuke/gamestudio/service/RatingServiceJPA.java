package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sk.tuke.gamestudio.entities.Rating;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public void setRating(Rating newRating) {
        Rating existingRating = entityManager.createQuery(
                        "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player",
                        Rating.class
                )
                .setParameter("game", newRating.getGame())
                .setParameter("player", newRating.getPlayer())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existingRating != null) {
            existingRating.setRating(newRating.getRating());
            existingRating.setRatedOn(ZonedDateTime.now());

        } else {
            if (newRating.getRatedOn() == null) {
                newRating.setRatedOn(ZonedDateTime.now());
            }
            entityManager.persist(newRating);
        }
    }


    public void reset() {
        entityManager.createQuery("DELETE FROM Rating").executeUpdate();
    }

    @Override
    public int getAverageRating(String game) {
        Double avg = entityManager.createQuery(
                        "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game",
                        Double.class
                )
                .setParameter("game", game)
                .getSingleResult();
        return avg != null ? (int) Math.round(avg) : 0;
    }

    @Override
    public int getRating(String game, String player) {
        return entityManager.createQuery(
                        "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player",
                        Integer.class
                )
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultStream()
                .findFirst()
                .orElse(-1);
    }
}
