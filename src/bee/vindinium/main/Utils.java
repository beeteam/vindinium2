package bee.vindinium.main;

import java.util.ArrayList;

public class Utils {
	
	static String positionToString(int[] p) {
		return "[" + p[0] + "," + p[1] + "]";
	}

	static void printList(String title, ArrayList<int[]> list) {
		System.out.print(title);
		for (int[] is : list) {
			System.out.print(is[0] + "," + is[1]+" - ");
		}
		System.out.print("\n");
	}
	
	static Direction direction(int[] start, int[] next)
	{
		if(next[0] == start[0]+1) return Direction.SOUTH;
		if(next[0] == start[0]-1) return Direction.NORTH;
		if(next[1] == start[1]+1) return Direction.EAST;
		if(next[1] == start[1]-1) return Direction.WEST;
		return null;
	}
}
