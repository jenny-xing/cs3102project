import java.io.*;
import java.util.*;

public class MagicGenerator {

	public static void main(String[] args) {
		for (int x = 5; x < 101; x++) {
			MagicGenerator tester = new MagicGenerator(x, 2);
			tester.generateMagic();
			tester.writeMagic();
		}

		MagicGenerator tester = new MagicGenerator(7, 2);
		tester.generateMagic();
		tester.writeMagic();
	}

	private static int order;
	private static int dimension;
	private HashMap<Integer, Integer> magicThing;

	public MagicGenerator(Integer o, Integer d) {
		order = o;
		dimension = d;
		magicThing = new HashMap<Integer, Integer>((int) Math.pow(0, d),
				(float) (1.0));
	}

	private HashMap<Integer, Integer> generateMagicSquare() {

		if (order < 3)
			throw new RuntimeException("invalid order for tic-tac-toe board.");

		// generation for odd orders
		if (order % 2 == 1) {
			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					// formula given by Wikipedia
					int value = order * ((row + col - 1 + order / 2) % order)
							+ ((row + 2 * col - 2) % order) + 1;
					magicThing.put(getKey(row - 1, col - 1), value);
				}
			}
			// printMagicSquare();
		}

		// generation for doubly even (divisible by 4) orders.
		// http://www.1728.org/magicsq2.htm
		if (order % 4 == 0) {
			// fills the "diagonals"
			int count = 1;
			final int smaller_order = order / 4;
			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					int key = getKey(row - 1, col - 1);
					// fills in the corner squares that are order/4 x order/4
					if ((col <= smaller_order && row <= smaller_order)
							|| (col > order - smaller_order && row > order
									- smaller_order)
							|| (col <= smaller_order && row > order
									- smaller_order)
							|| (col > order - smaller_order && row <= smaller_order)) {
						magicThing.put(key, count);
					}

					// fills the inner square that is order/2 x order/2
					if (col > smaller_order && col <= order - smaller_order
							&& row > smaller_order
							&& row <= order - smaller_order) {
						magicThing.put(key, count);
					}

					count++;
				}
			}

			// fills in the rest from the bottom right to top left
			count = 1;
			for (int row = order - 1; row >= 0; row--) {
				for (int col = order - 1; col >= 0; col--) {
					int key = getKey(row, col);
					if (!magicThing.containsKey(key)) {
						magicThing.put(key, count);
					}
					count++;
				}
			}
			// printMagicSquare();
		}

		// generation for singly even (divisible by 2 but not by 4) orders
		// http://www.1728.org/magicsq3.htm
		if (order % 2 == 0 && !(order % 4 == 0)) {

			final int smaller_order = order / 2;

			// fills in the board with smaller odd order magic squares
			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					// formula given by wikipedia
					int value = smaller_order
							* ((row + col - 1 + smaller_order / 2) % smaller_order)
							+ ((row + 2 * col - 2) % smaller_order) + 1;
					magicThing.put(getKey(row - 1, col - 1), value);
				}
			}

			// increments the quadrants so that they match the ACDB filling
			// pattern
			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					int key = getKey(row - 1, col - 1);
					int value = magicThing.get(key);
					int s2 = smaller_order * smaller_order;

					// quadrant A stays the same
					// quadrant B
					if (row > smaller_order && col > smaller_order)
						magicThing.put(key, value + s2);

					// quadrant C
					if (row <= smaller_order && col > smaller_order)
						magicThing.put(key, value + 2 * s2);

					// quadrant D
					if (row > smaller_order && col <= smaller_order)
						magicThing.put(key, value + 3 * s2);

				}
			}

			// swap (almost) ALL the values!

			// swaps right side values
			int rightWidth = (smaller_order - 3) / 2;

			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					if (col > order - rightWidth && row <= order / 2) {
						// unnecessary variables are unnecessary
						int key = getKey(row - 1, col - 1);
						int swapkey = key + (order * order) / 2;
						swapValues(key, swapkey);
					}
				}
			}

			// swaps left side values
			int leftWidth = rightWidth + 1;

			for (int row = 1; row <= order; row++) {
				for (int col = 1; col <= order; col++) {
					if (col <= leftWidth && row <= order / 2) {
						// unnecessary variables are unnecessary
						int key = getKey(row - 1, col - 1);
						int swapkey = key + (order * order) / 2;
						swapValues(key, swapkey);
					}
				}
			}
			// accounts for indent on left side values

			swapValues(getKey(leftWidth, 0), getKey(leftWidth, 0)
					+ (order * order) / 2);
			swapValues(getKey(leftWidth, 0) + leftWidth, getKey(leftWidth, 0)
					+ leftWidth + (order * order) / 2);
		}

		return magicThing;

	}

	private void swapValues(int x, int y) {
		int xval = magicThing.get(x);
		int yval = magicThing.get(y);
		magicThing.put(x, yval);
		magicThing.put(y, xval);
	}

	private static int getKey(int row, int col) {
		return order * row + col;
	}

	// the hashmap is written to a file named so: order_dimension.ser
	public void writeMagic() {
		String filename = order + "_" + dimension + ".txt";
		String filepath = "src/magic squares and cubes/" + filename;
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filepath), "utf-8"));
			for (int i = 0; i < TttBoard.pow(order, dimension); i++) {
				Integer value = magicThing.get(i);
				writer.write(value.toString());
				writer.write(" ");
			}
			// System.out.println("the magic thing is saved in " + filename);
		} catch (IOException ex) {
			System.out.println("Error writing magicThing");
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

	private void printMagicSquare() {
		System.out.println("Start MagicSquare");
		for (int row = 0; row < order; row++) {
			for (int col = 0; col < order; col++) {
				// System.out.println("(" + row + ", " + col + "): "
				// + magicSquare.get(getKey(row, col)));
				if (magicThing.containsKey(getKey(row, col))) {
					int value = magicThing.get(getKey(row, col));
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
		System.out.println("End MagicSquare");
	}

	// cube here meaning dimension>2
	private void printMagicCube() {
		System.out.println("this will not be implemented");
	}

	public void printMagic() {
		if (dimension == 2) {
			printMagicSquare();
		} else
			printMagicCube();

	}

	public HashMap<Integer, Integer> generateMagic() {
		if (dimension == 2) {
			return generateMagicSquare();
		}
		return null;
	}
}
