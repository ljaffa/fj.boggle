package boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.google.gson.Gson;

public class BoggleThread extends Thread {

	private final String word;
	private final BoggleGui frame;
	private boolean caught;
	private final JTextField text;

	public BoggleThread(String word, BoggleGui frame, JTextField text) {

		this.frame = frame;
		this.word = word;
		this.text = text;
		this.caught = false; // initialize to false
	}

	@Override
	public void run() {

		try {
			URL dictionaryURL = new URL("https://en.wiktionary.org/w/api.php?action=query&format=json&titles=" + word);
			HttpURLConnection connection = (HttpURLConnection) dictionaryURL.openConnection();
			InputStream input = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			Gson gson = new Gson();
			WordExistsJson wordExists = gson.fromJson(reader, WordExistsJson.class);

			if (wordExists.getQuery().getPages().containsKey(-1)) {
				JOptionPane.showMessageDialog(null, "The word that was entered was not in the dictionary.", "BOGGLE",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon("./boggleMessage.png"));
				caught = true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!caught) {
			frame.appendWord(word);
			int size = word.length();
			frame.addScore(size);
		}

	}
}
