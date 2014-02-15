/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 */

import java.util.*;

public class tttBoard {

	int size;
	int magicNum;
	String player0;
	String player1;
	HashMap<Integer, String> boardMap;
	HashMap<Integer, Integer> perfectSquare;
	TreeSet<Integer> player0Spots;
	TreeSet<Integer> player1Spots;

	public static void main(String args[]) {
		tttBoard testBoard = new tttBoard(4, "x", "o");
	}

	public tttBoard(Integer size, String p0, String p1) {
		this.size = size;
		magicNum = size * (size * size + 1) / 2;
		player0 = p0;
		player1 = p1;
		boardMap = new HashMap<Integer, String>(size * size, (float) 1.0);
		perfectSquare = new HashMap<Integer, Integer>(size * size, (float) 1.0);
		loadPerfectSquare();
	}

	// load Integer values into the correct values in the perfectSquare map
	private void loadPerfectSquare() {

		if (size < 3)
			throw new RuntimeException("invalid size for tic-tac-toe board.");

		// generation for odd sizes
		if (size % 2 == 1) {
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					// formula given by wikipedia
					int num = size * ((row + col - 1 + size / 2) % size)
							+ ((row + 2 * col - 2) % size) + 1;
					perfectSquare.put(getKey(row - 1, col - 1), num);
				}
			}
			// printMagicSquare();
		}

		// generation for double even (divisible by 4) sizes. see
		// http://www.1728.org/magicsq2.htm
		if (size % 4 == 0) {

			// fills the "diagonals"
			int count = 1;
			final int smaller_size = size / 4;
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					int key = getKey(row - 1, col - 1);
					// fills in the corner squares that are size/4 x size/4
					if ((col <= smaller_size && row <= smaller_size)
							|| (col > size - smaller_size && row > size
									- smaller_size)
							|| (col <= smaller_size && row > size
									- smaller_size)
							|| (col > size - smaller_size && row <= smaller_size)) {
						perfectSquare.put(key, count);
					}

					// fills the inner square that is size/2 x size/2
					if (col > smaller_size && col <= size - smaller_size
							&& row > smaller_size && row <= size - smaller_size) {
						perfectSquare.put(key, count);
					}

					count++;
				}
			}

			// fills in the rest from the bottom right to top left
			count = 1;
			for (int row = size - 1; row >= 0; row--) {
				for (int col = size - 1; col >= 0; col--) {
					int key = getKey(row, col);
					if (!perfectSquare.containsKey(key)) {
						perfectSquare.put(key, count);
					}
					count++;
				}
			}
			// printMagicSquare();
		}

		// TODO implement for single even sizes http://www.1728.org/magicsq3.htm
	}

	// params row and col: coordinates within the square (0 to
	// size^2-1)
	// return int key corresponding to the key in the maps for those coords
	private int getKey(int row, int col) {
		return size * row + col;
	}

	// param player: 0 or 1, the player number
	// params row and col: coordinates within the square
	// return boolean true if move successful, false otherwise
	private boolean move(int player, int row, int col) {
		if (boardMap.containsKey(getKey(row, col))) { // already occupied
			return false;
		}
		if (player != 0 && player != 1) { // invalid player
			return false;
		}
		if (row >= size || col >= size) { // invalid coords
			return false;
		}
		// valid move
		boardMap.put(getKey(row, col), player == 1 ? player1 : player0);
		if (player == 1) {
			player1Spots.add(perfectSquare.get(getKey(row, col)));
		} else {
			player0Spots.add(perfectSquare.get(getKey(row, col)));
		}
		return true;
	}

	// check for win or draw conditions
	private void check() {
		if (player0Spots.size() < size && player1Spots.size() < size)
			return; // not possible that someone has won
		int[] nPair = new int[size];
		int[] nIndices = new int[size];
		ArrayList<Integer> player0SpotsList = new ArrayList<Integer>(
				player0Spots);
		for (int i = 0; i < player0SpotsList.size() - size; i++) {
			// loop over elements in the list, grabbing each possible
			// combination of size spots and seeing if they sum to the magic
			// number
			for (int j = 0; j < size - 1; j++) { // assign indices
				nIndices[j] = i + j;
				nPair[j] = player0SpotsList.get(nIndices[j]);
			} // indices default to first unchecked size elements
			while (true) {
				int sum = sum(nPair);
				if (sum == magicNum) {
					win(player0);
					return;
				} else if (sum > magicNum)
					break; // continue to next starting digit
				else { // re-assign indices and nPair
					if (nIndices[size - 1] == player0SpotsList.size())
						break; // no more nPairs with this first value
					for (int j = size - 1; j >= 0; j--) {
						nIndices[j]++;
						nPair[j] = player0SpotsList.get(nIndices[j]);
					}
				}
			}
		}
		ArrayList<Integer> player1SpotsList = new ArrayList<Integer>(
				player1Spots);
		for (int i = 0; i < player1SpotsList.size() - size; i++) {
			// loop over elements in the list, grabbing each possible
			// combination of size spots and seeing if they sum to the magic
			// number
			for (int j = 0; j < size - 1; j++) { // assign indices
				nIndices[j] = i + j;
				nPair[j] = player1SpotsList.get(nIndices[j]);
			} // indices default to first unchecked size elements
			while (true) {
				int sum = sum(nPair);
				if (sum == magicNum) {
					win(player1);
					return;
				} else if (sum > magicNum)
					break; // continue to next starting digit
				else { // re-assign indices and nPair
					if (nIndices[size - 1] == player1SpotsList.size())
						break; // no more nPairs with this first value
					for (int j = size - 1; j >= 0; j--) {
						nIndices[j]++;
						nPair[j] = player1SpotsList.get(nIndices[j]);
					}
				}
			}
		}

		// TODO: check for draw
		// if (boardMap.size() < )
	}

	private int sum(int[] nPair) {
		int retval = 0;
		for (int i = 0; i < size; i++) {
			retval += nPair[i];
		}
		return retval;
	}

	// perform the winning operation given the winner
	private void win(String winner) {

	}

	// prints magic square in row-major oder
	private void printMagicSquare() {
		System.out.println("start");
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				// System.out.println("(" + row + ", " + col + "): "
				// + perfectSquare.get(getKey(row, col)));
				if (perfectSquare.containsKey(getKey(row, col))) {
					int value = perfectSquare.get(getKey(row, col));
					System.out.print(value);
					if (value < 10000)
						System.out.print(" ");
					if (value < 1000)
						System.out.print(" ");
					if (value < 100)
						System.out.print(" ");
					if (value < 10)
						System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("end");
	}
}