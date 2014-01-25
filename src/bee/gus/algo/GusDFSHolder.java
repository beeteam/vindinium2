package bee.gus.algo;

import bee.gus.client.Board;

public class GusDFSHolder {

	private GusDFS dfs;
	private Board board;
	private int[] start;
	private int[] end;
	
	
	public GusDFSHolder(Board board, int[] start, int[] end)
	{
		this.board = board;
		this.start = start;
		this.end = end;
				
		dfs = new GusDFS();
	}
	
	
	
	public int[][] getDistanceArray() throws Exception
	{
		boolean[][] maze = buildMaze();
		maze[start[0]][start[1]] = true;
		maze[end[0]][end[1]] = true;
		
		//printMaze(maze);
		
		dfs.register("maze",maze);
		dfs.register("start",start);
		dfs.register("end",end);
		dfs.run();
		
		return (int[][]) dfs.retrieve("path");
	}
	
	
	
	private boolean[][] buildMaze()
	{
		int x = board.tiles.length;
		int y = board.tiles[0].length;
		
		boolean[][] maze = new boolean[x][y];
		for(int i=0;i<x;i++) for(int j=0;j<y;j++)
		maze[i][j] = !isBlock(board.tiles[i][j]);
		
		return maze;
	}
	
	
	
	
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
	
	
	
	
	
	
	private boolean isBlock(Board.Tile tile)
	{
		if(tile==null) return true;
		return !tile.equals(Board.Tile.AIR);
	}
}
