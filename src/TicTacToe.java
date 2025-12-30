package src;

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.Random;
import javax.swing.*;

/**
 * A simple Swing-based Tic Tac Toe game implemented using Java Swing.
 * <p>
 * Features:
 * <ul>
 *   <li>3x3 board with clickable buttons</li>
 *   <li>Menu bar with New Game and Exit</li>
 *   <li>Win and draw detection</li>
 *   <li>Game-over dialog that offers replay</li>
 * </ul>
 */
public class TicTacToe extends JPanel implements ActionListener {

    /** Serialization version ID (required because this class extends {@link JPanel}). */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Random number generator used to choose the first player. */
    private final Random random = new Random();

    /** The main application window for the game. */
    private final JFrame frame = new JFrame("TicTacToe");

    /** Label used to display game messages such as turn and winner status. */
    private final JLabel textField = new JLabel();

    /** Array of 9 buttons representing the tic tac toe board cells. */
    private final JButton[] buttons = new JButton[9];

    /** True if it is X's turn; false if it is O's turn. */
    private boolean player1_turn;

    /** True when the game has ended (win or draw), preventing further moves. */
    private boolean gameOver = false;

    /**
     * Constructs the Tic Tac Toe UI, initializes the board, and displays the window.
     */
    public TicTacToe() {
        JPanel title_panel = new JPanel();
        JPanel button_panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(Color.BLUE);
        frame.setLayout(new BorderLayout());

        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Ink Free", Font.BOLD, 75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic Tac Toe");
        textField.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);
        title_panel.add(textField, BorderLayout.CENTER);

        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(Color.WHITE);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        frame.setJMenuBar(createMenuBar());
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Start a fresh game after the UI appears.
        newGame();
    }

    /**
     * Creates the top-level menu bar for the application.
     *
     * @return the configured {@link JMenuBar}.
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    /**
     * Creates the "File" menu containing:
     * <ul>
     *   <li>New Game: resets the board</li>
     *   <li>Exit: closes the application</li>
     * </ul>
     *
     * @return the configured {@link JMenu}.
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem newGameMenu = new JMenuItem("New Game");
        newGameMenu.setMnemonic(KeyEvent.VK_N);
        newGameMenu.addActionListener(_ -> newGame());

        final JMenuItem exitGameMenu = new JMenuItem("Exit");
        exitGameMenu.setMnemonic(KeyEvent.VK_X);
        exitGameMenu.addActionListener(_ -> System.exit(0));

        fileMenu.add(newGameMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitGameMenu);

        return fileMenu;
    }

    /**
     * Resets the game to a fresh state:
     * <ul>
     *   <li>Clears all button text</li>
     *   <li>Re-enables all buttons</li>
     *   <li>Clears backgrounds and resets colors</li>
     *   <li>Resets the gameOver flag</li>
     *   <li>Selects a starting player</li>
     * </ul>
     */
    public void newGame() {
        textField.setText("Tic Tac Toe");
        gameOver = false;

        for (JButton b : buttons) {
            b.setText("");
            b.setEnabled(true);
            b.setBackground(null);
            b.setForeground(Color.BLACK);
        }

        firstTurn();
    }

    /**
     * Handles clicks on the 3x3 game buttons.
     * <p>
     * If the game is already over, clicks are ignored.
     * After a valid move, winner/draw detection is performed.
     *
     * @param e the action event fired when a button is clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i] && buttons[i].getText().isEmpty()) {

                if (player1_turn) {
                    buttons[i].setForeground(Color.RED);
                    buttons[i].setText("X");
                    player1_turn = false;
                    textField.setText("O turn");
                } else {
                    buttons[i].setForeground(Color.BLUE);
                    buttons[i].setText("O");
                    player1_turn = true;
                    textField.setText("X turn");
                }

                checkWinner();
                return;
            }
        }
    }

    /**
     * Checks whether the board is full (no empty button labels).
     *
     * @return true if all 9 cells are filled; false otherwise.
     */
    private boolean isBoardFull() {
        for (JButton bt : buttons) {
            if (bt.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays a game-over dialog with a message and allows the user to replay.
     * <p>
     * If the user selects "Play Again", the board resets. Otherwise, the board is disabled.
     *
     * @param message the message to display (e.g., "X wins!", "O wins!", "It's a draw!").
     */
    private void showGameOver(String message) {
        int result = JOptionPane.showOptionDialog(
                frame,
                message + "\n\nWould you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "Close"},
                "Play Again"
        );

        if (result == JOptionPane.YES_OPTION) {
            newGame();
        } else {
            for (JButton bt : buttons) {
                bt.setEnabled(false);
            }
            textField.setText(message + " (Game ended)");
        }
    }

    /**
     * Randomly selects who starts (X or O) after a short delay without freezing the UI.
     * Uses a Swing {@link Timer} instead of {@link Thread#sleep(long)}.
     */
    public void firstTurn() {
        textField.setText("Deciding first turn...");

        Timer timer = new Timer(500, _ -> {
            player1_turn = random.nextBoolean();
            textField.setText(player1_turn ? "X Turn" : "O Turn");
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Checks all win conditions for X and O. If a win is found, triggers win handling.
     * If no win exists and the board is full, the game is declared a draw.
     */
    public void checkWinner() {
        // X win conditions
        if (buttons[0].getText().equals("X") && buttons[1].getText().equals("X") && buttons[2].getText().equals("X")) xWins(0, 1, 2);
        if (buttons[3].getText().equals("X") && buttons[4].getText().equals("X") && buttons[5].getText().equals("X")) xWins(3, 4, 5);
        if (buttons[6].getText().equals("X") && buttons[7].getText().equals("X") && buttons[8].getText().equals("X")) xWins(6, 7, 8);
        if (buttons[0].getText().equals("X") && buttons[3].getText().equals("X") && buttons[6].getText().equals("X")) xWins(0, 3, 6);
        if (buttons[1].getText().equals("X") && buttons[4].getText().equals("X") && buttons[7].getText().equals("X")) xWins(1, 4, 7);
        if (buttons[2].getText().equals("X") && buttons[5].getText().equals("X") && buttons[8].getText().equals("X")) xWins(2, 5, 8);
        if (buttons[0].getText().equals("X") && buttons[4].getText().equals("X") && buttons[8].getText().equals("X")) xWins(0, 4, 8);
        if (buttons[2].getText().equals("X") && buttons[4].getText().equals("X") && buttons[6].getText().equals("X")) xWins(2, 4, 6);

        // O win conditions
        if (buttons[0].getText().equals("O") && buttons[1].getText().equals("O") && buttons[2].getText().equals("O")) oWins(0, 1, 2);
        if (buttons[3].getText().equals("O") && buttons[4].getText().equals("O") && buttons[5].getText().equals("O")) oWins(3, 4, 5);
        if (buttons[6].getText().equals("O") && buttons[7].getText().equals("O") && buttons[8].getText().equals("O")) oWins(6, 7, 8);
        if (buttons[0].getText().equals("O") && buttons[3].getText().equals("O") && buttons[6].getText().equals("O")) oWins(0, 3, 6);
        if (buttons[1].getText().equals("O") && buttons[4].getText().equals("O") && buttons[7].getText().equals("O")) oWins(1, 4, 7);
        if (buttons[2].getText().equals("O") && buttons[5].getText().equals("O") && buttons[8].getText().equals("O")) oWins(2, 5, 8);
        if (buttons[0].getText().equals("O") && buttons[4].getText().equals("O") && buttons[8].getText().equals("O")) oWins(0, 4, 8);
        if (buttons[2].getText().equals("O") && buttons[4].getText().equals("O") && buttons[6].getText().equals("O")) oWins(2, 4, 6);

        // Draw condition
        if (!gameOver && isBoardFull()) {
            gameOver = true;
            showGameOver("It's a draw!");
        }
    }
    /**
     * Handles a winning game state for either player.
     * <p>
     * This method highlights the three winning cells, disables all remaining
     * board buttons to prevent further moves, marks the game as over, and
     * displays a game-over dialog announcing the winner.
     * <p>
     * If the game has already ended, this method returns immediately.
     *
     * @param winner the symbol of the winning player ("X" or "O")
     * @param a index of the first winning cell
     * @param b index of the second winning cell
     * @param c index of the third winning cell
     */
    private void handleWin(String winner, int a, int b, int c) {
        if (gameOver) return;
        gameOver = true;

        buttons[a].setBackground(Color.BLUE);
        buttons[b].setBackground(Color.BLUE);
        buttons[c].setBackground(Color.BLUE);

        for (JButton bt : buttons) {
            bt.setEnabled(false);
        }

        showGameOver(winner + " wins!");
    }
    /**
     * Handles an X win by delegating to {@link #handleWin(String, int, int, int)}.
     * <p>
     * This method is called when player X satisfies a winning condition.
     *
     * @param a index of the first winning cell
     * @param b index of the second winning cell
     * @param c index of the third winning cell
     */
    public void xWins(int a, int b, int c) {
        handleWin("X", a, b, c);
    }


    /**
     * Handles an O win by delegating to {@link #handleWin(String, int, int, int)}.
     * <p>
     * This method is called when player O satisfies a winning condition.
     *
     *
     * @param a index of the first winning cell
     * @param b index of the second winning cell
     * @param c index of the third winning cell
     */
    public void oWins(int a, int b, int c) {
        handleWin("O", a, b, c);
    }


}
