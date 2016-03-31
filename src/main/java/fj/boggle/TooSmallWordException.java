package fj.boggle;

public class TooSmallWordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TooSmallWordException() {
		super("The word that was entered was not 3 or more letters.");
	}
}
