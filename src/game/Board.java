package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Dimension d;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
	private Image ii;

	// Dot to be eaten by PacMan.(Changeable (Init 192,192,0))
	private final Color dotColor = new Color(255, 237, 0);
	private Color mazeColor;

	// Indicate level currently played
	private int level = 1;

	// Check if is in game
	private boolean inGame = false;

	// Check if Pacman dies or not
	private boolean dying = false;

	// Size of 1 block in game (Initial: 24)
	private final int b_size = 24;

	// Number of blocks in game (Initial: 29)
	private final int b_num = 29;

	// Screen size
	private final int scr_size = b_size * b_num;

	private final int pacAniDl = 2;
	private final int pacAniCnt = 4;

	// Max ghosts (Initial: 12)
	private final int g_max = 12;

	// Pacman speed (Initial: 6)
	private final int p_speed = 6;

	private int pacAniCount = pacAniDl;
	private int pacAniDir = 1;
	private int pacAniPos = 0;

	// Number of ghosts in game (At start: 6)
	private int g_num = 6;

	// Lives/Score
	private int lives, score;

	// Ghost and pacman movement
	private int[] dx, dy;
	private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

	// Image for ghost
	private Image ghost1;
	private Image ghost2;
	private Image ghost3;
	private Image ghost4;

	// Image for lives
	private Image livesLeft;

	// Image for pacman movement
	private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
	private Image pacman3up, pacman3down, pacman3left, pacman3right;
	private Image pacman4up, pacman4down, pacman4left, pacman4right;

	// Direction
	private int pacman_x, pacman_y, pacmand_x, pacmand_y;

	// Control with movement keys
	private int req_dx, req_dy, view_dx, view_dy;

	// 0 = block; 1 = left border; 2 = top border; 4 = right border; 8 = bottom
	// border; 16 = white dot
	// 225 value;
	// each number is sum of all element
	// levelData represent level
	// Draw the level
	// Level 1
	private final short levelData1[] = { 19, 26, 26, 26, 26, 18, 26, 26, 26, 26, 26, 22, 0, 0, 0, 0, 0, 19, 26, 26, 26,
			26, 26, 18, 26, 26, 26, 26, 22, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21,
			0, 0, 0, 0, 21, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 21,
			21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 21, 17, 26, 26, 26,
			26, 16, 26, 26, 18, 26, 26, 24, 26, 26, 26, 26, 26, 24, 26, 26, 18, 26, 26, 16, 26, 26, 26, 26, 20, 21, 0,
			0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 21, 21, 0, 0, 0, 0, 21, 0,
			0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 21, 25, 26, 26, 26, 26, 20, 0, 0, 25, 26,
			26, 22, 0, 0, 0, 0, 0, 19, 26, 26, 28, 0, 0, 17, 26, 26, 26, 26, 28, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 5, 0,
			0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0,
			0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 19, 26, 26, 24, 26, 26, 18, 26, 26, 24, 26, 26, 22,
			0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 5, 0, 3, 2, 2, 2, 0, 2, 2, 2, 6, 0, 5, 0, 0, 21, 0, 0, 0, 0, 0, 27, 26,
			26, 26, 26, 16, 26, 26, 16, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 16, 26, 26, 16, 26, 26, 26, 26, 30, 0, 0, 0,
			0, 0, 21, 0, 0, 5, 0, 9, 8, 8, 8, 0, 8, 8, 8, 12, 0, 5, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0,
			21, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 17, 26, 26, 26,
			26, 26, 24, 26, 26, 26, 26, 26, 20, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			21, 0, 0, 21, 0, 0, 0, 0, 0, 19, 26, 26, 26, 26, 16, 26, 26, 24, 26, 26, 22, 0, 0, 0, 0, 0, 19, 26, 26, 24,
			26, 26, 16, 26, 26, 26, 26, 22, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21,
			0, 0, 0, 0, 21, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 21,
			25, 26, 22, 0, 0, 17, 26, 26, 18, 26, 26, 24, 10, 10, 10, 10, 10, 24, 26, 26, 18, 26, 26, 20, 0, 0, 19, 26,
			28, 0, 0, 21, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 21,
			0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 21, 0, 0, 19, 26, 24, 26, 26, 28,
			0, 0, 25, 26, 26, 22, 0, 0, 0, 0, 0, 19, 26, 26, 28, 0, 0, 25, 26, 26, 24, 26, 22, 21, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0,
			0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 24, 26, 26,
			26, 26, 26, 24, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 28 };

	// Level 2
	private final short levelData2[] = { 0, 0, 0, 0, 0, 0, 0, 19, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26,
			22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 19, 26, 28, 0, 19, 26, 26, 26, 26, 26, 26, 26, 26, 26, 22, 0, 25, 26, 22, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 19, 28, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 25, 22, 0, 0, 0, 0, 0, 0, 0, 19,
			28, 0, 0, 19, 18, 24, 26, 26, 18, 18, 18, 18, 18, 26, 26, 24, 18, 22, 0, 0, 25, 22, 0, 0, 0, 0, 0, 0, 21, 0,
			0, 19, 16, 28, 0, 0, 0, 9, 0, 0, 0, 12, 0, 0, 0, 25, 16, 22, 0, 0, 21, 0, 0, 0, 0, 19, 26, 28, 0, 19, 24,
			20, 0, 0, 23, 0, 0, 1, 0, 4, 0, 0, 23, 0, 0, 17, 24, 22, 0, 25, 26, 22, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 19,
			16, 22, 0, 1, 0, 4, 0, 19, 16, 22, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21, 0, 19, 26, 24, 26, 20, 0, 17, 16,
			20, 0, 1, 0, 4, 0, 17, 16, 20, 0, 17, 26, 24, 26, 22, 0, 21, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 25, 16, 28, 0,
			1, 0, 4, 0, 25, 16, 28, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 21, 0, 17, 26, 26, 26, 20, 0, 0, 21, 0, 0, 1, 0, 4,
			0, 0, 21, 0, 0, 17, 26, 26, 26, 20, 0, 21, 0, 0, 21, 0, 21, 0, 0, 0, 17, 22, 0, 21, 0, 19, 0, 0, 0, 22, 0,
			21, 0, 19, 20, 0, 0, 0, 21, 0, 21, 0, 19, 28, 0, 17, 26, 26, 26, 24, 24, 26, 24, 26, 24, 24, 24, 24, 24, 26,
			24, 26, 24, 24, 26, 26, 26, 20, 0, 25, 22, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 21, 0, 0, 21, 21, 0, 19, 24, 26, 26, 26, 26, 26, 26, 26, 26, 26, 18, 26, 18, 26, 26, 26, 26, 26,
			26, 26, 26, 26, 24, 22, 0, 21, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 21, 0, 21, 21, 0, 21, 0, 19, 26, 26, 26, 26, 26, 26, 26, 26, 28, 0, 25, 26, 26, 26, 26, 26, 26, 26,
			26, 22, 0, 21, 0, 21, 17, 10, 20, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 17,
			10, 20, 21, 0, 21, 0, 25, 26, 26, 26, 26, 26, 26, 26, 26, 22, 0, 19, 26, 26, 26, 26, 26, 26, 26, 26, 28, 0,
			21, 0, 21, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 21,
			0, 21, 0, 19, 26, 26, 26, 26, 26, 26, 30, 0, 21, 0, 21, 0, 27, 26, 26, 26, 26, 26, 26, 22, 0, 21, 0, 21, 21,
			0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 21, 21, 0, 17, 18,
			24, 18, 22, 0, 19, 18, 26, 22, 0, 21, 0, 21, 0, 19, 26, 18, 22, 0, 19, 18, 24, 18, 20, 0, 21, 21, 0, 17, 28,
			0, 25, 20, 0, 17, 28, 0, 25, 18, 20, 0, 17, 18, 28, 0, 25, 20, 0, 17, 28, 0, 25, 20, 0, 21, 21, 0, 29, 0, 0,
			0, 25, 18, 28, 0, 0, 0, 25, 16, 2, 16, 28, 0, 0, 0, 25, 18, 28, 0, 0, 0, 29, 0, 21, 21, 0, 0, 0, 23, 0, 0,
			29, 0, 0, 23, 0, 0, 25, 0, 28, 0, 0, 23, 0, 0, 29, 0, 0, 23, 0, 0, 0, 21, 21, 0, 0, 19, 24, 22, 0, 0, 0, 19,
			24, 22, 0, 0, 21, 0, 0, 19, 24, 22, 0, 0, 0, 19, 24, 22, 0, 0, 21, 21, 0, 19, 28, 0, 25, 22, 0, 19, 28, 0,
			25, 22, 0, 21, 0, 19, 28, 0, 25, 22, 0, 19, 28, 0, 25, 22, 0, 21, 25, 26, 28, 0, 0, 0, 25, 26, 28, 0, 0, 0,
			25, 26, 24, 26, 28, 0, 0, 0, 25, 26, 28, 0, 0, 0, 25, 26, 28, };

	// Level 3
	private final short levelData3[] = { 0, 0, 0, 19, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19,
			22, 0, 0, 0, 0, 0, 19, 24, 24, 22, 0, 0, 0, 0, 0, 0, 19, 18, 24, 18, 22, 0, 0, 0, 0, 0, 0, 19, 24, 24, 22,
			0, 0, 0, 0, 21, 0, 0, 17, 26, 30, 0, 0, 27, 26, 24, 28, 0, 25, 24, 26, 22, 0, 0, 0, 27, 20, 0, 0, 17, 26,
			22, 0, 0, 25, 22, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 21, 0, 19, 28, 0, 21, 0, 0, 0,
			21, 0, 21, 0, 0, 0, 19, 26, 18, 26, 26, 26, 26, 26, 18, 24, 22, 0, 0, 0, 21, 0, 21, 0, 0, 21, 19, 26, 26,
			20, 0, 25, 18, 26, 26, 20, 0, 21, 0, 0, 0, 0, 0, 21, 0, 17, 26, 26, 18, 28, 0, 21, 0, 0, 21, 29, 0, 0, 21,
			0, 0, 29, 0, 0, 25, 18, 24, 26, 26, 18, 30, 0, 25, 18, 28, 0, 0, 21, 0, 0, 21, 0, 27, 20, 0, 0, 0, 25, 22,
			0, 0, 0, 0, 0, 29, 0, 0, 0, 21, 0, 0, 0, 29, 0, 0, 0, 21, 0, 19, 28, 0, 0, 21, 23, 0, 0, 0, 21, 0, 23, 0, 0,
			0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 29, 21, 0, 0, 0, 21, 0, 25, 26, 26, 26, 26, 26,
			18, 26, 24, 26, 18, 26, 26, 26, 22, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 0, 19, 28, 0, 0, 0, 0, 0, 0, 0, 21, 0,
			0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 25, 22, 0, 0, 23, 21, 0, 19, 28, 0, 0, 19, 26, 26, 26, 26, 18, 16, 26, 26,
			26, 16, 18, 26, 26, 24, 26, 20, 0, 0, 25, 22, 0, 21, 21, 0, 21, 0, 0, 19, 28, 0, 0, 0, 0, 17, 20, 0, 0, 0,
			17, 20, 0, 0, 0, 0, 25, 22, 0, 0, 21, 0, 21, 21, 0, 21, 0, 19, 28, 0, 0, 23, 0, 0, 25, 28, 0, 0, 0, 25, 28,
			0, 0, 23, 0, 0, 25, 22, 0, 21, 0, 21, 21, 0, 21, 0, 21, 0, 0, 19, 24, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 24,
			22, 0, 0, 21, 0, 21, 0, 21, 21, 0, 21, 0, 17, 26, 26, 28, 0, 17, 26, 22, 0, 23, 0, 23, 0, 19, 26, 20, 0, 25,
			26, 26, 20, 0, 21, 0, 21, 25, 26, 16, 26, 20, 0, 0, 0, 0, 21, 0, 17, 26, 16, 18, 16, 26, 20, 0, 21, 0, 0, 0,
			0, 17, 26, 16, 26, 28, 0, 0, 21, 0, 25, 26, 26, 22, 0, 21, 0, 21, 0, 25, 16, 28, 0, 21, 0, 21, 0, 19, 26,
			26, 28, 0, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 25, 18, 28, 0, 21, 0, 0, 13, 0, 0, 21, 0, 25, 18, 28, 0, 0, 0, 0,
			17, 26, 22, 0, 0, 21, 0, 27, 22, 0, 0, 29, 0, 0, 17, 26, 30, 0, 27, 26, 20, 0, 0, 29, 0, 0, 19, 30, 0, 21,
			0, 21, 19, 26, 20, 0, 0, 25, 22, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 19, 28, 0, 0, 21, 0, 21, 21,
			0, 25, 22, 0, 0, 25, 26, 26, 26, 26, 24, 26, 26, 26, 26, 26, 24, 26, 26, 26, 26, 28, 0, 0, 19, 28, 0, 21,
			21, 0, 0, 25, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 28, 0, 0, 21, 21, 0, 0, 0,
			25, 26, 26, 18, 18, 26, 18, 18, 18, 30, 0, 27, 18, 18, 18, 26, 26, 18, 26, 26, 28, 0, 0, 0, 21, 25, 22, 0,
			0, 0, 0, 0, 17, 28, 0, 25, 24, 28, 0, 0, 0, 25, 24, 20, 0, 0, 21, 0, 0, 0, 0, 0, 0, 21, 0, 25, 26, 22, 0, 0,
			19, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 27, 16, 22, 0, 0, 0, 0, 19, 28, 0, 0, 0, 29, 0, 19, 28, 0, 0,
			0, 27, 18, 26, 26, 26, 26, 26, 18, 28, 0, 0, 25, 16, 26, 22, 0, 19, 28, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0,
			21, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 21, 0, 29, 0, 29, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 25, 26, 26, 26,
			30, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, };

	// Level 4 - Bonus
	private final short levelData4[] = { 19, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26, 18, 26,
			18, 26, 18, 26, 18, 26, 18, 26, 22, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0,
			21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26,
			16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0,
			21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26,
			16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0,
			21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 17, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16,
			26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 16, 26, 20, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21,
			0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 0, 21, 25, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24,
			26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 28, };

	// Speed (At start: 3)
	private final int validSpeeds[] = { 1, 2, 3, 4, 6, 8 };
	private int currentSpeed = validSpeeds[3];
	private int speedIn = 3;
	private int maxSpeed = 6;

	// Ghost Placement for each level
	private final short x1[] = { 8, 20, 8, 20, 14, 14 };
	private final short y1[] = { 12, 12, 14, 14, 11, 15 };
	private final short x2[] = { 13, 15, 13, 15, 13, 15, 1, 27 };
	private final short y2[] = { 6, 6, 8, 8, 10, 10, 17, 17 };
	private final short x3[] = { 6, 23, 2, 26, 7, 21, 18, 14 };
	private final short y3[] = { 5, 1, 16, 16, 23, 23, 6, 9 };
	private final short x4[] = { 1, 8, 20, 28, 1, 28, 1, 28, 1, 8, 20, 28 };
	private final short y4[] = { 1, 1, 1, 1, 6, 6, 22, 22, 28, 28, 28, 28 };

	private short[] screenData;

	private Timer timer;

	public Board() {
		loadImages();
		initVariables();
		initBoard();
	}

	private void loadImages() {
		livesLeft = new ImageIcon("src/resources/images/heart.png").getImage();
		ghost1 = new ImageIcon("src/resources/images/ghost1.png").getImage();
		ghost2 = new ImageIcon("src/resources/images/ghost3.png").getImage();
		ghost3 = new ImageIcon("src/resources/images/ghost2.png").getImage();
		ghost4 = new ImageIcon("src/resources/images/ghost4.png").getImage();
		pacman1 = new ImageIcon("src/resources/images/pacman.png").getImage();
		pacman2up = new ImageIcon("src/resources/images/up1.png").getImage();
		pacman3up = new ImageIcon("src/resources/images/up2.png").getImage();
		pacman4up = new ImageIcon("src/resources/images/up3.png").getImage();
		pacman2down = new ImageIcon("src/resources/images/down1.png").getImage();
		pacman3down = new ImageIcon("src/resources/images/down2.png").getImage();
		pacman4down = new ImageIcon("src/resources/images/down3.png").getImage();
		pacman2left = new ImageIcon("src/resources/images/left1.png").getImage();
		pacman3left = new ImageIcon("src/resources/images/left2.png").getImage();
		pacman4left = new ImageIcon("src/resources/images/left3.png").getImage();
		pacman2right = new ImageIcon("src/resources/images/right1.png").getImage();
		pacman3right = new ImageIcon("src/resources/images/right2.png").getImage();
		pacman4right = new ImageIcon("src/resources/images/right3.png").getImage();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.black);
	}

	private void initVariables() {
		screenData = new short[b_num * b_num];
		d = new Dimension(400, 400);
		ghost_x = new int[g_max];
		ghost_dx = new int[g_max];
		ghost_y = new int[g_max];
		ghost_dy = new int[g_max];
		ghostSpeed = new int[g_max];
		dx = new int[4];
		dy = new int[4];

		// Time needed to refresh screen (Initial: 40)
		timer = new Timer(40, this);
		timer.restart();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		initGame();
	}

	private void doAni() {
		pacAniCount--;

		if (pacAniCount <= 0) {
			pacAniCount = pacAniDl;
			pacAniPos = pacAniPos + pacAniDir;

			if (pacAniPos == (pacAniCnt - 1) || pacAniPos == 0) {
				pacAniDir = -pacAniDir;
			}
		}
	}

	private void playGame(Graphics2D g2d) {
		if (dying) {
			death();
		} else {
			movePacman();
			drawPacman(g2d);
			moveGhosts(g2d);
			checkMaze();
		}
	}

	private void showIntroScreen(Graphics2D g2d) {
		switch (level) {
		case 1:
			// Border
			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scr_size / 2 - 30, scr_size - 100, 50);
			g2d.setColor(dotColor);
			g2d.drawRect(50, scr_size / 2 - 30, scr_size - 100, 50);

			// This link with key press to start the game
			String s1 = "Press [Enter] to start .";
			Font small1 = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr1 = this.getFontMetrics(small1);
			
			g2d.setFont(small1);
			g2d.drawString(s1, (scr_size - metr1.stringWidth(s1)) / 2, scr_size / 2);
			break;
		case 2:
			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scr_size / 2 - 30, scr_size - 100, 50);
			g2d.setColor(dotColor);
			g2d.drawRect(50, scr_size / 2 - 30, scr_size - 100, 50);

			// This link with key press to start the game
			String s2 = "Press [Enter] to start .";
			Font small2 = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr2 = this.getFontMetrics(small2);

			g2d.setFont(small2);
			g2d.drawString(s2, (scr_size - metr2.stringWidth(s2)) / 2, scr_size / 2);
			break;
		case 3:
			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scr_size / 2 - 30, scr_size - 100, 50);
			g2d.setColor(dotColor);
			g2d.drawRect(50, scr_size / 2 - 30, scr_size - 100, 50);

			// This link with key press to start the game
			String s3 = "Press [Enter] to start .";
			Font small3 = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr3 = this.getFontMetrics(small3);

			g2d.setFont(small3);
			g2d.drawString(s3, (scr_size - metr3.stringWidth(s3)) / 2, scr_size / 2);
			break;
		case 4:
			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scr_size / 2 - 30, scr_size - 100, 50);
			g2d.setColor(dotColor);
			g2d.drawRect(50, scr_size / 2 - 30, scr_size - 100, 50);

			// This link with key press to start the game
			String s4 = "Press [Enter] to start .";
			Font small4 = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr4 = this.getFontMetrics(small4);

			g2d.setFont(small4);
			g2d.drawString(s4, (scr_size - metr4.stringWidth(s4)) / 2, scr_size / 2);
			break;
		}

	}

	private void drawScore(Graphics2D g) {

		int i;
		String s;

		g.setFont(smallFont);
		g.setColor(mazeColor);
		s = "Score: " + score;

		// Scoreboard
		g.drawString(s, 5 * scr_size / 6, scr_size + 16);

		// For drawing lives left
		for (i = 0; i < lives; i++) {
			g.drawImage(livesLeft, i * 28 + 8, scr_size + 1, this);
		}
	}

	private void checkMaze() {
		short i = 0;
		boolean finished = true;

		while (i < b_num * b_num && finished) {
			if ((screenData[i] & 48) != 0) {
				finished = false;
			}
			i++;
		}

		switch (level) {
		case 1:
			if (finished) {
				level = 2;
				lives = 3;
				score += 50;
				finished = false;
				initLevel();
			}
			break;
		case 2:
			if (finished) {
				lives = 3;
				level = 3;
				score += 130;
				finished = false;
				initLevel();
			}
			break;
		case 3:
			if (finished) {
				level = 4;
				for (int j = 1; j <= lives; j++) {
					score += 150;
				}
				lives = 1;
				finished = false;
				initLevel();
			}
			break;
		case 4:
			if (finished) {
				score += 1000;
				
				inGame = false;
			}
			break;

		}
	}

	private void death() {

		lives--;

		if (lives == 0) {
			inGame = false;
			g_num = 6;
			level = 1;
		} else {
			switch (level) {
			case 1:
				break;
			case 2:
				if (currentSpeed <= 6) {
					speedIn++;
				}
				currentSpeed = validSpeeds[speedIn];
				if (currentSpeed > maxSpeed) {
					currentSpeed = maxSpeed;
				}
				g_num++;
				break;
			case 3:
				if (currentSpeed <= 6) {
					speedIn++;
				}
				currentSpeed = validSpeeds[speedIn];
				if (currentSpeed > maxSpeed) {
					currentSpeed = maxSpeed;
				}
				g_num++;
				break;
			}
			continueLevel();
		}
	}

	private void moveGhosts(Graphics2D g2d) {

		short i;
		int pos;
		int count;

		for (i = 0; i < g_num; i++) {
			if (ghost_x[i] % b_size == 0 && ghost_y[i] % b_size == 0) {
				pos = ghost_x[i] / b_size + b_num * (int) (ghost_y[i] / b_size);

				count = 0;

				if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}

				if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				if (count == 0) {

					if ((screenData[pos] & 15) == 15) {
						ghost_dx[i] = 0;
						ghost_dy[i] = 0;
					} else {
						ghost_dx[i] = -ghost_dx[i];
						ghost_dy[i] = -ghost_dy[i];
					}

				} else {

					count = (int) (Math.random() * count);

					if (count > 3) {
						count = 3;
					}

					ghost_dx[i] = dx[count];
					ghost_dy[i] = dy[count];
				}

			}

			ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
			ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
			drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

			if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12) && pacman_y > (ghost_y[i] - 12)
					&& pacman_y < (ghost_y[i] + 12) && inGame) {

				dying = true;
			}
		}
	}

	private void drawGhost(Graphics2D g2d, int x, int y) {
		switch (level) {
		case 1:
			g2d.drawImage(ghost1, x, y, this);
			break;
		case 2:
			g2d.drawImage(ghost2, x, y, this);
			break;
		case 3:
			g2d.drawImage(ghost3, x, y, this);
			break;
		case 4:
			g2d.drawImage(ghost4, x, y, this);
			break;
		}

	}

	private void movePacman() {

		int pos;
		short ch;

		if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
			pacmand_x = req_dx;
			pacmand_y = req_dy;
			view_dx = pacmand_x;
			view_dy = pacmand_y;
		}

		if (pacman_x % b_size == 0 && pacman_y % b_size == 0) {
			pos = pacman_x / b_size + b_num * (int) (pacman_y / b_size);
			ch = screenData[pos];

			if ((ch & 16) != 0) {
				screenData[pos] = (short) (ch & 15);
				score++;
			}

			if (req_dx != 0 || req_dy != 0) {
				if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0) || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
						|| (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
						|| (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
					pacmand_x = req_dx;
					pacmand_y = req_dy;
					view_dx = pacmand_x;
					view_dy = pacmand_y;
				}
			}

			// Check for standstill
			if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
					|| (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
					|| (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
					|| (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
				pacmand_x = 0;
				pacmand_y = 0;
			}
		}
		pacman_x = pacman_x + p_speed * pacmand_x;
		pacman_y = pacman_y + p_speed * pacmand_y;
	}

	private void drawPacman(Graphics2D g2d) {

		if (view_dx == -1) {
			drawPacmanLeft(g2d);
		} else if (view_dx == 1) {
			drawPacmanRight(g2d);
		} else if (view_dy == -1) {
			drawPacmanUp(g2d);
		} else {
			drawPacmanDown(g2d);
		}
	}

	private void drawPacmanUp(Graphics2D g2d) {

		switch (pacAniPos) {
		case 1:
			g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
			break;
		}
	}

	private void drawPacmanDown(Graphics2D g2d) {

		switch (pacAniPos) {
		case 1:
			g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
			break;
		}
	}

	private void drawPacmanLeft(Graphics2D g2d) {

		switch (pacAniPos) {
		case 1:
			g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
			break;
		}
	}

	private void drawPacmanRight(Graphics2D g2d) {

		switch (pacAniPos) {
		case 1:
			g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
			break;
		}
	}

	private void drawMaze(Graphics2D g2d) {

		short i = 0;
		int x, y;

		switch (level) {
		case 1:
			// Level 1: 57, 255, 20
			mazeColor = new Color(57, 255, 20);
			break;
		case 2:
			// Level 2: 255, 66, 0
			mazeColor = new Color(255, 66, 0);
			break;
		case 3:
			// Level 3: 251, 72, 196
			mazeColor = new Color(251, 72, 196);
			break;
		case 4:
			// Bonus: 255, 131, 0
			mazeColor = new Color(255, 131, 0);
			break;
		}
		for (y = 1; y < scr_size; y += b_size) {
			for (x = 0; x < scr_size; x += b_size) {

				g2d.setColor(mazeColor);
				g2d.setStroke(new BasicStroke(2));

				if ((screenData[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + b_size - 1);
				}

				if ((screenData[i] & 2) != 0) {
					g2d.drawLine(x, y, x + b_size - 1, y);
				}

				if ((screenData[i] & 4) != 0) {
					g2d.drawLine(x + b_size - 1, y, x + b_size - 1, y + b_size - 1);
				}

				if ((screenData[i] & 8) != 0) {
					g2d.drawLine(x, y + b_size - 1, x + b_size - 1, y + b_size - 1);
				}

				if ((screenData[i] & 16) != 0) {
					g2d.setColor(dotColor);
					g2d.fillRect(x + 10, y + 10, 3, 3);
				}

				i++;
			}
		}
	}

	private void initGame() {
		switch (level) {
		case 1:
			lives = 3;
			break;
		case 2:
			lives = 3;
			break;
		case 3:
			lives = 3;
			break;
		case 4:
			lives = 1;
			break;
		}
		score = 0;
		initLevel();
	}

	private void initLevel() {
		switch (level) {
		case 1:
			for (int i = 0; i < b_num * b_num; i++) {
				screenData[i] = levelData1[i];
			}
			break;
		case 2:
			for (int i = 0; i < b_num * b_num; i++) {
				screenData[i] = levelData2[i];
			}
			break;
		case 3:
			for (int i = 0; i < b_num * b_num; i++) {
				screenData[i] = levelData3[i];
			}
			break;
		case 4:
			for (int i = 0; i < b_num * b_num; i++) {
				screenData[i] = levelData4[i];
			}
			break;
		}
		continueLevel();

	}

	private void continueLevel() {
		int dx = 1;
		switch (level) {
		case 1:
			for (short i = 0; i < g_num; i++) {
				// Starting place of the ghosts
				ghost_x[i] = x1[i] * b_size;
				ghost_y[i] = y1[i] * b_size;
				ghost_dy[i] = 0;
				ghost_dx[i] = dx;
				dx = -dx;

				// Ghost speed set to 3
				ghostSpeed[i] = validSpeeds[2];
			}

			// Starting place of pacman
			pacman_x = 14 * b_size;
			pacman_y = 22 * b_size;

			pacmand_x = 0;
			pacmand_y = 0;
			req_dx = 0;
			req_dy = 0;
			view_dx = -1;
			view_dy = 0;
			dying = false;
			break;
		case 2:
			for (short i = 0; i < g_num; i++) {
				// Starting place of the ghosts
				ghost_x[i] = x2[i] * b_size;
				ghost_y[i] = y2[i] * b_size;
				ghost_dy[i] = 0;
				ghost_dx[i] = dx;
				dx = -dx;

				// Ghost speed set to 4
				if (currentSpeed <= 3) {
					ghostSpeed[i] = validSpeeds[3];
				} else {
					ghostSpeed[i] = currentSpeed;
				}
			}

			// Starting place of pacman
			pacman_x = 14 * b_size;
			pacman_y = 25 * b_size;

			pacmand_x = 0;
			pacmand_y = 0;
			req_dx = 0;
			req_dy = 0;
			view_dx = -1;
			view_dy = 0;
			dying = false;
			break;
		case 3:
			for (short i = 0; i < g_num; i++) {
				// Starting place of the ghosts
				ghost_x[i] = x3[i] * b_size;
				ghost_y[i] = y3[i] * b_size;
				ghost_dy[i] = 0;
				ghost_dx[i] = dx;
				dx = -dx;
				// Ghost speed set to 3
				if (currentSpeed <= 3) {
					ghostSpeed[i] = validSpeeds[3];
				} else {
					ghostSpeed[i] = currentSpeed;
				}
			}
			// Starting place of pacman
			pacman_x = 14 * b_size;
			pacman_y = 18 * b_size;

			pacmand_x = 0;
			pacmand_y = 0;
			req_dx = 0;
			req_dy = 0;
			view_dx = -1;
			view_dy = 0;
			dying = false;
			break;
		case 4:
			// Ghost num set to max: 12

			g_num = 12;
			for (short i = 0; i < g_num; i++) {
				// Starting place of the ghosts
				ghost_x[i] = x4[i] * b_size;
				ghost_y[i] = y4[i] * b_size;
				ghost_dy[i] = 0;
				ghost_dx[i] = dx;
				dx = -dx;
				// Ghost speed set to 8
				ghostSpeed[i] = 8;
			}
			// Starting place of pacman
			pacman_x = 14 * b_size;
			pacman_y = 14 * b_size;

			pacmand_x = 0;
			pacmand_y = 0;
			req_dx = 0;
			req_dy = 0;
			view_dx = -1;
			view_dy = 0;
			dying = false;
			break;
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, d.width, d.height);

		drawMaze(g2d);
		drawScore(g2d);
		doAni();

		if (inGame) {
			playGame(g2d);
		} else {
			showIntroScreen(g2d);
		}
		g2d.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}

	class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			// To check if it is ingame or not
			// Left: dx -1
			// Right: dx +1
			// Up: dy -1
			// Down: dy +1
			// Space: Pause
			// Esc: Restart
			if (inGame) {
				if (key == KeyEvent.VK_LEFT) {
					req_dx = -1;
					req_dy = 0;
				} else if (key == KeyEvent.VK_RIGHT) {
					req_dx = 1;
					req_dy = 0;
				} else if (key == KeyEvent.VK_UP) {
					req_dx = 0;
					req_dy = -1;
				} else if (key == KeyEvent.VK_DOWN) {
					req_dx = 0;
					req_dy = 1;
				} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
					inGame = false;
					level = 1;
					score = 0;
				} else if (key == KeyEvent.VK_SPACE) {
					if (timer.isRunning()) {
						timer.stop();
					} else {
						timer.start();
					}
				}
			} else {

				// input key to start the game
				// Initial key: Enter
				if (key == KeyEvent.VK_ENTER) {
					inGame = true;
					initGame();
				}
			}
		}
	}

}
