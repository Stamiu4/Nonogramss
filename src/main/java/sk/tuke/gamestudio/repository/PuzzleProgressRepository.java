package sk.tuke.gamestudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio.entities.PuzzleProgress;
import sk.tuke.gamestudio.entities.PuzzleProgressId;

public interface PuzzleProgressRepository extends JpaRepository<PuzzleProgress, PuzzleProgressId> {
    boolean existsByUsernameAndPuzzleId(String username, String puzzleId);
}
