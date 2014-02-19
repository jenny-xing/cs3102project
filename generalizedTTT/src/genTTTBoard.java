/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 * 
 * Class for an n-dimensional tic-tac-toe board using magic n-cubes to determine winner
 */

import java.util.*;

public class genTTTBoard {

	private int size;
	private int magicNum;
	private int dimension;
	private String player0;
	private String player1;
	private HashMap<Integer, String> boardMap;
	private HashMap<Integer, Integer> magicCube;
	private TreeSet<Integer> player0Spots;
	private TreeSet<Integer> player1Spots;
	private TreeSet<Integer> openSpots;
	private TreeSet<Integer> player0Pairs;
	private TreeSet<Integer> player1Pairs;
	
	final int MAX_SPACES = 1000000;

	public static void main(String args[]) {

	}

	public genTTTBoard(Integer size, Integer dim, String p0, String p1) {
		this.size = size;
		dimension = dim;
		magicNum = (size * (pow(size,dimension) + 1)) / 2;
		player0 = p0;
		player1 = p1;
		boardMap = new HashMap<Integer, String>(pow(size,dim), (float) 1.0);
		magicCube = new HashMap<Integer, Integer>(pow(size,dim), (float) 1.0);
		loadMagicCube();
		player0Spots = new TreeSet<Integer>();
		player1Spots = new TreeSet<Integer>();
		openSpots = new TreeSet<Integer>();
		loadOpenSpots();
		player0Pairs = new TreeSet<Integer>();
		player1Pairs = new TreeSet<Integer>();
	}
	
	public int getSize() { return size; }
	
	public HashMap<Integer, String> getBoardMap() { return boardMap; }
	
	public int getDimension() { return dimension; }
	
	//simple method to return x^exp for ints
	private int pow(int x, int exp) {
		int retval = 1;
		while (true) {
			if (exp == 0) break;
			retval *= x;
			exp--;
		}
		return retval;
	}
	
	//load all possible key values into openSpots set
	private void loadOpenSpots() {
		for (int i=0; i<pow(size, dimension); i++) {
			openSpots.add(i);
		}
	}

	// load Integer values into the correct values in the magicSquare map
	//TODO: implement for n-dimensions
	private void loadMagicCube() {

	}

	// param coords: coordinates within the n-cube
	// return int key corresponding to the key in the maps for those coords
	private int getKey(int[] coords) {
		int retval = 0;
		for (int i=0; i<dimension; i++) {
			retval += pow(size,i)*coords[i];
		}
		return retval;
	}

	// param player: 0 or 1, the player number
	// param coords: coordinates within the n-cube
	// return boolean true if move successful, false otherwise
	private boolean move(int player, int[] coords) {
//TODO
		return false;
	}
	
	//update pairs set for player to include all new possible pairs
	//if the last move was a winning one, call win() method
	//NOTE: the current move has NOT been added to the spots set
	private boolean updatePairs(int key, int player) {
//TODO
		return false;
	}

	// check for win or draw conditions
	// update the relevant player pairs set to include pairs wit
	//NOTE: the current move HAS been added to relevant pairs set
	//param player: 0 or 1, the player number
	private boolean check(int player) {
		if (player != 0 && player != 1) { // invalid player
			return false;
		}
		//checkWin() // deprecated
		TreeSet<Integer> pairs = (player == 0) ? player0Pairs : player1Pairs;
		String winner = (player == 0) ? player0 : player1; //not necessarily the winner yet
		if (pairs.contains(magicNum)) { //should be redundant
			win(winner);
			return true;
		}
		checkDraw();
		return true;
	}
	
	
	//method to check for draw condition
	//TODO: currently only checks that all spaces are filled
	//TODO: draw condition can occur before this is true
	private void checkDraw() {
		if (openSpots.size() == 0)
			draw();
	}
	
	//perform draw operation
	private void draw() {
		
	}

	//sum the array of size elements
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

	// x and y are the keys
	private void swapValues(int x, int y) {
		int xval = magicCube.get(x);
		int yval = magicCube.get(y);
		magicCube.put(x, yval);
		magicCube.put(y, xval);
	}
	
	
}
