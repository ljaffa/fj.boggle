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

	public void fillBoard() {
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

	public boolean checkWord(String word) {
		this.word = word;
		this.letters = word.toCharArray();
		stack = new Stack<Character>();

		int k = 0;
		String newWord = null;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].equals(letters[k])) {
					stack.push(board[i][j].charAt(0));
					k++;

					checkAround(i, j, k);
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		while (!stack.isEmpty()) {
			char let = stack.pop();
			builder.append(let);
		}
		if (word.equals(builder.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public void checkAround(int i, int j, int index) {
		// mark each cell as visited when push onto the stack
		int k = index;
		if (inBounds(i + 1, j) && board[i + 1][j].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i + 1, j, k);
		}
		if (inBounds(i + 1, j - 1) && board[i + 1][j - 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i + 1, j - 1, k);
		}
		if (inBounds(i + 1, j + 1) && board[i + 1][j + 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i + 1, j + 1, k);
		}
		if (inBounds(i, j - 1) && board[i][j - 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i, j - 1, k);
		}
		if (inBounds(i, j + 1) && board[i][j + 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i, j + 1, k);
		}
		if (inBounds(i - 1, j - 1) && board[i - 1][j - 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i - 1, j - 1, k);
		}
		if (inBounds(i - 1, j) && board[i - 1][j].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i - 1, j, k);
		}
		if (inBounds(i - 1, j + 1) && board[i - 1][j + 1].equals(letters[k])) {
			stack.push(board[i][j].charAt(0));
			k++;
			checkAround(i - 1, j + 1, k);
		}
	}

	public boolean inBounds(int i, int j) {
		if (i >= 0 && i < 4) {
			if (j >= 0 && j < 4) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Logic log = new Logic();
		log.fillBoard();
		log.printBoard();
		System.out.println("Enter a word");
		Scanner keyboard = new Scanner(System.in);
		String word = keyboard.nextLine();
		System.out.println(log.checkWord(word));
	}
}
