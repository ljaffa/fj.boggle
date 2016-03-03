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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BoggleGui extends JFrame {

	private JLabel timerLabel;
	private JLabel imageLabel;
	private int interval = 121;
	private Timer timer;
	private TextArea area;
	private JTextField wordLabel;

	public BoggleGui() {

		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.BLUE);

		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(4, 4));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.BLUE);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		container.add(mainPanel, BorderLayout.CENTER);
		container.add(boardPanel, BorderLayout.EAST);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 40);

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

		leftPanel.add(area, BorderLayout.WEST);
		leftPanel.add(wordLabel, BorderLayout.SOUTH);

		wordLabel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				area.append(wordLabel.getText() + "\n");
				wordLabel.setText("");
			}
		});

		int delay = 1000;
		int period = 1000;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				timerLabel.setText(String.valueOf(setInterval()));

			}
		}, delay, period);

	}

	private int setInterval() {
		if (interval == 1)
			timer.cancel();
		return --interval;
	}

	public static void main(String[] args) {
		new BoggleGui().setVisible(true);
	}

}
