package fj.boggle;

public class TooSmallWordException extends Exception {

	public TooSmallWordException() {
		super("The word that was entered was not 3 or more letters.");
	}
}
