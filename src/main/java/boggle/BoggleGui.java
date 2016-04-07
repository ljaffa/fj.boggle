package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class BoggleGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BoggleThread thread;
	private Logic log;
	private Cell[][] boggle;
	private Timer timer;
	private Font letterFont;

	private Container container;

	private JPanel boardPanel;
	private JPanel leftPanel;
	private JPanel topPanel;
	private JPanel rightPanel;
	private JPanel scorePanel;
	private JTextField wordTextField;
	private JTextArea wordListArea;
	private JLabel[][] boggleBoard;
	private JLabel timerLabel;
	private JLabel status;
	private JLabel imageLabel;
	private JLabel score1, score2;
	private JButton shuffle;
	private JButton rotate;
	private JButton pauseButton;
	private ImageIcon boggleIcon;

	private ArrayList<String> words;
	private String[][] copy;
	private int interval = 181;
	private int turn = 1, players = 2;
	private int total1, total2, total = 0;// starts off as zero
	private boolean paused;

	public BoggleGui() {
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

		// log = new Logic(wordLabel);
		log = new Logic();
		boggle = log.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);

			}
		}

		letterFont = new Font("Calibri", Font.BOLD, 50);
		boggleIcon = new ImageIcon(getClass().getResource("/boggleMessage.png"));

		words = new ArrayList<String>();
		copy = new String[4][4];
		paused = false; // default it to false
		turn = 1;

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
		wordTextField.setBackground(Color.ORANGE);
		wordTextField.setForeground(Color.BLUE);
		wordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		wordTextField.setPreferredSize(new Dimension(50, 40));
		wordTextField.setFont(font);

		score1.setFont(font);
		score1.setForeground(Color.WHITE);
		score2.setFont(font);
		score2.setForeground(Color.WHITE);

		status.setFont(font);
		status.setForeground(Color.WHITE);
		status.setText("hhhel");

		wordListArea.setBackground(Color.BLACK);
		wordListArea.setForeground(Color.WHITE);
		wordListArea.setFont(fontTwo);
		wordListArea.setEditable(false);
		wordListArea.setPreferredSize(new Dimension(200, 50));

		shuffle.setBackground(Color.ORANGE);
		shuffle.setForeground(Color.BLUE);
		shuffle.setFont(font);
		shuffle.setBorder(null);
		shuffle.setBorderPainted(false);
		shuffle.setFocusPainted(false);
		shuffle.setRolloverEnabled(false);

		rotate.setBackground(Color.ORANGE);
		rotate.setForeground(Color.BLUE);
		rotate.setFont(font);
		rotate.setBorder(null);
		rotate.setBorderPainted(false);
		rotate.setFocusPainted(false);
		rotate.setRolloverEnabled(false);

		pauseButton.setBackground(Color.ORANGE);
		pauseButton.setForeground(Color.BLUE);
		pauseButton.setFont(fontTwo);
		pauseButton.setBorder(null);
		pauseButton.setBorderPainted(false);
		pauseButton.setFocusPainted(false);
		pauseButton.setRolloverEnabled(false);
	}

	private void addActionListeners() {
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
				String word = wordTextField.getText().toLowerCase();
				if (word.length() == 0) {
					return;
				}
				wordTextField.setText("");
				boolean valid = false;

				if (words.contains(word)) {
					JOptionPane.showMessageDialog(null, "You already chose that word. Try again.", "BOGGLE",
							JOptionPane.PLAIN_MESSAGE, boggleIcon);
					return;
				}

				try {
					valid = log.checkWord(word);
				} catch (TooSmallWordException e) {
					JOptionPane.showMessageDialog(null, "The word is not at least 3 letters long.", "BOGGLE",
							JOptionPane.PLAIN_MESSAGE, boggleIcon);
					return;
				}

				if (valid) {
					thread = new BoggleThread(word, BoggleGui.this, wordTextField);
					thread.start();

				} else {
					JOptionPane.showMessageDialog(null, "This word does not exist in the board.", "BOGGLE",
							JOptionPane.PLAIN_MESSAGE, new ImageIcon("./boggleMessage.png"));

				}
			}
		});

	}

	/*
	 * private void addTimer() { int delay = 1000; int period = 1000; timer =
	 * new Timer(); timer.scheduleAtFixedRate(new TimerTask() {
	 * 
	 * @Override public void run() { if (!paused) timerLabel.setText("Timer: " +
	 * String.valueOf(checkTimer())); } }, delay, period); }
	 */

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
		JOptionPane.showMessageDialog(null, "Timer is up!!", "Timer", JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("./timer.gif"));
		if (players == 2) {
			if (turn == 1) {
				turn = 2;
				total1 = total;
				total = 0;
				score1.setText("Score: ???");
				wordListArea.setText("");
				words.clear();
				JOptionPane.showMessageDialog(null, "Press enter to begin", "Player 2", JOptionPane.PLAIN_MESSAGE);
				interval = 10;
				timer.start();
				return;
			} else {
				// remeber to switch
				total2 = total;
				score2.setText("Score: " + total2);
				score1.setText("Score: " + total1);
				if (total1 > total2) {
					status.setText("Player 1 wins");
				} else if (total1 < total2) {
					status.setText("Player 2 wins");
				} else {
					status.setText("Tie game");
				}
			}
		}
		wordTextField.setEnabled(false);
	}

	// excluding bord
	public void resetBoard() {
		wordTextField.setEnabled(true);
		wordListArea.setText("");
		words.clear();
		if (players == 1) {
			score2.setVisible(false);
		}
		score2.setText("Score: 0");
		score1.setText("Score: 0");
		total = 0;
		interval = 10;
		fillBoard();
		timer.start();
	}

	public void appendWord(String word) {
		words.add(word);
		wordListArea.append(word + "\n");
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
		if (turn ==1){
		score1.setText("Score 1: " + total);
		}
		else{
			score2.setText("Score 2: " + total);
		}
	}

	public void fillBoard() {

		boggle = log.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {

				boggleBoard[row][col].setText(boggle[row][col].getValue());
				boggleBoard[row][col].setHorizontalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setVerticalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setFont(letterFont);
				boggleBoard[row][col].setForeground(Color.BLUE);
				boggleBoard[row][col].setBackground(Color.YELLOW);
				boggleBoard[row][col].setOpaque(true);
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10, true));
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
		new BoggleGui().setVisible(true);
	}

}
