package fj.boggle;

public class NotWordException extends Exception {

	public NotWordException() {
		super("The word that was entered is not a word in the dictionary");

	}
}
