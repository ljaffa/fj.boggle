package fj.boggle;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Logic {

	private String[][] board;
	private int num;
	private String letter;

	private String word;
	private Stack<Character> stack;
	private char[] letters;
	private BoggleThread thread;

	public String[][] fillBoard() {

		// make random for the vowels

		board = new String[4][4];
		Random rand = new Random();

		String[] vowels = new String[4];
		int vowelIndex = 0;
		while (vowelIndex < 4) {
			int vowelNum = rand.nextInt(5 - 1) + 1;
			String vowel = null;
			switch (vowelNum) {
			case 1:
				vowel = "A";
				break;
			case 2:
				vowel = "E";
				break;
			case 3:
				vowel = "I";
				break;
			case 4:
				vowel = "O";
				break;
			case 5:
				vowel = "U";
				break;
			}
			vowels[vowelIndex++] = vowel;

			int randRow = rand.nextInt(4);
			int randCol = rand.nextInt(4);

			board[randRow][randCol] = vowel;
		}

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null) {
					num = rand.nextInt(91 - 65) + 65;
					if (num == 81) {
						letter = "QU";
					} else {
						letter = String.valueOf((char) num);
					}
					board[i][j] = letter;
				}
			}
		}
		return board;
	}

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public boolean checkWord(String word) throws TooSmallWordException {

		if (word.length() < 3) {
			throw new TooSmallWordException();
		}

		this.word = word;
		this.letters = word.toCharArray();
		stack = new Stack<Character>();
		int k = 0;
		boolean found = false;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j].equalsIgnoreCase("QU")) {
					if (letters[k] == 'q' || letters[k] == 'Q') {
						stack.push('Q');
						stack.push('U');
						k += 2;

						found = checkAround(i, j, k);
						if (found) {
							return foundWord();
						} else {
							k = 0;
							stack.clear();
						}

					}
				}
				if (k < letters.length
						&& board[i][j].equalsIgnoreCase(String
								.valueOf(letters[k]))) {

					stack.push(board[i][j].charAt(0));
					k++;

					// not sure what to do with the checkAround because we never
					// call it

					// checkAround(i, j, k);

					// if the word is found, break out of the loop
					// if not, need to check in the board if another first
					// letter exists
					found = checkAround(i, j, k);

					if (found) {
						return foundWord();
					} else {
						k = 0;
						stack.clear();
					}

				}
			}


		}
		
		JOptionPane.showMessageDialog(null, "This word does not exist in the board.");
		return foundWord();
	}

	public boolean checkAround(int i, int j, int index) {
		// mark each cell as visited when push onto the stack
		// figure out what to do with the "QU"

		int k = index;
		if (k >= letters.length) {
			return true;

		} else {
			if (inBoard(i + 1, j, k)) {

				stack.push(board[i + 1][j].charAt(0));
				k++;
				return checkAround(i + 1, j, k);
			} else {
				k = hasQ(i + 1, j, k);

			}
			if (inBoard(i + 1, j - 1, k)) {
				stack.push(board[i + 1][j - 1].charAt(0));
				k++;
				return checkAround(i + 1, j - 1, k);
			} else {
				k = hasQ(i + 1, j - 1, k);

			}
			if (inBoard(i + 1, j + 1, k)) {
				stack.push(board[i + 1][j + 1].charAt(0));
				k++;
				return checkAround(i + 1, j + 1, k);
			} else {
				k = hasQ(i + 1, j + 1, k);

			}
			if (inBoard(i, j - 1, k)) {
				stack.push(board[i][j - 1].charAt(0));
				k++;
				return checkAround(i, j - 1, k);
			} else {
				k = hasQ(i, j - 1, k);

			}
			if (inBoard(i, j + 1, k)) {
				stack.push(board[i][j + 1].charAt(0));
				k++;
				return checkAround(i, j + 1, k);
			} else {
				k = hasQ(i, j + 1, k);

			}
			if (inBoard(i - 1, j - 1, k)) {
				stack.push(board[i - 1][j - 1].charAt(0));
				k++;
				return checkAround(i - 1, j - 1, k);
			} else {
				k = hasQ(i - 1, j - 1, k);

			}
			if (inBoard(i - 1, j, k)) {
				stack.push(board[i - 1][j].charAt(0));
				k++;
				return checkAround(i - 1, j, k);
			} else {
				k = hasQ(i - 1, j, k);

			}
			if (inBoard(i - 1, j + 1, k)) {
				stack.push(board[i - 1][j + 1].charAt(0));
				k++;
				return checkAround(i - 1, j + 1, k);
			} else {
				k = hasQ(i - 1, j + 1, k);

			}
		}
		return false;
	}

	public boolean foundWord() {
		StringBuilder builder = new StringBuilder();
		while (!stack.isEmpty()) {
			char let = stack.firstElement();
			stack.remove(0);

			builder.append(let);

		}

		return word.equalsIgnoreCase(builder.toString());

	}

	public boolean inBounds(int i, int j) {
		if (i >= 0 && i < 4) {
			if (j >= 0 && j < 4) {
				return true;
			}
		}
		return false;
	}

	public boolean inBoard(int i, int j, int k) {
		if (inBounds(i, j)) {
			if (board[i][j].equalsIgnoreCase("QU")) {
				return false;
			} else {
				return board[i][j].equalsIgnoreCase(String.valueOf(letters[k]));
			}

		}

		return false;

	}

	public int hasQ(int i, int j, int k) {

		if(inBounds(i,j)){
		if (board[i][j].equalsIgnoreCase("QU")) {
			if (letters[k] == 'q' || letters[k] == 'Q') {
				stack.push('Q');
				stack.push('U');
				k += 2;
			}
		}
		}
		return k;
	}

	public void callThread() {
		// thread = new BoggleThread(word);
		// thread.start();
	}

	public static void main(String[] args) throws TooSmallWordException {
		Logic log = new Logic();
		log.fillBoard();
		log.printBoard();
		System.out.println("Enter a word");
		Scanner keyboard = new Scanner(System.in);
		String word = keyboard.nextLine();
		// log.startCode(word);
		System.out.println(log.checkWord(word));
	}
}
