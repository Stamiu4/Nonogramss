package sk.tuke.gamestudio.model;

public class Puzzle {
    private final String name;
    private final String difficulty;

    public Puzzle(String name, String difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }


}
