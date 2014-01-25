package bee.vindinium.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import vindinium.Direction;
import vindinium.Board.Tile;

public class BFS {

	private State state;

	private int[] start;
	private int[] end;

	static final List<Tile> freeTiles = java.util.Arrays.asList(new Tile[] {
			Tile.AIR, Tile.FREE_MINE, Tile.TAVERN });

	public BFS(State state, int[] start, int[] end) {
		super();
		this.state = state;
		this.start = start;
		this.end = end;
	}

	ArrayList<int[]> getDistance(State state, int[] start, int[] end) {
		ArrayList<int[]> queue = new ArrayList<>();
		queue.add(start);
//		queue.addAll(getFreeVoisin(start));
		ArrayList<int[]> resultat = new ArrayList<>();
		if (recursive(queue, resultat, start)) {
			resultat.add(start);
		}
		return resultat;
	}

	public boolean recursive(ArrayList<int[]> queue, ArrayList<int[]> resultat, int[] pos) {
		
		if (equals(pos, this.end)) {
			resultat.add(pos);
			return false;
		}
		
		ArrayList<int[]> v = getFreeVoisin(pos);
//		for (int[] is : v) {
//			if (!queue.contains(is)) {
//				queue.add(is);
//			}
//		}
		for (int[] voisin : v) {
			
			if (recursive(queue, resultat, voisin)) {
				resultat.add(voisin);
				break;
			} else {
				if (equals(voisin, this.end)) {
					resultat.add(voisin);
					return false;
				}
			}
		}
		return true;
	}

	public boolean equals (int[] p1,int[] p2 ) {
		return p1[0]==p2[0] && p1[1]==p2[1];
	}
	
	public ArrayList<int[]> getFreeVoisin(int[] pos) {
		ArrayList<int[]> queue = new ArrayList<>();
		final int last = state.game.board.size - 1;

		int x = pos[0];
		int y = pos[1];

		if (y > 0 && freeTiles.contains(state.game.board.tiles[x][y - 1])) {
			queue.add(new int[] { x, y - 1 });
		}

		if (y < last && freeTiles.contains(state.game.board.tiles[x][y + 1])) {
			queue.add(new int[] { x, y + 1 });
		}

		if (x > 0 && freeTiles.contains(state.game.board.tiles[x - 1][y])) {
			queue.add(new int[] { x - 1, y });
		}

		if (x < last && freeTiles.contains(state.game.board.tiles[x + 1][y])) {
			queue.add(new int[] { x + 1, y });
		}

		return queue;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int[] getStart() {
		return start;
	}

	public void setStart(int[] start) {
		this.start = start;
	}

	public int[] getEnd() {
		return end;
	}

	public void setEnd(int[] end) {
		this.end = end;
	}

}
