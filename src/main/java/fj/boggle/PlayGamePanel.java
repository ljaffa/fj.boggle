package fj.boggle;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PlayGamePanel extends JPanel {

	private BufferedImage image;

	public PlayGamePanel() {

		try {
			InputStream in = PlayGamePanel.class
					.getResourceAsStream("./boggleImage.jpg");

			image = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setLayout(new BorderLayout());

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

	}

}
