package fj.boggle;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BoggleThread extends Thread {

	private String word;

	public BoggleThread(String word) {

		this.word = word;
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
			System.out.println("You did not enter a correct word.");

		}
		
		System.out.println(response.getBody());

	}

	public static void main(String[] args) {

		new BoggleThread("gjhkhky").start();
	}

}
