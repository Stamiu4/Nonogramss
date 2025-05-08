package sk.tuke.gamestudio.UI;

import sk.tuke.gamestudio.entities.Comment;
import sk.tuke.gamestudio.entities.Rating;
import sk.tuke.gamestudio.entities.Score;
import sk.tuke.gamestudio.service.CommentJDBC;
import sk.tuke.gamestudio.service.RatingJDBC;
import sk.tuke.gamestudio.service.ScoreJDBC;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.state.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class    NanogramGameUI extends JFrame {
    private int numRows, numCols;
    private JButton[][] gridButtons;
    private boolean[][] solution;
    private String[] rowHints;
    private int baseScore = 0;
    private int finalScore = 0;
    private String[] colHints;


    private static final int MODE_FILL = 0;
    private static final int MODE_MARK = 1;
    private static final int MODE_COLOR = 2;
    private int currentMode = MODE_FILL;
    private Color currentColor = Color.RED;

    private GameState gameState;
    private JLabel stateLabel;
    private JPanel controlPanel;


    private ScoreService scoreService;


    private long startTime;
    private JLabel elapsedTimeLabel;
    private Timer timer;

    public NanogramGameUI(boolean[][] solution, String[] rowHints, String[] colHints) {
        this.solution = solution;
        this.rowHints = rowHints;
        this.colHints = colHints;
        this.numRows = solution.length;
        this.numCols = solution[0].length;
        gameState = GameState.PLAYING;
        setTitle("Nonogram");
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\artee\\IdeaProjects\\gamestudio-8975\\Nanogram_icon.png");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null);
        setIconImage(imageIcon.getImage());
        setVisible(true);
        startTime = System.currentTimeMillis();
        startTimer();
    }

    private void initUI() {
        // Панель с подсказками и игровым полем
        JPanel mainPanel = new JPanel(new GridLayout(numRows + 1, numCols + 1));
        mainPanel.add(new JLabel(""));

        for (int j = 0; j < numCols; j++) {
            JLabel colHintLabel = new JLabel(colHints[j], SwingConstants.CENTER);
            mainPanel.add(colHintLabel);
        }

        gridButtons = new JButton[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            JLabel rowHintLabel = new JLabel(rowHints[i], SwingConstants.RIGHT);
            mainPanel.add(rowHintLabel);
            for (int j = 0; j < numCols; j++) {
                final JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(40, 40));
                cell.setBackground(Color.WHITE);
                cell.setOpaque(true);
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                cell.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (currentMode == MODE_FILL) {
                            if (cell.getBackground().equals(Color.WHITE)) {
                                cell.setBackground(Color.BLACK);
                                cell.setText("");
                            } else {
                                cell.setBackground(Color.WHITE);
                            }
                        } else if (currentMode == MODE_MARK) {
                            if (cell.getText().equals("")) {
                                cell.setText("X");
                                cell.setForeground(Color.RED);
                            } else {
                                cell.setText("");
                            }
                            cell.setBackground(Color.WHITE);
                        } else if (currentMode == MODE_COLOR) {
                            cell.setBackground(currentColor);
                            cell.setText("");
                        }
                    }
                });
                gridButtons[i][j] = cell;
                mainPanel.add(cell);
            }
        }
        add(mainPanel, BorderLayout.CENTER);

        // Панель управления
        controlPanel = new JPanel();
        stateLabel = new JLabel("State: PLAYING");
        controlPanel.add(stateLabel);

        // Метка для отображения прошедшего времени
        elapsedTimeLabel = new JLabel("Time: 0 sec");
        controlPanel.add(elapsedTimeLabel);

        JToggleButton modeToggle = new JToggleButton("Mode: insert");
        modeToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modeToggle.isSelected()) {
                    currentMode = MODE_MARK;
                    modeToggle.setText("Mode: mark");
                } else {
                    currentMode = MODE_FILL;
                    modeToggle.setText("Mode: insert");
                }
            }
        });
        controlPanel.add(modeToggle);

        JButton checkButton = new JButton("Check Solution");
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkUserSolution()) {
                    gameState = GameState.SOLVED;
                    stateLabel.setText("State: SOLVED");
                    timer.stop(); // Останавливаем таймер при решении
                    // Вычисляем время решения в секундах и множитель
                    long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
                    double multiplier;
                    if (elapsedSeconds < 180) { // менее 3 минут
                        multiplier = 2.0;
                    } else if (elapsedSeconds < 420) { // менее 7 минут
                        multiplier = 1.5;
                    } else if (elapsedSeconds < 900) { // менее 15 минут
                        multiplier = 1.2;
                    } else {
                        multiplier = 1.0;
                    }
                    baseScore = 150;
                    finalScore = (int) (baseScore * multiplier);
                    JOptionPane.showMessageDialog(null, "Solution is correct, Congrats!\nYour score: " + finalScore);

                    int choice = JOptionPane.showConfirmDialog(
                            NanogramGameUI.this,
                            "Would you like to color your nonogram?",
                            "Coloring Option",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        currentMode = MODE_COLOR;
                        stateLabel.setText("State: COLORING");
                        addColorSelectionPanel();
                        addFinishColoringButton();
                    } else {
                        openFeedbackDialog();
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Solution isn't correct, try again.");
                }
            }
        });
        controlPanel.add(checkButton);

        JButton feedbackButton = new JButton("Submit Feedback");
        feedbackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFeedbackDialog();
            }
        });
        controlPanel.add(feedbackButton);

        JButton viewPuzzlesButton = new JButton("View Puzzles");
        viewPuzzlesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NanogramChooserUI.showPuzzleChooser();
                dispose();
            }
        });
        controlPanel.add(viewPuzzlesButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                long seconds = elapsedMillis / 1000;
                elapsedTimeLabel.setText("Time: " + seconds + " sec");
            }
        });
        timer.start();
    }

    private boolean checkUserSolution() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boolean userFilled = gridButtons[i][j].getBackground().equals(Color.BLACK);
                if (userFilled != solution[i][j])
                    return false;
            }
        }
        return true;
    }

    // Добавляем панель выбора цвета (фиксированные варианты)
    private void addColorSelectionPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createTitledBorder("Select Color"));

        JButton redButton = new JButton("Red");
        redButton.setBackground(Color.RED);
        redButton.addActionListener(e -> currentColor = Color.RED);
        colorPanel.add(redButton);

        JButton yellowButton = new JButton("Yellow");
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.addActionListener(e -> currentColor = Color.YELLOW);
        colorPanel.add(yellowButton);

        JButton blueButton = new JButton("Blue");
        blueButton.setBackground(Color.BLUE);
        blueButton.setForeground(Color.WHITE);
        blueButton.addActionListener(e -> currentColor = Color.BLUE);
        colorPanel.add(blueButton);

        JButton greenButton = new JButton("Green");
        greenButton.setBackground(Color.GREEN);
        greenButton.addActionListener(e -> currentColor = Color.GREEN);
        colorPanel.add(greenButton);

        JButton orangeButton = new JButton("Orange");
        orangeButton.setBackground(Color.ORANGE);
        orangeButton.addActionListener(e -> currentColor = Color.ORANGE);
        colorPanel.add(orangeButton);

        JButton pinkButton = new JButton("Pink");
        pinkButton.setBackground(Color.PINK);
        pinkButton.addActionListener(e -> currentColor = Color.PINK);
        colorPanel.add(pinkButton);

        JButton grayButton = new JButton("Gray");
        grayButton.setBackground(Color.GRAY);
        grayButton.addActionListener(e -> currentColor = Color.GRAY);
        colorPanel.add(grayButton);

        JButton brownButton = new JButton("Brown");
        brownButton.setBackground(new Color(139, 69, 19));
        brownButton.addActionListener(e -> currentColor = new Color(139, 69, 19));
        colorPanel.add(brownButton);

        controlPanel.add(colorPanel);
        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private void addFinishColoringButton() {
        JButton finishButton = new JButton("Finish Coloring");
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFeedbackDialog();
                dispose();
            }
        });
        controlPanel.add(finishButton);
        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private void openFeedbackDialog() {
        JDialog dialog = new JDialog(this, "Submit Feedback", true);
        dialog.setLayout(new BorderLayout());

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(new JLabel("Player Name:"));
        JTextField nameField = new JTextField(15);
        namePanel.add(nameField);
        dialog.add(namePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Rating (1-5):"));
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
        panel.add(ratingSpinner);

        panel.add(new JLabel("Comment:"));
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(commentArea);
        panel.add(scrollPane);

        dialog.add(panel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int rating = (int) ratingSpinner.getValue();
                String comment = commentArea.getText();

                //Rating userRating = new Rating("Nonogram", playerName, rating, LocalDateTime.now());
                Rating userRating = new Rating();
                userRating.setRating(rating);
                userRating.setGame("Nonogram");
                userRating.setPlayer(playerName);
                userRating.setRatedOn(ZonedDateTime.now(ZoneId.of("Europe/Bratislava")));
                //Comment userComment = new Comment("Nonogram", playerName, comment, LocalDateTime.now());
                Comment userComment = new Comment();
                userComment.setComment(comment);
                userComment.setPlayer(playerName);
                userComment.setGame("Nonogram");
                userComment.setCommentedOn(LocalDateTime.now(ZoneId.of("Europe/Bratislava")));
                //Score score = new Score(playerName, "Nongrams", finalScore, LocalDateTime.now());
                Score userScore = new Score();
                userScore.setPlayer(playerName);
                userScore.setGame("Nonogram");
                userScore.setScore(finalScore);
                userScore.setPlayedOn(ZonedDateTime.now(ZoneId.of("Europe/Bratislava")));
                //Rating2 rating2_score = new Rating2("Nonogram", playerName, finalScore, new Date());
                CommentJDBC commentDAO = new CommentJDBC();
                RatingJDBC ratingDAO = new RatingJDBC();
                //Rating2DBC rating2DAO = new Rating2DBC();
                ScoreJDBC scoreDAO = new ScoreJDBC();

                ratingDAO.addRating(userRating);
//                rating2DAO.addRating(rating2_score);
                commentDAO.addComment(userComment);

                scoreDAO.addScore(userScore);

                System.out.println("Submitted rating: " + userRating);
                System.out.println("Submitted comment: " + userComment);
                System.out.println("Score recived: " + finalScore);

                JOptionPane.showMessageDialog(dialog, "Thank you for your feedback!");
                dialog.dispose();

                int choice = JOptionPane.showConfirmDialog(
                        NanogramGameUI.this,
                        "Would you like to choose another puzzle?",
                        "Choose Puzzle",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    NanogramChooserUI.showPuzzleChooser();
                }
            }
        });
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
