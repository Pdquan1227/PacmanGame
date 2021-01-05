package game;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;

	public Game() {
		initUI1();
	}

	private void initUI1() {
		Board board = new Board();
		add(board);
		setTitle("Pacman - Limited Edition !");
		setSize(709, 756);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			var ex = new Game();
			ex.setVisible(true);
		});
	}
}
