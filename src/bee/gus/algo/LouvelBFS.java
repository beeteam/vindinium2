package bee.gus.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bee.gus.client.Board;
import bee.vindinium.main.Utils;

public class LouvelBFS {

	// private State state;
	Board board;
	private int[] start;
	private int[] end;
	HashMap<String, String> reverse = new HashMap<>();
	public ArrayList<int[]> queue = new ArrayList<>();
	public ArrayList<String> queueTmp = new ArrayList<String>();
	public HashSet<String> traited = new HashSet<String>();
	int cpt = 0;

	List<String> freeTiles = null;

	public LouvelBFS(Board board, int[] start, int[] end) {
		super();
		this.board = board;
		this.start = start;
		this.end = end;

		this.freeTiles = new ArrayList<>();
		freeTiles.add("  ");
	}

	ArrayList<int[]> getDistance() {
		ArrayList<int[]> resultat = new ArrayList<>();
		traited = new HashSet<>();
		if (recursive(resultat, start)) {
			resultat.add(start);
		}

		resultat.add(end);

		int[] tmp = null;
		String s = reverse.get(Utils.positionToString(end));
		tmp = Utils.stringToPosition(s);
		while (!equals(tmp, start)) {
			tmp = Utils.stringToPosition(reverse.get(Utils
					.positionToString(tmp)));
			System.out.println(Utils.positionToString(tmp));
			if (!equals(tmp, start)) {
				resultat.add(Utils.stringToPosition(reverse.get(Utils
						.positionToString(tmp))));
			}
		}
		Collections.reverse(resultat);
		return resultat;
	}

	public int[][] getDistanceArray() {
		ArrayList<int[]> list = getDistance();
		int[][] tab = new int[list.size()][2];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = list.get(i);
		}
		return tab;
	}

	public boolean recursive(ArrayList<int[]> resultat, int[] pos) {
		System.out.println("Position : " + Utils.positionToString(pos));
		if (equals(pos, this.end)) {
			resultat.add(pos);
			System.out
					.println("***********************************************************");
			return true;
		}

		ArrayList<int[]> v = getFreeVoisin(pos);
		for (int[] is : v) {
			if (!traited.contains(Utils.positionToString(is))) {
				addMap(Utils.positionToString(pos), Utils.positionToString(is));
				if (!queueTmp.contains(Utils.positionToString(is))) {
					queue.add(is);
					queueTmp.add(Utils.positionToString(is));
				}
				Utils.printList("queue : ", queue);
			}
		}

		this.traited.add(Utils.positionToString(pos));
		Utils.printSet("Traited : ", traited);
		int[] p = null;
		if (!queue.isEmpty()) {
			p = queue.remove(0);
			System.out.println("Traitement de : " + Utils.positionToString(p));
		} else {
			return true;
		}

		if (recursive(resultat, p)) {
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
		final int last = board.size - 1;

		int x = pos[0];
		int y = pos[1];

		if ((end[0] == x && end[1] == (y - 1))
				|| (end[0] == x && end[1] == (y + 1))
				|| (end[0] == (x - 1) && end[1] == y)
				|| (end[0] == (x + 1) && end[1] == y)) {
			voisin.add(end);
		}

		if ((y > 0 && freeTiles.contains(board.tiles[x][y - 1].toString()))) {
			voisin.add(new int[] { x, y - 1 });
		}

		if (y < last && freeTiles.contains(board.tiles[x][y + 1].toString())) {
			voisin.add(new int[] { x, y + 1 });
		}

		if (x > 0 && freeTiles.contains(board.tiles[x - 1][y].toString())) {
			voisin.add(new int[] { x - 1, y });
		}

		if (x < last && freeTiles.contains(board.tiles[x + 1][y].toString())) {
			voisin.add(new int[] { x + 1, y });
		}

		return voisin;
	}

	public void addMap(String key, String val) {
		reverse.put(val, key);
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
