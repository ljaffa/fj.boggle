package fj.boggle;

import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Logic {

	private Cell[][] board;
	private int num;
	private String letter;

	private String word;
	private Stack<Cell> stack;
	private char[] letters;
	private JTextField wordLabel;

	public Logic(JTextField wordLabel) {
		this.wordLabel = wordLabel;
	}

	public Cell[][] fillBoard() {

		// make random for the vowels

		board = new Cell[4][4];
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
			board[randRow][randCol] = new Cell(randRow, randCol, vowel);
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
					board[i][j] = new Cell(i, j, letter);
				}
			}
		}
		return board;
	}

	public boolean checkWord(String word) throws TooSmallWordException {

		if (word.length() < 3) {
			throw new TooSmallWordException();
		}

		this.word = word;
		this.letters = word.toCharArray();
		stack = new Stack<Cell>();

		int k = 0;
		boolean found = false;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j].getValue().equalsIgnoreCase("QU")) {
					if (letters[k] == 'q' || letters[k] == 'Q') {
						stack.push(board[i][j]);
						k += 2;

						board[i][j].setVisited(true);

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
						&& board[i][j].getValue().equalsIgnoreCase(
								String.valueOf(letters[k]))) {

					stack.push(board[i][j]);

					k++;
					board[i][j].setVisited(true);

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

		JOptionPane
				.showMessageDialog(null,
						"This word does not exist in the board.", "BOGGLE",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(
								"./boggleMessage.png"));
		wordLabel.setText("");
		return foundWord();
	}

	public boolean checkAround(int i, int j, int index) {

		int k = index;
		if (k >= letters.length) {
			return true;

		} else {
			if (inBoard(i + 1, j, k)) {
				stack.push(board[i + 1][j]);
				k++;
				return checkAround(i + 1, j, k);
			} else {
				k = hasQ(i + 1, j, k);

			}
			if (inBoard(i + 1, j - 1, k)) {
				stack.push(board[i + 1][j - 1]);
				k++;
				return checkAround(i + 1, j - 1, k);
			} else {
				k = hasQ(i + 1, j - 1, k);

			}
			if (inBoard(i + 1, j + 1, k)) {
				stack.push(board[i + 1][j + 1]);
				k++;
				return checkAround(i + 1, j + 1, k);
			} else {
				k = hasQ(i + 1, j + 1, k);

			}
			if (inBoard(i, j - 1, k)) {

				stack.push(board[i][j - 1]);
				k++;
				return checkAround(i, j - 1, k);
			} else {
				k = hasQ(i, j - 1, k);

			}
			if (inBoard(i, j + 1, k)) {

				stack.push(board[i][j + 1]);
				k++;
				return checkAround(i, j + 1, k);
			} else {
				k = hasQ(i, j + 1, k);

			}
			if (inBoard(i - 1, j - 1, k)) {

				stack.push(board[i - 1][j - 1]);
				k++;
				return checkAround(i - 1, j - 1, k);
			} else {
				k = hasQ(i - 1, j - 1, k);

			}
			if (inBoard(i - 1, j, k)) {

				stack.push(board[i - 1][j]);
				k++;
				return checkAround(i - 1, j, k);
			} else {
				k = hasQ(i - 1, j, k);

			}
			if (inBoard(i - 1, j + 1, k)) {

				stack.push(board[i - 1][j + 1]);
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
			Cell let = stack.firstElement();
			stack.remove(0);

			builder.append(let.getValue());

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
			if (board[i][j].getValue().equalsIgnoreCase("QU")
					&& !board[i][j].isVisited()) {
				return false;
			} else {
				if (k >= letters.length) {
					return false;
				} else {
					if (board[i][j].getValue().equalsIgnoreCase(
							String.valueOf(letters[k]))
							&& !board[i][j].isVisited()) {
						return true;
					} else {
						return false;
					}
				}
			}

		}

		return false;

	}

	public int hasQ(int i, int j, int k) {

		if (inBounds(i, j)) {
			if (board[i][j].getValue().equalsIgnoreCase("QU")) {
				if (letters[k] == 'q' || letters[k] == 'Q') {
					stack.push(board[i][j]);
					k += 2;
					board[i][j].setVisited(true);
				}
			}
		}
		return k;
	}

}
