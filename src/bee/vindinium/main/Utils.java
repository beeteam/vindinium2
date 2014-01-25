package bee.vindinium.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.BidiMap;


public class Utils {

	public static String positionToString(int[] p) {
		return "[" + p[0] + "," + p[1] + "]";
	}

	public static void printSet(String title, HashSet<String> list) {
		System.out.print(title);
		for (String is : list) {
			System.out.print(is);
		}
		System.out.print("\n");
	}

	public static void printList(String title, ArrayList<int[]> list) {
		System.out.print(title);
		for (int[] is : list) {
			System.out.print("[" + is[0] + "," + is[1] + "]");
		}
		System.out.print("\n");
	}

	public static Direction direction(int[] start, int[] next) {
		if (next[0] == start[0] + 1)
			return Direction.SOUTH;
		if (next[0] == start[0] - 1)
			return Direction.NORTH;
		if (next[1] == start[1] + 1)
			return Direction.EAST;
		if (next[1] == start[1] - 1)
			return Direction.WEST;
		return Direction.STAY;
	}

	public static boolean isBlock(Board.Tile tile) {
		return !tile.equals(Board.Tile.AIR);
	}

	// construire le lab
	public static boolean[][] buildMaze(State state) {
		Board board;

		board = state.game.board;

		int x = board.tiles.length;
		int y = board.tiles[0].length;

		boolean[][] maze = new boolean[x][y];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				maze[i][j] = !isBlock(board.tiles[i][j]);

		return maze;
	}

	// imprimer le laby
	public static void printMaze(boolean[][] maze) {
		int x = maze.length;
		int y = maze[0].length;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				String s = maze[i][j] ? " " : "#";
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
	public static void printBidi(BidiMap<int[], int[]> bidiMap) {
		for (Iterator iterator = bidiMap.mapIterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			System.out.println(Utils.positionToString(is));
		}
	}
	
	public static void printMap(HashMap<String, List<String>> map) {
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String is = (String) iterator.next();
			ArrayList<String> s = (ArrayList<String>) map.get(is);
			System.out.print("key : " + is);
			for (String string : s) {
				System.out.print("value : " + string);
			}
		}
	}
	
	public static int[] stringToPosition(String pos) {
		String x = extractForRegex(pos, "\\[(.*),", false);
		String y = extractForRegex(pos, ",(.*)\\]", false);
		return new int[] {Integer.valueOf(x), Integer.valueOf(y)};
	}
	
	public static String extractForRegex(String text, String regex, boolean multiline) {
		Pattern pattern = null;
		if (multiline) {
			pattern = Pattern.compile(regex, Pattern.MULTILINE);
		} else {
			pattern = Pattern.compile(regex);
		}
		Matcher matcher = null;
		matcher = pattern.matcher(text.trim());

		boolean matchFound = matcher.find();

		if (matchFound) {
			if (matcher.groupCount() == 1) {
				return matcher.group(1);
			} else {
				return "";
			}
		}

		return "";
	}
	
}
