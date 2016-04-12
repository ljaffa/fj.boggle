package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.google.inject.Inject;

public class BoggleFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BoggleThread thread;
	private final Logic logic;
	private Cell[][] boggle;
	private Timer timer;
	private final Font letterFont;

	private final Container container;

	private final JPanel boardPanel;
	private final JPanel leftPanel;
	private final JPanel topPanel;
	private final JPanel rightPanel;
	private final JPanel scorePanel;
	private final JTextField wordTextField;
	private final JTextArea wordListArea;
	private final JLabel[][] boggleBoard;
	private final JLabel timerLabel;
	private final JLabel status;
	private final JLabel imageLabel;
	private final JLabel score1, score2;
	private final JButton shuffle;
	private final JButton rotate;
	private final JButton pauseButton;
	private final ImageIcon boggleIcon;

	private final ArrayList<String> words;
	private final String[][] copy;
	private int interval = 181;
	private int turn = 1, players = 1;
	private int total1, total2, total = 0;
	private boolean paused;
	private final Stack<Integer> rowsSelected;
	private final Stack<Integer> columnsSelected;

	@Inject
	public BoggleFrame(int players) {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg")).getImage());

		container = getContentPane();
		boardPanel = new JPanel();
		topPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		scorePanel = new JPanel();
		wordListArea = new JTextArea();
		shuffle = new JButton("Reset Board!");
		rotate = new JButton("ROTATE");
		pauseButton = new JButton("PAUSE");
		imageLabel = new JLabel(new ImageIcon(getClass().getResource("/boggle.png")));
		timerLabel = new JLabel();
		score1 = new JLabel("Score 1: " + total);
		score2 = new JLabel("Score 2: " + total);
		status = new JLabel();
		boggleBoard = new JLabel[4][4];
		wordTextField = new JTextField();
		rowsSelected = new Stack<Integer>();
		columnsSelected = new Stack<Integer>();

		logic = new Logic();
		boggle = logic.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);

			}
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				wordTextField.requestFocus();
			}
		});

		letterFont = new Font("Calibri", Font.BOLD, 50);
		boggleIcon = new ImageIcon(getClass().getResource("/boggleMessage.png"));

		words = new ArrayList<String>();
		copy = new String[4][4];
		paused = false;
		turn = 1;
		this.players = players;
		format();
		addToPanels();
		addActionListeners();
		addTimer();
		resetBoard();

	}

	private void addToPanels() {
		scorePanel.add(score1);
		scorePanel.add(score2);

		topPanel.add(imageLabel, BorderLayout.NORTH);
		topPanel.add(timerLabel, BorderLayout.SOUTH);
		topPanel.add(scorePanel, BorderLayout.WEST);
		topPanel.add(status, BorderLayout.EAST);

		rightPanel.add(pauseButton, BorderLayout.NORTH);
		rightPanel.add(boardPanel, BorderLayout.CENTER);
		rightPanel.add(rotate, BorderLayout.SOUTH);

		leftPanel.add(wordTextField, BorderLayout.SOUTH);
		leftPanel.add(wordListArea, BorderLayout.CENTER);
		leftPanel.add(shuffle, BorderLayout.NORTH);

		container.add(rightPanel, BorderLayout.CENTER);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);
	}

	private void format() {
		container.setLayout(new BorderLayout());

		boardPanel.setLayout(new GridLayout(4, 4));

		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.BLUE);

		leftPanel.setLayout(new BorderLayout());
		rightPanel.setLayout(new BorderLayout());

		scorePanel.setBackground(Color.blue);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 35);
		Font fontTwo = new Font("Berlin Sans FB", Font.PLAIN, 30);

		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(font);
		timerLabel.setForeground(Color.WHITE);

		wordTextField.setOpaque(true);
		wordTextField.setBackground(new Color(204, 204, 255));
		wordTextField.setForeground(Color.BLUE);
		wordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		wordTextField.setPreferredSize(new Dimension(50, 40));
		wordTextField.setFont(font);
		wordTextField.setFocusable(true);

		score1.setFont(font);
		score1.setForeground(Color.WHITE);
		score2.setFont(font);
		score2.setForeground(Color.WHITE);

		status.setFont(font);
		status.setForeground(Color.WHITE);
		status.setText("hhhel");

		wordListArea.setBackground(Color.WHITE);
		wordListArea.setForeground(Color.BLACK);
		wordListArea.setFont(fontTwo);
		wordListArea.setEditable(false);
		wordListArea.setPreferredSize(new Dimension(200, 50));

		shuffle.setBackground(new Color(204, 204, 255));
		shuffle.setForeground(Color.BLUE);
		shuffle.setFont(font);
		shuffle.setBorder(null);
		shuffle.setBorderPainted(false);
		shuffle.setFocusPainted(false);
		shuffle.setRolloverEnabled(false);

		rotate.setBackground(new Color(204, 204, 255));
		rotate.setForeground(Color.BLUE);
		rotate.setFont(font);
		rotate.setBorder(null);
		rotate.setBorderPainted(false);
		rotate.setFocusPainted(false);
		rotate.setRolloverEnabled(false);

		pauseButton.setBackground(new Color(204, 204, 255));
		pauseButton.setForeground(Color.BLUE);
		pauseButton.setFont(fontTwo);
		pauseButton.setBorder(null);
		pauseButton.setBorderPainted(false);
		pauseButton.setFocusPainted(false);
		pauseButton.setRolloverEnabled(false);
	}

	private void addActionListeners() {

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() {

			public boolean postProcessKeyEvent(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					checkWord();
				}
				return false;
			}
		});
		shuffle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				resetBoard();
			}
		});

		rotate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				rotateMatrixRight();
			}
		});

		pauseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				paused = true;
				UIManager.put("OptionPane.okButtonText", "Resume");
				JOptionPane.showMessageDialog(null, "The game is paused.\nClick resume to resume the game.", "BOGGLE",
						JOptionPane.PLAIN_MESSAGE, boggleIcon);

				paused = false;

			}
		});
		wordTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				checkWord();
			}
		});
	}

	public void checkWord() {
		String word = wordTextField.getText().toLowerCase();
		wordTextField.setText("");
		resetCells();
		if (word.length() == 0) {
			return;
		}
		boolean valid = false;

		if (words.contains(word)) {
			JOptionPane.showMessageDialog(null, "You already chose that word. Try again.", "BOGGLE",
					JOptionPane.PLAIN_MESSAGE, boggleIcon);
			return;
		}

		try {
			valid = logic.checkWord(word);
		} catch (TooSmallWordException e) {
			JOptionPane.showMessageDialog(null, "The word is not at least 3 letters long.", "BOGGLE",
					JOptionPane.PLAIN_MESSAGE, boggleIcon);
			return;
		}

		if (valid) {
			thread = new BoggleThread(word, BoggleFrame.this, wordTextField);
			thread.start();

		} else {
			JOptionPane.showMessageDialog(null, "This word does not exist in the board.", "BOGGLE",
					JOptionPane.PLAIN_MESSAGE, new ImageIcon("./boggleMessage.png"));

		}
	}

	private void addTimer() {
		timer = new Timer(1000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if (!paused) {
					timerLabel.setText("Timer: " + String.valueOf(checkTimer()));
				}
			}

		});
	}

	private int checkTimer() {
		if (interval == 0) {
			endRound();
			return 0;
		}
		return --interval;
	}

	public void endRound() {
		timer.stop();
		setStatus(2);
		if (players == 2) {
			if (turn == 1) {
				setStatus(6);
				turn = 2;
				total1 = total;
				total = 0;
				score1.setText("Score: ???");
				wordListArea.setText("");
				words.clear();
				JOptionPane.showMessageDialog(null, "Press enter to begin", "Player 2", JOptionPane.PLAIN_MESSAGE);
				interval = 181;
				timer.start();
				return;
			} else {
				total2 = total;
				score1.setText("Score: " + total1);
				score2.setText("Score: " + total2);
				if (total1 > total2) {
					setStatus(3);

				} else if (total1 < total2) {
					setStatus(4);
				} else {
					setStatus(5);
				}
			}
		}
		wordTextField.setEnabled(false);
	}

	private void setStatus(int num) {
		switch (num) {
		case 1:
			status.setText("Good Luck!");
			break;
		case 2:
			status.setText("Times Up!");
			break;
		case 3:
			status.setText("Player 1 Wins!");
			break;
		case 4:
			status.setText("Player 2 Wins!");
			break;
		case 5:
			status.setText("Tie Game");
			break;
		case 6:
			status.setText("Player 2's Turn");
			break;
		case 7:
			status.setText("Player 1's Turn");
			break;
		}
	}

	public void resetCells() {
		for (JLabel[] element : boggleBoard) {
			for (JLabel element2 : element) {
				element2.setBorder(new LineBorder(Color.BLUE, 10, true));
			}
		}
	}

	public void resetBoard() {
		wordTextField.setEnabled(true);
		wordTextField.setText((""));
		wordListArea.setText("");
		words.clear();
		score1.setText("Score 1: 0");
		if (players == 1) {
			setStatus(1);
			score2.setVisible(false);
		} else {
			score2.setText("Score 2: 0");
			setStatus(7);
		}
		total = 0;
		turn = 1;
		interval = 181;
		fillBoard();
		timer.start();
	}

	public void appendWord(String word) {
		words.add(word);
		wordListArea.append(word.toUpperCase() + "\n");
		wordTextField.setText("");
	}

	public void addScore(int amt) {

		// it is only likely that the word will be from 3-8
		switch (amt) {
		case 3:
			total += 1;
			break;
		case 4:
			total += 2;
			break;
		case 5:
			total += 3;
			break;
		case 6:
			total += 4;
			break;
		case 7:
			total += 5;
			break;
		case 8:
			total += 6;
			break;
		}
		if (turn == 1) {
			score1.setText("Score 1: " + total);
		} else {
			score2.setText("Score 2: " + total);
		}
	}

	public void fillBoard() {

		boggle = logic.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col].setText(boggle[row][col].getValue());
				boggleBoard[row][col].setHorizontalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setVerticalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setFont(letterFont);
				boggleBoard[row][col].setForeground(Color.BLUE);
				boggleBoard[row][col].setBackground(Color.WHITE);
				boggleBoard[row][col].setOpaque(true);
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10, true));
				final int i = row;
				final int j = col;
				boggleBoard[row][col].addMouseListener(new MouseListener() {

					public void mouseClicked(MouseEvent arg0) {
						wordTextField.setText(wordTextField.getText() + logic.getValueOfCell(i, j));
						boggleBoard[i][j].setBorder(new LineBorder(Color.RED, 10, true));
					}

					public void mouseEntered(MouseEvent arg0) {
					}

					public void mouseExited(MouseEvent arg0) {
					}

					public void mousePressed(MouseEvent arg0) {
					}

					public void mouseReleased(MouseEvent arg0) {
					}
				});
			}
		}
	}

	public void rotateMatrixRight() {

		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				copy[c][4 - 1 - r] = boggle[r][c].getValue();

			}
		}
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[i].length; j++) {
				Cell cell = new Cell(i, j, copy[i][j]);
				boggle[i][j] = cell;
				boggleBoard[i][j].setText(copy[i][j]);
			}
		}
	}

	public void setPlayer(int players) {
		this.players = players;
	}

	public static void main(String[] args) {
		new BoggleFrame(1).setVisible(true);
	}

}
