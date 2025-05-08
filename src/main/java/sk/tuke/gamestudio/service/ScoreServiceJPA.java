package sk.tuke.gamestudio.service;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entities.Score;

import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public void addScore(Score newScore) {
        // Ищем существующий счёт игрока
        Score existingScore = entityManager.createQuery("SELECT r FROM Score r WHERE r.game = :game AND r.player = :player", Score.class)
                .setParameter("game", newScore.getGame())
                .setParameter("player", newScore.getPlayer())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (existingScore != null) {
            // Суммируем очки, обновляем дату
            existingScore.setScore(existingScore.getScore() + newScore.getScore());
            existingScore.setPlayedOn(newScore.getPlayedOn());
            entityManager.merge(existingScore);
        } else {
            // Если нет предыдущей записи — создаём новую
            entityManager.persist(newScore);
        }
    }


    @Override
    public List<Score> getTopScores(String game) {
        return entityManager.createQuery("SELECT s FROM Score s WHERE s.game = :game ORDER BY s.score DESC", Score.class)
                .setParameter("game", game)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("delete from score").executeUpdate();
    }
}