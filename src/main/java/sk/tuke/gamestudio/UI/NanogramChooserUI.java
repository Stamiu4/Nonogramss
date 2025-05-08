package sk.tuke.gamestudio.UI;



import sk.tuke.gamestudio.gameclassses.Hints;
import sk.tuke.gamestudio.gameclassses.SolverN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NanogramChooserUI extends JFrame {


    private static class Puzzle {
        String name;
        String filePath;

        int progress;

        public Puzzle(String name, String filePath, int progress) {
            this.name = name;
            this.filePath = filePath;
            this.progress = progress;
        }
    }

    // Пример списка головоломок. Здесь все по умолчанию имеют progress = 0 (не решены)
    private Puzzle[] puzzles = new Puzzle[]{
            new Puzzle("Dino - ", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\dino.txt", 0),
            new Puzzle("Flower - 🌻", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Flower.txt", 0),
           // new Puzzle("Heart - 💟", "C:\\Users\\artee\\IdeaProjects\\gamestudio-8973\\src\\main\\java\\sk\\tuke\\gamestudio\\Nanogram_Puzzles\\heart.txt", 0),
            new Puzzle("Heart - 💟", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\heart.txt", 0),

            new Puzzle("House - 🏠", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\House.txt", 0),
            new Puzzle("Input", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\input.txt", 0),
            new Puzzle("Vaze ", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Vaza.txt", 0),
            new Puzzle("Pumpin - 🎃", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Pumpkin.txt", 0),
            new Puzzle("Coffe - ☕", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Coffe.txt", 0),
            new Puzzle("Paw - 🐾", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Paw.txt", 0),
            //new Puzzle("Cat - 😺", "C:\\Users\\artee\\IdeaProjects\\gamestudio-8973\\src\\main\\java\\sk\\tuke\\gamestudio\\Nanogram_Puzzles\\Cat.txt", 0),
            // new Puzzle("Penguino - 🐧", "C:\\Users\\artee\\IdeaProjects\\gamestudio-8973\\src\\main\\java\\sk\\tuke\\gamestudio\\Nanogram_Puzzles\\Penguino.txt", 0)
            new Puzzle("Rybicka - 🐟", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Rybicka.txt", 0),
            new Puzzle("Panda - 🐼", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Panda.txt", 0),
            new Puzzle("Kanvica - 🫖", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Kanvica.txt", 0),
            new Puzzle("Medved - 🐻", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Medved.txt", 0),//Не раблотает так как не может решать больше размера >20 или просто оно решает только семетричные нанограммы -- ?//P.S Ипсо все работает
            new Puzzle("Psik - 🐶", "D:\\IdeaProjects\\Nonogramss\\src\\main\\resources\\puzzles\\Psik.txt", 0),
    };

    public NanogramChooserUI() {
        setTitle("Choose a Nonogram");
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\artee\\IdeaProjects\\gamestudio-8975\\Nanogram_icon.png");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null);
        setIconImage(imageIcon.getImage());
        setVisible(true);


    }

    private void initUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Puzzle puzzle : puzzles) {
            JButton btn = new JButton();

            String progressIcon;
            switch (puzzle.progress) {
                case 2:
                    progressIcon = "✅";
                    break;
                case 1:
                    progressIcon = "⌛";
                    break;
                default:
                    progressIcon = "";
                    break;
            }
            btn.setText(puzzle.name + " " + progressIcon);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadPuzzle(puzzle.filePath);
                }
            });
            panel.add(btn);
            panel.add(Box.createVerticalStrut(10)); // отступ между кнопками
        }
        add(panel, BorderLayout.CENTER);
    }

    private void loadPuzzle(String fileName) {
        try {
            Hints hints = Hints.readFromFile(fileName);
            SolverN solver = new SolverN();
            solver.solveTaskFromFile(fileName);
            String solutionString = solver.getFirstSolution();
            if (solutionString == null) {
                JOptionPane.showMessageDialog(this, "Solution not found for the selected puzzle.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean[][] solutionGrid = Game.parseSolution(solutionString);
            SwingUtilities.invokeLater(() -> {
                new NanogramGameUI(solutionGrid, hints.getRowHints(), hints.getColHints());
            });
            JOptionPane.showMessageDialog(this, "Loading puzzle: " + fileName);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading puzzle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void showPuzzleChooser() {
        SwingUtilities.invokeLater(() -> {
            new NanogramChooserUI();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NanogramChooserUI();
        });
    }

}
