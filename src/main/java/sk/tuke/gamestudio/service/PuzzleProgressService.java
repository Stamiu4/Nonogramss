package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entities.PuzzleProgress;

import java.util.List;

public interface PuzzleProgressService {
    void markPuzzleAsSolved(String username, String puzzleName);
    boolean isPuzzleSolved(String username, String puzzleName);
    List<String> getSolvedPuzzles(String username);
}