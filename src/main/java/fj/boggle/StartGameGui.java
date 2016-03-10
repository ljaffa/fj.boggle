package fj.boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class StartGameGui extends JFrame {
	
	private JButton button;

	public StartGameGui() throws IOException{
		
	
	setTitle("BOGGLE");
	setSize(600, 700);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setResizable(false);

	Container container = getContentPane();
	container.setLayout(new BorderLayout());
	
	PlayGamePanel panel = new PlayGamePanel();

	container.add(panel, BorderLayout.CENTER);

	button = new JButton("PLAY GAME!");
	button.setPreferredSize(new Dimension(150, 70));
	button.setBackground(Color.BLUE);
	button.setForeground(Color.YELLOW);
	button.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

	container.add(button, BorderLayout.SOUTH);
	
	InputStream in = new FileInputStream("music.wav");

	AudioStream music = new AudioStream(in);

	AudioPlayer.player.start(music);
	
	this.setIconImage(new ImageIcon("./frameLogo.jpg").getImage());
	
	button.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {

			dispose();
			new BoggleGui().setVisible(true);

		}
	});

}
	public static void main(String[] args) throws IOException{
		new StartGameGui().setVisible(true);
	}
}