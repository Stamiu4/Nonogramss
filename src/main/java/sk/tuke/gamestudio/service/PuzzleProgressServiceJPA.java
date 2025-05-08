package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entities.PuzzleProgress;
import sk.tuke.gamestudio.repository.PuzzleProgressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PuzzleProgressServiceJPA implements PuzzleProgressService {

    @Autowired
    private PuzzleProgressRepository repository;

    @Override
    public void markPuzzleAsSolved(String username, String puzzleId) {
        if (!repository.existsByUsernameAndPuzzleId(username, puzzleId)) {
            repository.save(new PuzzleProgress(username, puzzleId));
        }
    }

    @Override
    public boolean isPuzzleSolved(String username, String puzzleId) {
        return repository.existsByUsernameAndPuzzleId(username, puzzleId);
    }

    @Override
    public List<String> getSolvedPuzzles(String username) {
        return repository.findAll().stream()
                .filter(p -> p.getUsername().equals(username) && p.isSolved())
                .map(PuzzleProgress::getPuzzleId)
                .collect(Collectors.toList());
    }
}
