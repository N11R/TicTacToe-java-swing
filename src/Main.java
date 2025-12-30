package src;

import javax.swing.*;

/**
 * Entry point for the Tic Tac Toe application.
 *
 * <p>
 * This class is responsible for launching the graphical user interface
 * by creating an instance of {@link TicTacToe}. The actual game logic
 * and user interface are handled within the {@code TicTacToe} class.
 *
 * @author Nasra
 */
public class Main extends JPanel {

    /**
     * Starts the Tic Tac Toe application.
     * <p>
     * This method is invoked by the Java Virtual Machine (JVM) when the
     * program begins execution. It initializes the Swing-based game by
     * instantiating the {@link TicTacToe} class.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
