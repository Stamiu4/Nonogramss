package sk.tuke.gamestudio.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "puzzle_progress")
@IdClass(PuzzleProgressId.class)
public class PuzzleProgress {


    @Id
    private String username;

    @Id
    private String puzzleId;

    private boolean solved;

    public PuzzleProgress() {}

    public PuzzleProgress(String username, String puzzleId) {
        this.username = username;
        this.puzzleId = puzzleId;
        this.solved = true;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPuzzleId() { return puzzleId; }
    public void setPuzzleId(String puzzleId) { this.puzzleId = puzzleId; }

    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
}
