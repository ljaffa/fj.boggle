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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class BoggleGui extends JFrame {

	private Logic log;
	private String[][] boggle;

	private JLabel timerLabel;
	private JLabel imageLabel;
	private int interval = 121;
	private Timer timer;
	private TextArea area;
	private JTextField wordLabel;

	private JLabel[][] boggleBoard;
	private BoggleThread thread;

	public BoggleGui() {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		boggleBoard = new JLabel[4][4];
		log = new Logic();

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		

		// make a grid layout and connect it to the logic class
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(4, 4));

		boggle = log.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);
				boggleBoard[row][col].setText(boggle[row][col]);
			}
		}

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.BLUE);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		container.add(boardPanel, BorderLayout.CENTER);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 35);

		imageLabel = new JLabel(new ImageIcon("boggle.png"));
		topPanel.add(imageLabel, BorderLayout.NORTH);
		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

		wordLabel = new JTextField();
		wordLabel.setOpaque(true);
		wordLabel.setBackground(Color.YELLOW);
		wordLabel.setForeground(Color.BLUE);

		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setPreferredSize(new Dimension(50, 40));
		wordLabel.setFont(font);

		timerLabel.setFont(font);
		topPanel.add(timerLabel, BorderLayout.SOUTH);

		area = new TextArea();
		area.setBackground(Color.BLACK);
		area.setForeground(Color.WHITE);
		area.setFont(new Font("Berlin Sans FB", Font.PLAIN, 25));
		area.setEditable(false);
		area.setPreferredSize(new Dimension(200,50));

		leftPanel.add(wordLabel, BorderLayout.SOUTH);
		leftPanel.add(area, BorderLayout.WEST);
		

		wordLabel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				boolean valid = false;
				try {
					valid = log.checkWord(wordLabel.getText());
				} catch (TooSmallWordException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (valid) {
					
					thread = new BoggleThread(wordLabel.getText(), BoggleGui.this, wordLabel);
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
				timerLabel.setText(String.valueOf(setInterval()));

			}
		}, delay, period);

	}

	private int setInterval() {
		if (interval == 1) {
			timer.cancel();
		}
		return --interval;
	}
	
	public void appendWord(String word){
		area.append(word + "\n");
		wordLabel.setText("");
	}

	public static void main(String[] args) {
		new BoggleGui().setVisible(true);
	}

}
