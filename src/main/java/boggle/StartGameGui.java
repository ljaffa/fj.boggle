package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class StartGameGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton singleButton, doubleButton, rules, highScore;
	private JPanel optionsPanel;
	private int player;
	private BoggleGui boggle;

	public StartGameGui() throws IOException {

		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		PlayGamePanel panel = new PlayGamePanel();
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setBackground(Color.blue);
		container.add(panel, BorderLayout.CENTER);

		singleButton = new JButton("Single Player");
		singleButton.setPreferredSize(new Dimension(50, 40));
		singleButton.setBackground(Color.BLUE);
		singleButton.setForeground(Color.YELLOW);
		singleButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		doubleButton = new JButton("Double Player");
		doubleButton.setPreferredSize(new Dimension(50, 40));
		doubleButton.setBackground(Color.BLUE);
		doubleButton.setForeground(Color.YELLOW);
		doubleButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		rules = new JButton("Rules");
		rules.setPreferredSize(new Dimension(50, 40));
		rules.setBackground(Color.BLUE);
		rules.setForeground(Color.YELLOW);
		rules.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		highScore = new JButton("High Scores");
		highScore.setPreferredSize(new Dimension(50, 40));
		highScore.setBackground(Color.BLUE);
		highScore.setForeground(Color.YELLOW);
		highScore.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		optionsPanel.add(singleButton);
		optionsPanel.add(doubleButton);
		optionsPanel.add(rules);
		optionsPanel.add(highScore);
		container.add(optionsPanel, BorderLayout.SOUTH);

		InputStream in = new FileInputStream(new File(getClass().getResource("/music.wav").getPath()));
		AudioStream music = new AudioStream(in);
		AudioPlayer.player.start(music);
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg")).getImage());

		singleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				boggle = new BoggleGui(1);
				// boggle.setPlayer(1);
				boggle.setVisible(true);

			}
		});

		doubleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				boggle = new BoggleGui(2);
				// boggle.setPlayer(2);
				boggle.setVisible(true);

			}
		});
	}

	public static void main(String[] args) throws IOException {
		new StartGameGui().setVisible(true);
	}
}