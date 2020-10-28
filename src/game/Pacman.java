package game;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Pacman extends JFrame {

	private static final long serialVersionUID = 1L;
	public Pacman() {
        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Changeable (Init 380, 420)
        setSize(380, 420);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            var ex = new Pacman();
            ex.setVisible(true);
        });
    }
}
