package sk.tuke.gamestudio.entities;

import java.io.Serializable;
import java.util.Objects;

public class PuzzleProgressId implements Serializable {
    private String username;
    private String puzzleId;

    public PuzzleProgressId() {}

    public PuzzleProgressId(String username, String puzzleId) {
        this.username = username;
        this.puzzleId = puzzleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuzzleProgressId)) return false;
        PuzzleProgressId that = (PuzzleProgressId) o;
        return Objects.equals(username, that.username) && Objects.equals(puzzleId, that.puzzleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, puzzleId);
    }
}
