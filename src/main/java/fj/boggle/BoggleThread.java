package fj.boggle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BoggleThread extends Thread {

	private String word;
	private BoggleGui frame;
	private boolean caught;
	private JTextField text;

	public BoggleThread(String word, BoggleGui frame, JTextField text) {

		this.frame = frame;
		this.word = word;
		this.text = text;
		this.caught = false; // initialize to false
	}

	@Override
	public void run() {

		StringBuilder builder = new StringBuilder();
		builder.append("https://wordsapiv1.p.mashape.com/words/");
		builder.append(word);
		builder.append("/definitions");

		HttpResponse<JsonNode> response = null;

		try {
			response = Unirest
					.get(builder.toString())
					.header("X-Mashape-Key",
							"tUX0EvhpmFmshGEJpal40dLinQHip1nvCqWjsnERTWgoGmbBcK")
					.header("Accept", "application/json").asJson();
		} catch (UnirestException e) {
			JOptionPane.showMessageDialog(null,
					"The word that was entered was not in the dictionary.",
					"BOGGLE", JOptionPane.PLAIN_MESSAGE, new ImageIcon(
							"./boggleMessage.png"));
			text.setText("");
			caught = true;

		}

		// System.out.println(response.getBody());
		if (!caught) {
			frame.appendWord(word);
			int size = word.length();
			frame.addScore(size);
		}

	}

	public static void main(String[] args) {

		// new BoggleThread("gjhkhky").start();
	}

}
