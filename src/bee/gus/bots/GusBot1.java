package bee.gus.bots;

import java.util.List;

import bee.gus.algo.GusDFSHolder;
import bee.gus.algo.LouvelBFS;
import bee.gus.client.Board;
import bee.gus.client.Bot;
import bee.gus.client.Direction;
import bee.gus.client.Hero;
import bee.gus.client.State;


public class GusBot1 implements Bot {
	
	public static final int TURNS = 40;
	public static final String MAP = "m1";
	//public static final String MAP = null;
	
	
	public static final int WEAK_LEVEL = 25;
	public static final int ABIT_THIRSTY_LEVEL = 70;
	public static final int VERY_THIRSTY_LEVEL = 30;
	public static final int AGGRESSIVE_LEVEL = 10;
	
	
	public static final int ALGO = 2;
	
	

	private int turn;
	private int totalTurn;
	private Board board;
	private int boardX;
	private int boardY;

	private Hero me;
	private int[] me_;
	
	private int[][] path;
	private int pathLength;
	private int index;
	
	
	
	
	

	public Direction nextMove(State state)
	{
		try
		{
			initData(state);
			println("------------------------");
			println("turn: "+turn+"/"+totalTurn+" life: "+me.life);
			println();
			
			
			if(canFightMine())
			{
				if(isMine(westTile())) return shortcut("W->mine!",Direction.WEST);
				if(isMine(eastTile())) return shortcut("E->mine!",Direction.EAST);
				if(isMine(northTile())) return shortcut("N->mine!",Direction.NORTH);
				if(isMine(southTile())) return shortcut("S->mine!",Direction.SOUTH);
			}
			
			if(isABitThirsty())
			{
				if(isBeer(westTile())) return shortcut("W->beer!",Direction.WEST);
				if(isBeer(eastTile())) return shortcut("E->beer!",Direction.EAST);
				if(isBeer(northTile())) return shortcut("N->beer!",Direction.NORTH);
				if(isBeer(southTile())) return shortcut("S->beer!",Direction.SOUTH);
			}
			
			if(isAgressive())
			{
				if(isHero(westTile())) return shortcut("W->hero!",Direction.WEST);
				if(isHero(eastTile())) return shortcut("E->hero!",Direction.EAST);
				if(isHero(northTile())) return shortcut("N->hero!",Direction.NORTH);
				if(isHero(southTile())) return shortcut("S->hero!",Direction.SOUTH);
			}
			
			
			if(path==null) startStrategy();
			if(hasPath()) return walkInsidePath();
			
			
			println("IDLE (-_-)");
			return Direction.STAY;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Direction.STAY;
		}
	}
	
	
	
	
	
	
	private Direction shortcut(String title, Direction d)
	{
		//resetPath();
		println(title+" shortcut to "+d.name+" (life="+me.life+")");
		return d;
	}
	
	
	
	
	
	
	
	private void initData(State state)
	{
		turn = state.game.turn/4;
		totalTurn = state.game.maxTurns/4;
		board = state.game.board;
		boardX = board.tiles.length;
		boardY = board.tiles[0].length;
		
		me = state.hero();
		me_ =  heroToIntArray(me);
	}
	
	
	
	
	
	
	
	
	private void startStrategy() throws Exception
	{
		if(isVeryThirsty())
			startBeerStrategy();
		else startMineStrategy();
	}
	
	
	
	
	private void startBeerStrategy() throws Exception
	{
		int[] end = searchNearestBeer();
		if(end==null) return;
		
		println("Start beer strategy: order="+toString(me_)+"->"+toString(end));
		initializePath(me_,end);
	}
	
	
	
	
	private void startMineStrategy() throws Exception
	{
		int[] end = searchNearestMine();
		if(end==null) return;
		
		println("Start mine strategy: order="+toString(me_)+"->"+toString(end));
		initializePath(me_,end);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void initializePath(int[] start, int[] end) throws Exception
	{
		path = computePath(start,end);
		pathLength = path==null?-1:path.length;
		index = 0;
		
		println("path 1: "+pathToString1());
		println("path 2: "+pathToString2());
		println();
	}
	
	
	private int[][] computePath(int[] start, int[] end) throws Exception
	{
		if(ALGO==1) return computePath1(start,end);
		if(ALGO==2) return computePath2(start,end);
		throw new Exception("Unknown algo: "+ALGO);
	}
	
	
	private int[][] computePath1(int[] start, int[] end) throws Exception
	{return new GusDFSHolder(board,start,end).getDistanceArray();}
	
	
	private int[][] computePath2(int[] start, int[] end) throws Exception
	{return new LouvelBFS(board,start,end).getDistanceArray();}
	
	
	
	
	
	
	
	
	
	private Direction walkInsidePath() throws Exception
	{
		if(index==path.length-1)
			throw new Exception("Reaching end of path !!!!");
		
		int[] p0 = path[index];
		int[] p1 = path[index+1];
		
		Direction d = direction(p0,p1);
		
		print("index: "+index+" ");
		print("p0: "+toString(p0)+" ");
		print("p1: "+toString(p1)+" ");
		println("walking to direction: "+d.name);
		
		if(index==path.length-2) resetPath();
		else index++;
		
		return d;
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
	
	
	
	
	
	
	
	private int[] seachOneMine() throws Exception
	{
		int x = board.tiles.length;
		int y = board.tiles[0].length;
		
		for(int i=0;i<x;i++) for(int j=0;j<y;j++)
			if(isMine(board.tiles[i][j])) return new int[]{i,j};
		
		throw new Exception("Mine not found");
	}
	
	
	
	
	
	
	private int[] searchNearestMine()
	{
		double d_min = Double.MAX_VALUE;
		int[] bestPosition = null;
		
		int X = board.tiles.length;
		int Y = board.tiles[0].length;
		
		for(int i=0;i<X;i++) for(int j=0;j<Y;j++)
			if(isMine(board.tiles[i][j]))
		{
			int[] minePosition = new int[]{i,j};
			double d = distance(minePosition,me_);
			if(d<d_min)
			{
				d_min = d;
				bestPosition = minePosition;
			}
		}
		return bestPosition;
	}
	
	
	
	
	
	
	
	
	private int[] searchNearestBeer()
	{
		double d_min = Double.MAX_VALUE;
		int[] bestPosition = null;
		
		int X = board.tiles.length;
		int Y = board.tiles[0].length;
		
		for(int i=0;i<X;i++) for(int j=0;j<Y;j++)
			if(isBeer(board.tiles[i][j]))
		{
			int[] beerPosition = new int[]{i,j};
			double d = distance(beerPosition,me_);
			if(d<d_min)
			{
				d_min = d;
				bestPosition = beerPosition;
			}
		}
		return bestPosition;
	}

	
	
	
	
	
	
	
	
	
	
	
	private Board.Tile westTile()
	{
		int x = me_[0];
		int y = me_[1]-1;
		return y>=0?board.tiles[x][y]:null;
	}
	
	private Board.Tile eastTile()
	{
		int x = me_[0];
		int y = me_[1]+1;
		return y<boardY?board.tiles[x][y]:null;
	}
	
	private Board.Tile northTile()
	{
		int x = me_[0]-1;
		int y = me_[1];
		return x>=0?board.tiles[x][y]:null;
	}
	
	private Board.Tile southTile()
	{
		int x = me_[0]+1;
		int y = me_[1];
		return x<boardX?board.tiles[x][y]:null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean isMine(Board.Tile tile)
	{
		if(tile==null) return false;
		return tile.equals(Board.Tile.FREE_MINE);
	}
	
	
	
	private boolean isBeer(Board.Tile tile)
	{
		if(tile==null) return false;
		return tile.equals(Board.Tile.TAVERN);
	}
	
	
	
	private boolean isHero(Board.Tile tile)
	{
		if(tile==null) return false;
		return tile.toString().startsWith("@");
	}
	
	
	
	
	
	private Direction direction(int[] start, int[] next) throws Exception
	{
		if(next[0] == start[0]+1) return Direction.SOUTH;
		if(next[0] == start[0]-1) return Direction.NORTH;
		if(next[1] == start[1]+1) return Direction.EAST;
		if(next[1] == start[1]-1) return Direction.WEST;
		
		throw new Exception("Invalid next position: "+toString(next));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean canFightMine()
	{return me.life>=WEAK_LEVEL;}
	
	
	private boolean isABitThirsty()
	{return me.life<=ABIT_THIRSTY_LEVEL;}
	
	
	private boolean isVeryThirsty()
	{return me.life<=VERY_THIRSTY_LEVEL;}
	
	
	private boolean isAgressive()
	{return me.life>=AGGRESSIVE_LEVEL;}
	
	
	
	
	
	

	
	
	private String toString(int[] p)
	{return p==null?"null":"["+p[0]+" "+p[1]+"]";}
	
	
	
	private double distance(int[] p1, int[] p2)
	{
		double dx = (double) p1[0]-p2[0];
		double dy = (double) p1[1]-p2[1];
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	
	
	
	
	
	
	
	
	private void resetPath()
	{
		path=null;
		pathLength=-1;
	}
	
	private boolean hasPath()
	{return pathLength>0;}

	
	
	private String pathToString1()
	{
		if(path==null) return "no path";
		return toString(path[0])+" -> "+toString(path[pathLength-1])+" ("+pathLength+")";
	}
	

	private String pathToString2()
	{
		if(path==null) return "no path";
		StringBuffer b = new StringBuffer();
		for(int[] p : path) b.append(toString(p));
		return b.toString();
	}
	
	
	
	
	
	
	
	
	

	
	private void print(String m)
	{System.out.print(m);}
	
	private void println(String m)
	{System.out.println(m);}
	
	private void println()
	{println("");}
}
