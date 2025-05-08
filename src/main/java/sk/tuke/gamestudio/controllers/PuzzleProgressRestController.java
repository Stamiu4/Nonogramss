package sk.tuke.gamestudio.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entities.User;
import sk.tuke.gamestudio.service.PuzzleProgressService;

import java.util.Map;

@RestController
@RequestMapping("/progress")
public class PuzzleProgressRestController {

    @Autowired
    private PuzzleProgressService puzzleProgressService;

    @PostMapping("/save")
    public void saveProgress(@RequestBody Map<String, String> data,
                             @SessionAttribute(value = "user", required = false) User user) {
        if (user != null && data.containsKey("puzzleId")) {
            String puzzleId = data.get("puzzleId");
            puzzleProgressService.markPuzzleAsSolved(user.getUsername(), puzzleId);
        }
    }

    @GetMapping("/solved")
    public boolean isSolved(@RequestParam String puzzleId,
                            @SessionAttribute(value = "user", required = false) User user) {
        if (user == null) return false;
        return puzzleProgressService.isPuzzleSolved(user.getUsername(), puzzleId);
    }
}