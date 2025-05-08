package sk.tuke.gamestudio.controllers.gamewebcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.UI.Game;
import sk.tuke.gamestudio.entities.*;
import sk.tuke.gamestudio.gameclassses.Hints;
import sk.tuke.gamestudio.gameclassses.SolverN;
import sk.tuke.gamestudio.model.Puzzle;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.PuzzleProgressService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("user")
public class PlayController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PuzzleProgressService puzzleProgressService;

    @ModelAttribute("user")
    public User user() {
        return null;
    }

    // 📌 Добавляем уровни сложности
    private final List<Puzzle> availablePuzzles = List.of(
            new Puzzle("Heart", "Easy"),
            new Puzzle("House", "Easy"),
            new Puzzle("Dino", "Medium"),
            new Puzzle("Flower", "Medium"),
            new Puzzle("Pumpkin", "Medium"),
            new Puzzle("Coffe", "Medium"),
            new Puzzle("Psik", "Hard"),
            new Puzzle("Medved", "Hard"),
            new Puzzle("Panda", "Hard"),
            new Puzzle("Paw", "Hard")
    );

    @GetMapping("/")
    public String index(Model model, @SessionAttribute(value = "user", required = false) User user) {
        model.addAttribute("message", "Select puzzle");
        model.addAttribute("topScores", scoreService.getTopScores("nonogram"));
        model.addAttribute("comments", commentService.getComments("nonogram"));
        model.addAttribute("averageRating", ratingService.getAverageRating("nonogram"));
        model.addAttribute("availablePuzzles", availablePuzzles);
        model.addAttribute("difficultyLevels", List.of("Easy", "Medium", "Hard"));

        List<String> solved = (user != null)
                ? puzzleProgressService.getSolvedPuzzles(user.getUsername())
                : List.of();
        model.addAttribute("solvedPuzzles", solved);

        long remainingEasy = availablePuzzles.stream()
                .filter(p -> p.getDifficulty().equals("Easy") && !solved.contains(p.getName()))
                .count();
        long remainingMedium = availablePuzzles.stream()
                .filter(p -> p.getDifficulty().equals("Medium") && !solved.contains(p.getName()))
                .count();
        long remainingHard = availablePuzzles.stream()
                .filter(p -> p.getDifficulty().equals("Hard") && !solved.contains(p.getName()))
                .count();

        model.addAttribute("remainingEasy", remainingEasy);
        model.addAttribute("remainingMedium", remainingMedium);
        model.addAttribute("remainingHard", remainingHard);

        return "menuu";
    }



    @GetMapping("/play/{puzzle}")
    public String playPuzzle(@PathVariable("puzzle") String puzzleId, Model model,
                             @SessionAttribute(value = "user", required = false) User user) {
        try {
            File file = new File("src/main/resources/puzzles/" + puzzleId + ".txt");
            byte[] data = new FileInputStream(file).readAllBytes();
            ByteArrayInputStream stream1 = new ByteArrayInputStream(data);
            ByteArrayInputStream stream2 = new ByteArrayInputStream(data);

            Hints hints = Hints.readFromInputStream(stream1);
            SolverN solver = new SolverN();
            solver.solveTaskFromInputStream(stream2);
            String solution = solver.getFirstSolution();

            if (solution == null) {
                model.addAttribute("error", "Solution not found");
                return "error";
            }

            model.addAttribute("hintPart1", hints.getRowHints());
            model.addAttribute("hintPart2", hints.getColHints());
            model.addAttribute("puzzleId", puzzleId);
            model.addAttribute("topScores", scoreService.getTopScores("nonogram"));
            model.addAttribute("comments", commentService.getComments("nonogram"));
            model.addAttribute("averageRating", ratingService.getAverageRating("nonogram"));
            model.addAttribute("availablePuzzles", availablePuzzles);
            model.addAttribute("user", user);

            return "game";

        } catch (IOException e) {
            model.addAttribute("error", "Failed to load puzzle: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/play/{puzzle}/check")
    @ResponseBody
    public Map<String, Object> checkPuzzle(@PathVariable("puzzle") String puzzleId,
                                           @RequestBody boolean[][] selectedMatrix,
                                           @SessionAttribute(value = "user", required = false) User user) {
        try {
            File file = new File("src/main/resources/puzzles/" + puzzleId + ".txt");
            byte[] data = new FileInputStream(file).readAllBytes();
            ByteArrayInputStream bis1 = new ByteArrayInputStream(data);
            ByteArrayInputStream bis2 = new ByteArrayInputStream(data);

            Hints hints = Hints.readFromInputStream(bis1);
            SolverN solver = new SolverN();
            solver.solveTaskFromInputStream(bis2);
            String solutionStr = solver.getFirstSolution();

            if (solutionStr == null)
                return Map.of("ok", false, "message", "Solution is missing");

            boolean[][] solution = Game.parseSolution(solutionStr);
            boolean correct = compareGrids(solution, selectedMatrix);

            if (correct && user != null) {
                double multiplier = switch (getDifficultyForPuzzle(puzzleId)) {
                    case "Medium" -> 1.5;
                    case "Hard" -> 2.5;
                    default -> 1.0; // Easy или неизвестная сложность
                };

                int baseScore = 150;
                int finalScore = (int) (baseScore * multiplier);

                Score score = new Score();
                score.setGame("nonogram");
                score.setScore(finalScore);
                score.setPlayedOn(ZonedDateTime.now());
                score.setPlayer(user.getUsername());
                scoreService.addScore(score);

                puzzleProgressService.markPuzzleAsSolved(user.getUsername(), puzzleId);
            }


            return Map.of("ok", correct, "message", correct ? "✅ Guessed!" : "❌ Try again");

        } catch (Exception e) {
            return Map.of("ok", false, "message", "Error: " + e.getMessage());
        }
    }

    @PostMapping("/comment/save")
    public String saveComment(@RequestParam String content,
                              @RequestParam int rating,
                              @RequestParam(defaultValue = "nonogram") String game,
                              @SessionAttribute(value = "user", required = false) User user) {
        if (user == null)
            return "redirect:/login";

        Comment comment = new Comment();
        comment.setPlayer(user.getUsername());
        comment.setGame(game);
        comment.setComment(content);
        comment.setCommentedOn(LocalDateTime.now());
        commentService.addComment(comment);

        Rating rate = new Rating();
        rate.setPlayer(user.getUsername());
        rate.setGame(game);
        rate.setRating(rating);
        rate.setRatedOn(ZonedDateTime.now());
        ratingService.setRating(rate);

        return "redirect:/";
    }
    private String getDifficultyForPuzzle(String puzzleId) {
        return switch (puzzleId) {
            case "Heart", "House", "Paw" -> "Easy";
            case "Dino", "Flower", "Pumpkin" -> "Medium";
            case "Medved", "Panda", "Psik", "Coffe" -> "Hard";
            default -> "Easy";
        };
    }


    private boolean compareGrids(boolean[][] expected, boolean[][] actual) {
        if (expected.length != actual.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (expected[i].length != actual[i].length) return false;
            for (int j = 0; j < expected[i].length; j++) {
                if (expected[i][j] != actual[i][j]) return false;
            }
        }
        return true;
    }
}
