package bee.gus.bots;

import java.util.List;

import bee.gus.algo.GusDFS;
import bee.gus.client.Board;
import bee.gus.client.Bot;
import bee.gus.client.Direction;
import bee.gus.client.Hero;
import bee.gus.client.State;


public class GusBot1 implements Bot {
	
	
	private GusDFS dfs;
	
	
	
	public GusBot1() {
		dfs = new GusDFS();
	}
	
	
	
	

	public Direction nextMove(State state)
	{
		try
		{
			Board board = state.game.board;
			boolean[][] maze = buildMaze(board);
			
			if(state.game.turn==0)
			printMaze(maze);
			
			Hero me = state.hero();
			Hero ennemi = anotherHero(state);
			
			int[] start =  heroToIntArray(me);
			int[] end = heroToIntArray(ennemi);
			
			dfs.register("maze",maze);
			dfs.register("start",start);
			dfs.register("end",end);
			dfs.run();
			
			
			int[][] path = (int[][]) dfs.retrieve("path");
			
			if(path.length==1) return Direction.STAY;
			
			int[] next = path[1];
			
			
			System.out.print("ennemi: "+ennemi.id+" ");
			System.out.print("start: "+toString(start)+" ");
			System.out.print("end: "+toString(end)+" ");
			System.out.println("next: "+toString(next));
			
			return direction(start,next);
			
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
	

	
	
	
	
	
	private boolean[][] buildMaze(Board board)
	{
		int x = board.tiles.length;
		int y = board.tiles[0].length;
		
		boolean[][] maze = new boolean[x][y];
		for(int i=0;i<x;i++) for(int j=0;j<y;j++)
		maze[i][j] = !isWall(board.tiles[i][j]);
		
		return maze;
	}
	
	
	
	
	private boolean isWall(Board.Tile tile)
	{return tile.equals(Board.Tile.WALL);}
	
	
	
	
	
	private Direction direction(int[] start, int[] next) throws Exception
	{
		if(next[0] == start[0]+1) return Direction.SOUTH;
		if(next[0] == start[0]-1) return Direction.NORTH;
		if(next[1] == start[1]+1) return Direction.EAST;
		if(next[1] == start[1]-1) return Direction.WEST;
		
		throw new Exception("Invalid next position: "+toString(next));
	}
	
	
	
	private String toString(int[] p) {return "["+p[0]+" "+p[1]+"]";}
	
	
	
	
	private void printMaze(boolean[][] maze)
	{
		int x = maze.length;
		int y = maze[0].length;
		for(int i=0;i<x;i++) 
		{
			for(int j=0;j<y;j++)
			{
				
			}
		}
	}
}
