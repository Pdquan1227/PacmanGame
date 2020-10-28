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
	private final Color dotColor = new Color(192, 192, 0);
	private Color mazeColor;

	private boolean inGame = false;
	private boolean dying = false;

	// Changeable (Init 24,15)
	private final int b_size = 24;
	private final int b_num = 15;
	private final int scr_size = b_size * b_num;
	
	private final int pacAniDl = 2;
	private final int pacAniCnt = 4;
	
	// Changeable (Init 12,6)
	private final int g_max = 12;
	private final int p_speed = 6;

	private int pacAniCount = pacAniDl;
	private int pacAniDir = 1;
	private int pacAniPos = 0;
	
	// Changeable (Init 6)
	private int g_num = 6;
	
	private int pacsLeft, score;
	private int[] dx, dy;
	private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

	private Image ghost;
	private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
	private Image pacman3up, pacman3down, pacman3left, pacman3right;
	private Image pacman4up, pacman4down, pacman4left, pacman4right;

	private int pacman_x, pacman_y, pacmand_x, pacmand_y;
	private int req_dx, req_dy, view_dx, view_dy;

	// still unclear
	private final short levelData[] = { 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 21, 0, 0, 0, 17, 16,
			16, 16, 16, 16, 16, 16, 16, 16, 20, 21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 21, 0, 0, 0,
			17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20, 17,
			16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20, 25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0,
			21, 1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21, 1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0,
			21, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16,
			20, 0, 21, 1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21, 1, 17, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 20, 0, 21, 1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8,
			25, 24, 24, 24, 28 };

	
	private final int validSpeeds[] = { 1, 2, 3, 4, 6, 8 };
	
	// Changeable (Init 6) 
	private final int maxSpeed = 6;

	private int currentSpeed = 3;
	private short[] screenData;
	private Timer timer;

	public Board() {
		loadImages();
		initVariables();
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.black);
	}

	private void initVariables() {

		screenData = new short[b_num * b_num];
		mazeColor = new Color(5, 100, 5);
		d = new Dimension(400, 400);
		ghost_x = new int[g_max];
		ghost_dx = new int[g_max];
		ghost_y = new int[g_max];
		ghost_dy = new int[g_max];
		ghostSpeed = new int[g_max];
		dx = new int[4];
		dy = new int[4];

		timer = new Timer(40, this);
		timer.start();
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

		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, scr_size / 2 - 30, scr_size - 100, 50);
		g2d.setColor(Color.white);
		g2d.drawRect(50, scr_size / 2 - 30, scr_size - 100, 50);

		String s = "Press s to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g2d.setColor(Color.white);
		g2d.setFont(small);
		g2d.drawString(s, (scr_size - metr.stringWidth(s)) / 2, scr_size / 2);
	}

	private void drawScore(Graphics2D g) {

		int i;
		String s;

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		s = "Score: " + score;
		g.drawString(s, scr_size / 2 + 96, scr_size + 16);

		for (i = 0; i < pacsLeft; i++) {
			g.drawImage(pacman3left, i * 28 + 8, scr_size + 1, this);
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

		if (finished) {

			score += 50;

			if (g_num < g_max) {
				g_num++;
			}

			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

			initLevel();
		}
	}

	private void death() {

		pacsLeft--;

		if (pacsLeft == 0) {
			inGame = false;
		}

		continueLevel();
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

		g2d.drawImage(ghost, x, y, this);
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
			drawPacnanLeft(g2d);
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

	private void drawPacnanLeft(Graphics2D g2d) {

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

		for (y = 0; y < scr_size; y += b_size) {
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
					g2d.fillRect(x + 11, y + 11, 2, 2);
				}

				i++;
			}
		}
	}

	private void initGame() {

		pacsLeft = 3;
		score = 0;
		initLevel();
		g_num = 6;
		currentSpeed = 3;
	}

	private void initLevel() {

		int i;
		for (i = 0; i < b_num * b_num; i++) {
			screenData[i] = levelData[i];
		}

		continueLevel();
	}

	private void continueLevel() {

		short i;
		int dx = 1;
		int random;

		for (i = 0; i < g_num; i++) {

			ghost_y[i] = 4 * b_size;
			ghost_x[i] = 4 * b_size;
			ghost_dy[i] = 0;
			ghost_dx[i] = dx;
			dx = -dx;
			random = (int) (Math.random() * (currentSpeed + 1));

			if (random > currentSpeed) {
				random = currentSpeed;
			}

			ghostSpeed[i] = validSpeeds[random];
		}

		pacman_x = 7 * b_size;
		pacman_y = 11 * b_size;
		pacmand_x = 0;
		pacmand_y = 0;
		req_dx = 0;
		req_dy = 0;
		view_dx = -1;
		view_dy = 0;
		dying = false;
	}

	private void loadImages() {

		ghost = new ImageIcon("src/resources/images/ghost.png").getImage();
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
				} else if (key == KeyEvent.VK_PAUSE) {
					if (timer.isRunning()) {
						timer.stop();
					} else {
						timer.start();
					}
				}
			} else {
				if (key == 's' || key == 'S') {
					inGame = true;
					initGame();
				}
			}
		}

		
	}

}
