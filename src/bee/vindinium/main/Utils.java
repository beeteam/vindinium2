package bee.vindinium.main;

import java.util.ArrayList;

import bee.gus.client.Board;
import bee.gus.client.State;

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
	
	private boolean isBlock(Board.Tile tile)
	{return !tile.equals(Board.Tile.AIR);}
	
	//construire le lab
	private boolean[][] buildMaze(State state)
	{
		Board board;
		
		board = state.game.board;
		
		int x = board.tiles.length;
		int y = board.tiles[0].length;
		
		boolean[][] maze = new boolean[x][y];
		for(int i=0;i<x;i++) for(int j=0;j<y;j++)
		maze[i][j] = !isBlock(board.tiles[i][j]);
		
		return maze;
	}
	//imprimer le laby
		private void printMaze(boolean[][] maze)
		{
			int x = maze.length;
			int y = maze[0].length;
			for(int i=0;i<x;i++) 
			{
				for(int j=0;j<y;j++)
				{
					String s = maze[i][j]?" ":"#";
					System.out.print(s);
				}
				System.out.println();
			}
		}
}
