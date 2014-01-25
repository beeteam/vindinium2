package bee.gus.bots;

import java.util.List;

import bee.gus.algo.GusDFS;
import bee.gus.client.Board;
import bee.gus.client.Bot;
import bee.gus.client.Direction;
import bee.gus.client.Hero;
import bee.gus.client.State;


public class GusBot1 implements Bot {
	
	
	private Board board;
	private Hero me;
	private int[] me_;
	
	
	
	private GusDFS dfs;
	
	private boolean running = false;
	private int[][] path;
	private int index;
	
	
	public GusBot1()
	{dfs = new GusDFS();}
	
	
	
	

	public Direction nextMove(State state)
	{
		try
		{
			board = state.game.board;
			me = state.hero();
			me_ =  heroToIntArray(me);
			
			
			
			if(!running)
			{
				boolean[][] maze = buildMaze();
				int[] start = me_;
				int[] end = seachMine();
				
				maze[start[0]][start[1]] = true;
				maze[end[0]][end[1]] = true;
				
				printMaze(maze);
				
				dfs.register("maze",maze);
				dfs.register("start",start);
				dfs.register("end",end);
				dfs.run();
				
				path = (int[][]) dfs.retrieve("path");
				
				System.out.println("running DFS");
				System.out.println("start: "+toString(start));
				System.out.println("end: "+toString(end));
				System.out.println("path length: "+path.length);
				System.out.println();
				
				index = 0;
				running = true;
			}
			
			
			if(running)
			{
				if(index==path.length-1) return Direction.STAY;
					
				int[] p0 = path[index];
				int[] p1 = path[index+1];
				
				Direction d = direction(p0,p1);
				
				System.out.print("index: "+index+" ");
				System.out.print("p0: "+toString(p0)+" ");
				System.out.print("p1: "+toString(p1)+" ");
				System.out.println("direction: "+d.name);
				
				
				
				index++;
				
				if(index==path.length)
				{running = false;}
				
				return d;
			}
			return Direction.STAY;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Direction.STAY;
		}
	}
	
	
	
	
	
	private Hero anotherHero(State state)
	{
		List<Hero> l = state.game.heroes;
		for(Hero r:l) if(r!=state.hero()) return r;
		return null;
	}
	
	
	
	
	private int[] heroToIntArray(Hero hero)
	{
		int x = hero.position.getKey().intValue();
		int y = hero.position.getValue().intValue();
		return new int[]{x,y};
	}
	
	
	
	
	
	
	
	private int[] seachMine() throws Exception
	{
		int x = board.tiles.length;
		int y = board.tiles[0].length;
		
		for(int i=0;i<x;i++) for(int j=0;j<y;j++)
			if(isMine(board.tiles[i][j])) return new int[]{i,j};
		
		throw new Exception("Mine not found");
	}
	
	
	
	
	
	private int[] searchNearestMine() throws Exception
	{
		
		int X = board.tiles.length;
		int Y = board.tiles[0].length;
		
		for(int i=0;i<X;i++) for(int j=0;j<Y;j++)
			if(isMine(board.tiles[i][j])) return new int[]{i,j};
		
		throw new Exception("Mine not found");
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
	
	
	
	
	private boolean isBlock(Board.Tile tile)
	{return !tile.equals(Board.Tile.AIR);}
	
	
	
	private boolean isMine(Board.Tile tile)
	{return tile.equals(Board.Tile.FREE_MINE);}
	
	
	
	
	private Direction direction(int[] start, int[] next) throws Exception
	{
		if(next[0] == start[0]+1) return Direction.SOUTH;
		if(next[0] == start[0]-1) return Direction.NORTH;
		if(next[1] == start[1]+1) return Direction.EAST;
		if(next[1] == start[1]-1) return Direction.WEST;
		
		throw new Exception("Invalid next position: "+toString(next));
	}
	
	
	
	private String toString(int[] p)
	{return "["+p[0]+" "+p[1]+"]";}
	
	
	
	
	
	private double distance(int[] p1, int[] p2)
	{
		double dx = (double) p1[0]-p2[0];
		double dy = (double) p1[1]-p2[1];
		return Math.sqrt(dx*dx + dy*dy);
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
}
