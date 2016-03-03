package fj.boggle;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Logic {

	private String[][] board;
	private int num;
	private String letter;

	private String word;
	private Stack<Character> stack;
	private char[] letters;
	private BoggleThread thread;

	public void fillBoard() {

		// make random for the vowels

		board = new String[4][4];
		Random rand = new Random();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
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

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void startCode(String word) throws TooSmallWordException {

		if (word.length() < 3) {
			throw new TooSmallWordException();
		}
		checkWord(word);
		callThread();
	}

	public boolean checkWord(String word) {
		this.word = word;
		this.letters = word.toCharArray();
		stack = new Stack<Character>();
		int k = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (k < letters.length
						&& board[i][j].equalsIgnoreCase(String
								.valueOf(letters[k]))) {
					stack.push(board[i][j].charAt(0));
					k++;

				}
			}
		}

		return foundWord();

	}

	public void checkAround(int i, int j, int index) {
		// mark each cell as visited when push onto the stack
		// figure out what to do with the "QU"

		int k = index;

		if (k < letters.length) {

			if (inBounds(i + 1, j)
					&& board[i + 1][j].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i + 1][j].charAt(0));
				k++;
				checkAround(i + 1, j, k);
			}
			if (inBounds(i + 1, j - 1)

			&& board[i + 1][j - 1].equalsIgnoreCase(String.valueOf(letters[k]))) {
				stack.push(board[i + 1][j - 1].charAt(0));
				k++;
				checkAround(i + 1, j - 1, k);
			}
			if (inBounds(i + 1, j + 1)

			&& board[i + 1][j + 1].equalsIgnoreCase(String.valueOf(letters[k]))) {
				stack.push(board[i + 1][j + 1].charAt(0));
				k++;
				checkAround(i + 1, j + 1, k);
			}
			if (inBounds(i, j - 1)
					&& board[i][j - 1].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i][j - 1].charAt(0));
				k++;
				checkAround(i, j - 1, k);
			}
			if (inBounds(i, j + 1)
					&& board[i][j + 1].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i][j + 1].charAt(0));
				k++;
				checkAround(i, j + 1, k);
			}
			if (inBounds(i - 1, j - 1)
					&& board[i - 1][j - 1].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i - 1][j - 1].charAt(0));
				k++;
				checkAround(i - 1, j - 1, k);
			}
			if (inBounds(i - 1, j)
					&& board[i - 1][j].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i - 1][j].charAt(0));
				k++;
				checkAround(i - 1, j, k);
			}
			if (inBounds(i - 1, j + 1)
					&& board[i - 1][j + 1].equalsIgnoreCase(String
							.valueOf(letters[k]))) {
				stack.push(board[i - 1][j + 1].charAt(0));
				k++;
				checkAround(i - 1, j + 1, k);
			}
		}
	}

	public boolean foundWord() {
		StringBuilder builder = new StringBuilder();
		while (!stack.isEmpty()) {
			char let = stack.firstElement();
			stack.remove(0);

			builder.append(let);

		}

		return (word.equalsIgnoreCase(builder.toString()));

	}

	public boolean inBounds(int i, int j) {
		if (i >= 0 && i < 4) {
			if (j >= 0 && j < 4) {
				return true;
			}
		}
		return false;
	}

	public void callThread() {
		thread = new BoggleThread(word);
		thread.start();
	}

	public static void main(String[] args) throws TooSmallWordException {
		Logic log = new Logic();
		log.fillBoard();
		log.printBoard();
		System.out.println("Enter a word");
		Scanner keyboard = new Scanner(System.in);
		String word = keyboard.nextLine();
		log.startCode(word);
		System.out.println(log.checkWord(word));
	}
}
