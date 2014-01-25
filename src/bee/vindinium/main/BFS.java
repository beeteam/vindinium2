package bee.vindinium.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import vindinium.Board.Tile;

public class BFS {

	private State state;
	private int[] start;
	private int[] end;
	BidiMap<int[], int[]> bidiMap = new DualHashBidiMap();
	public ArrayList<int[]> queue = new ArrayList<>();
	public HashSet<String> traited = new HashSet<>();
	int cpt = 0;
	
	List<String> freeTiles = null;

	public BFS(State state, int[] start, int[] end) {
		super();
		this.state = state;
		this.start = start;
		this.end = end;

		this.freeTiles = new ArrayList<>();
		freeTiles.add("  ");
	}

	ArrayList<int[]> getDistance(int[] start, int[] end) {
		ArrayList<int[]> resultat = new ArrayList<>();
		traited = new HashSet<>();
		// queue.add(start);
		if (recursive(resultat, start)) {
			resultat.add(start);
		}

		BidiMap<int[], int[]> inverse = bidiMap.inverseBidiMap();
		resultat.add(end);
		int[] tmp = inverse.get(end);
		while (!equals(tmp, start)) {
			resultat.add(inverse.get(tmp));
			tmp = inverse.get(end);
		}
		resultat.add(start);
		Collections.reverse(resultat);
		return resultat;
	}

	public boolean recursive(ArrayList<int[]> resultat, int[] pos) {
		System.out.println("cpt : " + cpt++);
		this.traited.add(Utils.positionToString(pos));
		
//		Utils.printList("Traited : ", traited);
		if (equals(pos, this.end)) {
			resultat.add(pos);
			Utils.printList("Résultat : ", resultat);
			return true;
		}
		
		ArrayList<int[]> v = getFreeVoisin(pos);
		for (int[] is : v) {
			if (!traited.contains(is)) {
				bidiMap.put(pos, is);
				queue.add(is);
			}
		}

		int[] p = queue.remove(0);
		
		System.out.println("Traitement de : " + Utils.positionToString(p));
		
		if (recursive(resultat, p)) {
			resultat.add(p);
			return true;
		} else {
			return false;
		}
	}

	public boolean equals(int[] p1, int[] p2) {
		return p1[0] == p2[0] && p1[1] == p2[1];
	}

	public ArrayList<int[]> getFreeVoisin(int[] pos) {
		ArrayList<int[]> voisin = new ArrayList<>();
		final int last = state.game.board.size - 1;

		int x = pos[0];
		int y = pos[1];

		if (y > 0 && freeTiles.contains(state.game.board.tiles[x][y - 1].toString())) {
			voisin.add(new int[] { x, y - 1 });
		}

//		System.out.println(state.game.board.tiles[x][y + 1]);
		if (y < last && freeTiles.contains(state.game.board.tiles[x][y + 1].toString())) {
			voisin.add(new int[] { x, y + 1 });
		}

//		System.out.println(state.game.board.tiles[x - 1][y]);
		if (x > 0 && freeTiles.contains(state.game.board.tiles[x - 1][y].toString())) {
			voisin.add(new int[] { x - 1, y });
		}

//		System.out.println(state.game.board.tiles[x + 1][y]);
		if (x < last && freeTiles.contains(state.game.board.tiles[x + 1][y].toString())) {
			voisin.add(new int[] { x + 1, y });
		}

		return voisin;
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
