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
	
	public static final boolean PRINT1 = true;
	public static final boolean PRINT2 = false;
	
	
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
		Direction d = nextMove_(state);
		println2((state.game.turn/4)+": DIRECTION FINAL = "+d);
		return d;
	}
	
	
	
	
	

	private Direction nextMove_(State state)
	{
		try
		{
			initData(state);
			println1("------------------------");
			println1("turn: "+turn+"/"+totalTurn+" life: "+me.life+" me: "+toString(me_));
			println1();
			
			
			if(canFightMine())
			{
				if(isTargetMine(westTile())) return shortcut("W->mine!",Direction.WEST);
				if(isTargetMine(eastTile())) return shortcut("E->mine!",Direction.EAST);
				if(isTargetMine(northTile())) return shortcut("N->mine!",Direction.NORTH);
				if(isTargetMine(southTile())) return shortcut("S->mine!",Direction.SOUTH);
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
			
			
			println1("IDLE (-_-)");
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
		int[] target = targetPosition(d);
		if(equals(target,pathEnd())) resetPath();
		
		println1(title+" shortcut to "+d.name+" (life="+me.life+")");
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
		
		println1("Start beer strategy: order="+toString(me_)+"->"+toString(end));
		initializePath(me_,end);
	}
	
	
	
	
	private void startMineStrategy() throws Exception
	{
		int[] end = searchNearestMine();
		if(end==null) return;
		
		println1("Start mine strategy: order="+toString(me_)+"->"+toString(end));
		initializePath(me_,end);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void initializePath(int[] start, int[] end) throws Exception
	{
		path = computePath(start,end);
		pathLength = path==null?-1:path.length;
		index = 0;
		
		println1("path 1: "+pathToString1());
		println1("path 2: "+pathToString2());
		println1();
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
		
		if(!equals(me_,p0))
		{
			println1("Dephasage entre position r�elle et chemin prevu: reset");
			resetPath();
			return Direction.STAY;
		}
		
		Direction d = direction(p0,p1);
		
		
		if(isHero(tileAt(p1)))
		{
			println1("walking interrupted by hero: reseting path");
			resetPath();
			return d;
		}
		
		print1("index: "+index+" ");
		print1("p0: "+toString(p0)+" ");
		print1("p1: "+toString(p1)+" ");
		println1("walking to direction: "+d.name);
		
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
	
	
	
	
	
	
	

	
	
	
	
	private int[] searchNearestMine()
	{
		double d_min = Double.MAX_VALUE;
		int[] bestPosition = null;
		
		int X = board.tiles.length;
		int Y = board.tiles[0].length;
		
		for(int i=0;i<X;i++) for(int j=0;j<Y;j++)
			if(isTargetMine(board.tiles[i][j]))
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
			println1("distance to "+toString(beerPosition)+" = "+d);
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
	

	
	
	
	
	
	private int[] targetPosition(Direction d)
	{
		if(d==Direction.WEST) return new int[]{me_[0],me_[1]+1};
		if(d==Direction.EAST) return new int[]{me_[0],me_[1]-1};
		if(d==Direction.NORTH) return new int[]{me_[0]-1,me_[1]};
		if(d==Direction.SOUTH) return new int[]{me_[0]+1,me_[1]};
		return null;
	}
	
	
	
	
	
	
	
	private boolean isTargetMine(Board.Tile tile)
	{
		if(tile==null) return false;
		String desc = tile.toString();
		return desc.startsWith("$") && !desc.equals("$"+me.id);
	}
	
	
	
	
//	private boolean isFreeMine(Board.Tile tile)
//	{
//		if(tile==null) return false;
//		return tile.equals(Board.Tile.FREE_MINE);
//	}
	
	
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Board.Tile tileAt(int[] p)
	{
		int x = p[0];
		int y = p[1];
		if(x<0 || x>=board.tiles.length) return null;
		if(y<0 || y>=board.tiles[0].length) return null;
		return board.tiles[x][y];
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
	
	
	
	private boolean equals(int[] p1, int[] p2)
	{return p1!=null && p2!=null && p1[0]==p2[0] && p1[1]==p2[1];}
	
	
	
	
	
	
	
	private void resetPath()
	{
		path=null;
		pathLength=-1;
	}
	
	private boolean hasPath()
	{return pathLength>0;}
	
	
	private int[] pathEnd()
	{return path==null || path.length==0?null:path[path.length-1];}

	
	
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
	
	
	
	
	
	
	
	
	

	
	private void print1(String m)
	{if(PRINT1) System.out.print(m);}
	
	private void println1(String m)
	{if(PRINT1) System.out.println(m);}
	
	private void println1()
	{if(PRINT1) println1("");}
	
	
	
	
	private void println2(String m)
	{if(PRINT2) System.out.println(m);}
	
}
