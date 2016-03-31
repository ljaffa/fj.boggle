package fj.boggle;

public class NotWordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotWordException() {
		super("The word that was entered is not a word in the dictionary");

	}
}
