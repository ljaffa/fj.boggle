package fj.boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class BoggleGui extends JFrame {

	private Logic log;
	private Cell[][] boggle;

	private JLabel timerLabel;
	private JLabel imageLabel;
	private int interval = 181;
	private Timer timer;
	private TextArea area;
	private JTextField wordLabel;
	private JLabel score;
	private int total = 0;// starts off as zero
	private JButton shuffle;
	private JButton rotate;
	private JButton pauseButton;
	private JLabel[][] boggleBoard;
	private BoggleThread thread;
	private JPanel boardPanel;
	private Font letterFont;
	private ArrayList<String> words;
	private String[][] copy;
	private JPanel leftPanel;
	private JPanel topPanel;
	private JPanel rightPanel;
	private Container container;
	private Boolean paused;
	private ImageIcon boggleIcon;

	public BoggleGui() {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		this.setIconImage(new ImageIcon("./frameLogo.jpg").getImage());
		boggleBoard = new JLabel[4][4];

		wordLabel = new JTextField();

		log = new Logic(wordLabel);

		container = getContentPane();

		// make a grid layout and connect it to the logic class
		boardPanel = new JPanel();

		// fillBoard();
		letterFont = new Font("Calibri", Font.BOLD, 50);

		boggle = log.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);

			}
		}

		copy = new String[4][4];

		fillBoard();

		topPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();

		imageLabel = new JLabel(new ImageIcon("boggle.png"));
		timerLabel = new JLabel();
		score = new JLabel("Score: " + total);
		area = new TextArea();
		shuffle = new JButton("Shuffle Board!");
		rotate = new JButton("ROTATE");
		pauseButton = new JButton("PAUSE");
		
		boggleIcon = new ImageIcon("./boggleMessage.png");

		words = new ArrayList<String>();

		paused = false; // default it to false

		format();
		addToPanels();
		addActionListeners();
		addTimer();

	}

	private void addToPanels() {
		topPanel.add(imageLabel, BorderLayout.NORTH);
		topPanel.add(timerLabel, BorderLayout.SOUTH);
		topPanel.add(score, BorderLayout.WEST);

		rightPanel.add(pauseButton, BorderLayout.NORTH);
		rightPanel.add(boardPanel, BorderLayout.CENTER);
		rightPanel.add(rotate, BorderLayout.SOUTH);

		leftPanel.add(wordLabel, BorderLayout.SOUTH);
		leftPanel.add(area, BorderLayout.CENTER);
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

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 35);

		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(font);
		timerLabel.setForeground(Color.WHITE);

		wordLabel.setOpaque(true);
		wordLabel.setBackground(Color.ORANGE);
		wordLabel.setForeground(Color.BLUE);
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setPreferredSize(new Dimension(50, 40));
		wordLabel.setFont(font);

		score.setFont(font);
		score.setForeground(Color.WHITE);

		area.setBackground(Color.BLACK);
		area.setForeground(Color.WHITE);
		area.setFont(new Font("Berlin Sans FB", Font.PLAIN, 30));
		area.setEditable(false);
		area.setPreferredSize(new Dimension(200, 50));

		shuffle.setBackground(Color.ORANGE);
		shuffle.setForeground(Color.BLUE);
		shuffle.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		rotate.setBackground(Color.ORANGE);
		rotate.setForeground(Color.BLUE);
		rotate.setFont(font);

		pauseButton.setBackground(Color.ORANGE);
		pauseButton.setForeground(Color.BLUE);
		pauseButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 30));
	}

	private void addActionListeners() {
		shuffle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				fillBoard();
				total = 0;
				score.setText("Score: " + total);

				interval = 181;
				area.setText("");
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
				JOptionPane
						.showMessageDialog(
								null,
								"The game is paused.\nClick resume to resume the game.",
								"BOGGLE", JOptionPane.PLAIN_MESSAGE,
								boggleIcon);

				paused = false;

			}
		});
		wordLabel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				boolean used = false;
				boolean valid = false;

				if (words.contains(wordLabel.getText())) {
					JOptionPane.showMessageDialog(null,
							"You already chose that word. Try again.",
							"BOGGLE", JOptionPane.PLAIN_MESSAGE, boggleIcon);
					wordLabel.setText("");
					used = true;

				}

				if (!used) {
					try {
						valid = log.checkWord(wordLabel.getText());
					} catch (TooSmallWordException e) {
						JOptionPane.showMessageDialog(null,
								"The word is not at least 3 letters long.",
								"BOGGLE", JOptionPane.PLAIN_MESSAGE,boggleIcon);
						wordLabel.setText("");
					}
				}

				if (valid) {

					thread = new BoggleThread(wordLabel.getText(),
							BoggleGui.this, wordLabel);
					thread.start();

				}
			}
		});
	}

	private void addTimer() {
		int delay = 1000;
		int period = 1000;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (!paused)
					timerLabel.setText("Timer: "
							+ String.valueOf(setInterval()));

			}
		}, delay, period);
	}

	private int setInterval() {
		if (interval == 1) {
			timer.cancel();
			JOptionPane.showMessageDialog(null, "Timer is up!!", "Timer",
					JOptionPane.PLAIN_MESSAGE, new ImageIcon("./timer.gif"));
			wordLabel.setEnabled(false);
			int reply = JOptionPane.showConfirmDialog(null, "Your score is "
					+ total + ".\nDo you want to play again?", "PLAY AGAIN",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
					boggleIcon);
			if (reply == JOptionPane.YES_OPTION) {
				restartGame();
			} else {
				JOptionPane.showMessageDialog(null,
						"Good Bye! Have a great day!", "Good Bye",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(
								"./goodbye.png"));
				dispose();
			}

		}
		return --interval;
	}

	public void appendWord(String word) {
		words.add(word);
		area.append(word + "\n");
		wordLabel.setText("");
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

		score.setText("Score: " + total);
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
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10,
						true));
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
				boggle[i][j].setValue(copy[i][j]);
				System.out.print(copy[i][j] + " ");
			}
			System.out.println();
		}

	}

	public String[][] rotateMatrixLeft() {

		String[][] ret = new String[4][4];
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				ret[i][j] = boggle[j][4 - i - 1].getValue();
			}
		}
		return ret;
	}

	public void restartGame() {
		dispose();
		new BoggleGui().setVisible(true);
	}

	public static void main(String[] args) {
		new BoggleGui().setVisible(true);
	}

}
