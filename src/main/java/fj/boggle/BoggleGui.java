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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class BoggleGui extends JFrame {

	private Logic log;
	private String[][] boggle;

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
	private JLabel[][] boggleBoard;
	private BoggleThread thread;
	private JPanel boardPanel;
	private Font letterFont;
	private ArrayList<String> words;
	private String[][] copy;

	public BoggleGui() {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		this.setIconImage(new ImageIcon("./frameLogo.jpg").getImage());
		boggleBoard = new JLabel[4][4];
		log = new Logic();

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		// make a grid layout and connect it to the logic class
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(4, 4));

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

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.BLUE);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		rightPanel.add(boardPanel, BorderLayout.CENTER);

		// container.add(boardPanel, BorderLayout.CENTER);
		container.add(rightPanel, BorderLayout.CENTER);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 35);

		imageLabel = new JLabel(new ImageIcon("boggle.png"));
		topPanel.add(imageLabel, BorderLayout.NORTH);
		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

		wordLabel = new JTextField();
		wordLabel.setOpaque(true);
		wordLabel.setBackground(Color.ORANGE);
		wordLabel.setForeground(Color.BLUE);

		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setPreferredSize(new Dimension(50, 40));
		wordLabel.setFont(font);

		timerLabel.setFont(font);
		timerLabel.setForeground(Color.WHITE);
		topPanel.add(timerLabel, BorderLayout.SOUTH);

		score = new JLabel("Score: " + total);
		score.setFont(font);
		score.setForeground(Color.WHITE);
		topPanel.add(score, BorderLayout.WEST);

		area = new TextArea();
		area.setBackground(Color.BLACK);
		area.setForeground(Color.WHITE);
		area.setFont(new Font("Berlin Sans FB", Font.PLAIN, 25));
		area.setEditable(false);
		area.setPreferredSize(new Dimension(200, 50));

		shuffle = new JButton("Shuffle Board!");
		shuffle.setBackground(Color.ORANGE);
		shuffle.setForeground(Color.BLUE);
		shuffle.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		rotate = new JButton("ROTATE");
		rotate.setBackground(Color.ORANGE);
		rotate.setForeground(Color.BLUE);
		rotate.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		rightPanel.add(rotate, BorderLayout.SOUTH);

		leftPanel.add(wordLabel, BorderLayout.SOUTH);
		leftPanel.add(area, BorderLayout.CENTER);
		leftPanel.add(shuffle, BorderLayout.NORTH);
		// leftPanel.add(rotate, BorderLayout.WEST);

		words = new ArrayList<String>();

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
		wordLabel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				boolean used = false;
				boolean valid = false;

				if (words.contains(wordLabel.getText())) {
					JOptionPane.showMessageDialog(null,
							"You already chose that word. Try again.","BOGGLE",
							JOptionPane.PLAIN_MESSAGE, new ImageIcon("./boggleMessage.png"));
					wordLabel.setText("");
					used = true;

				}

				if (!used) {
					try {
						valid = log.checkWord(wordLabel.getText());
					} catch (TooSmallWordException e) {
						JOptionPane.showMessageDialog(null,
								"The word is not at least 3 letters long.","BOGGLE",
								JOptionPane.PLAIN_MESSAGE, new ImageIcon("./boggleMessage.png"));
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

		int delay = 1000;
		int period = 1000;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				timerLabel.setText("Timer: " + String.valueOf(setInterval()));

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
					new ImageIcon("boggleMessage.png"));
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

				boggleBoard[row][col].setText(boggle[row][col]);
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
				copy[c][4 - 1 - r] = boggle[r][c];

			}
		}
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[i].length; j++) {
				boggle[i][j] = copy[i][j];
				boggleBoard[i][j].setText(copy[i][j]);
				System.out.print(copy[i][j] + " ");
			}
			System.out.println();
		}

	}

	public String[][] rotateMatrixLeft() {

		String[][] ret = new String[4][4];
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				ret[i][j] = boggle[j][4 - i - 1];
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
