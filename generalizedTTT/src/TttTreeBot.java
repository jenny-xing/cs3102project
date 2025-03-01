import java.util.*;

/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 * 
 *         A class that implements an AI to play tic-tac-toe with 2 players. A
 *         clever bot that searches in the game space for up to 1000
 *         possibilities before deciding where to move.
 * 
 *         If the entire game space is < 1000, this bot will play perfectly.
 * 
 */
public class TttTreeBot implements TttBot {

	private TttBoard board;
	private TttGameTree tree;
	private int id; // the player # of this object
	private int prev;

	public TttTreeBot(TttBoard board, int id) {
		tree = new TttGameTree(board, id);
		this.id = id;
		this.board = board;
	}

	@Override
	public int move() {
		int ret = tree.findBestMove();
		if (ret != -1)
			return ret;
		else
			return randomMove();
	}

	/**
	 * Because we are using HashSet, this will be an expensive O(n) operation.
	 * 
	 * @param openSpots
	 * @return int representing random move
	 */
	private int randomMove(HashSet<Integer> openSpots) {
		Random myRand = new Random();
		while (true) {
			int next = myRand.nextInt(TttBoard
					.pow(board.order, board.dimension));
			if (openSpots.contains(next))
				return next;
		}
	}

	@Override
	public int randomMove() {
		HashSet<Integer> openSpots = board.getOpenSpots();
		return randomMove(openSpots);
	}

	@Override
	public int firstMove() {
		HashSet<Integer> openSpots = board.getOpenSpots();
		if (board.order % 2 == 1) // there is only one center spot
		{
			int center = TttBoard.pow(board.order, board.dimension) / 2;
			if (openSpots.contains(center))
				return center;
		} else { // there are multiple "center" spots
			int dimCoord = board.order / 2;
			int[] coords = new int[board.dimension];
			for (int i = 0; i < board.dimension; i++)
				coords[i] = dimCoord;
			int key = board.getKey(coords);
			// note the following could be much more elegant
			// also, it only works for 3d
			if (openSpots.contains(key))
				return key;
			key++;
			coords[0]++;
			if (openSpots.contains(key))
				return key;
			coords[0]--;
			coords[1]++;
			key += board.dimension - 1;
			if (openSpots.contains(key))
				return key;
			coords[0]++;
			key++;
			if (openSpots.contains(key))
				return key;
			if (board.dimension > 2) { // ie. 3d
				coords[2]++;
				coords[0]--;
				coords[1]--;
				key = board.getKey(coords);
				if (openSpots.contains(key))
					return key;
				key++;
				coords[0]++;
				if (openSpots.contains(key))
					return key;
				coords[0]--;
				coords[1]++;
				key += board.dimension - 1;
				if (openSpots.contains(key))
					return key;
				coords[0]++;
				key++;
				if (openSpots.contains(key))
					return key;
			}
		}
		return randomMove(openSpots);
	}

	@Override
	public void registerMove(int key) {
		prev = key;
		updateTree();
	}

	// Update the game tree using the value of prev
	// to specify the last move that was made.
	private void updateTree() {
		if (prev == -1)
			return;
		tree = tree.selectChild(prev);
	}

}