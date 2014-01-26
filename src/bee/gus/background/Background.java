package bee.gus.background;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bee.gus.algo.LouvelBFS;
import bee.gus.client.Board;

public class Background implements Runnable {

	
	private Board board;
	
	private int board_x;
	private int board_y;
	
	private Set mines;
	private Set beers;
	
	private Map pathsToMine;
	private Map pathsToBeer;
	
	
	
	public Background(Board board)
	{
		this.board = board;
		
		board_x = board.tiles.length;
		board_y = board.tiles[0].length;
		
		mines = new HashSet();
		beers = new HashSet();
		
		findTargets();
		
		pathsToMine = new Hashtable();
		pathsToBeer = new Hashtable();
	}
	
	
	
	
	private void findTargets()
	{
		for(int i=0;i<board_x;i++) for(int j=0;j<board_y;j++)
		{
			if(isMine(tileAt(i,j))) mines.add(i+"_"+j);
			if(isBeer(tileAt(i,j))) beers.add(i+"_"+j);
		}
	}



	public void run()
	{
		for(int i=0;i<board_x;i++) for(int j=0;j<board_y;j++)
		handleTile(i,j);
	}
	
	
	
	
	
	private void handleTile(int x, int y)
	{
		Board.Tile tile = board.tiles[x][y];
		
		if(isBlock(tile)) return;
		if(!isNearTarget(x,y)) return;
		
		int[][] path_mine = findNearestPath_mine(x,y);
		if(path_mine!=null) pathsToMine.put(x+"_"+y,path_mine);
		
		int[][] path_beer = findNearestPath_beer(x,y);
		if(path_beer!=null) pathsToBeer.put(x+"_"+y,path_beer);
	}
	
	
	
	
	
	
	private int[][] findNearestPath_mine(int x, int y)
	{
		int min_length = Integer.MAX_VALUE;
		int[][] min_path = null;
		
		int[] start = new int[]{x,y};
		Iterator it = mines.iterator();
		while(it.hasNext())
		{
			int[] end = toIntArray((String)it.next());
			int[][] path = computePath2(start,end);
			int length = path.length;
			
			if(length<min_length)
			{
				min_length = length;
				min_path = path;
			}
		}
		return min_path;
	}
	
	
	
	
	
	private int[][] findNearestPath_beer(int x, int y)
	{
		int min_length = Integer.MAX_VALUE;
		int[][] min_path = null;
		
		int[] start = new int[]{x,y};
		Iterator it = beers.iterator();
		while(it.hasNext())
		{
			int[] end = toIntArray((String)it.next());
			int[][] path = computePath2(start,end);
			int length = path.length;
			
			if(length<min_length)
			{
				min_length = length;
				min_path = path;
			}
		}
		return min_path;
	}
	
	
	
	
	
	
	private int[][] computePath2(int[] start, int[] end)
	{return new LouvelBFS(board,start,end).getDistanceArray();}
	
	
	
	
	
	private boolean isBlock(Board.Tile tile)
	{
		String s = tile.toString();
		return !s.startsWith("@") && !s.equals("  ");
	}
	
	
	
	
	
	private boolean isBeer(Board.Tile tile)
	{
		if(tile==null) return false;
		return tile.toString().equals("[]");
	}
	
	
	private boolean isMine(Board.Tile tile)
	{
		if(tile==null) return false;
		return tile.toString().startsWith("$");
	}
	
	
	private boolean isTarget(Board.Tile tile)
	{
		if(tile==null) return false;
		String s = tile.toString();
		return s.startsWith("$") || s.equals("[]");
	}
	
	
	
	
	
	private boolean isNearTarget(int x, int y)
	{
		if(isTarget(westTile(x,y))) return true;
		if(isTarget(eastTile(x,y))) return true;
		if(isTarget(northTile(x,y))) return true;
		if(isTarget(southTile(x,y))) return true;
		return false;
	}
	
	
	
	
	private Board.Tile westTile(int x, int y)
	{return y>=1?board.tiles[x][y-1]:null;}
	
	
	private Board.Tile eastTile(int x, int y)
	{return y<board_y-1?board.tiles[x][y+1]:null;}
	
	
	private Board.Tile northTile(int x, int y)
	{return x>=1?board.tiles[x-1][y]:null;}
	
	
	private Board.Tile southTile(int x, int y)
	{return x<board_x+1?board.tiles[x+1][y]:null;}
	
	
	
	
	
	private Board.Tile tileAt(int x, int y)
	{
		if(x<0 || x>=board.tiles.length) return null;
		if(y<0 || y>=board.tiles[0].length) return null;
		return board.tiles[x][y];
	}
	
	
	
	
	private int[] toIntArray(String s)
	{
		String[] n = s.split("_");
		return new int[]{i_(n[0]),i_(n[1])};
 	}
	
	private int i_(String s)
	{return Integer.parseInt(s);}
	
}
