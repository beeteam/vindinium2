package bee.gus.algo;

import java.util.List;

import bee.vindinium.main.Board;
import bee.vindinium.main.State;

public class GusBFS {

	
	
	private State state;
	private Board.Tile[][] tiles;
	private int X;
	private int Y;
	
	private boolean[][] markers;
	
	
	
	public GusBFS(State state) {
		this.state = state;
		tiles = state.game.board.tiles;
		X = tiles.length;
		Y = tiles[0].length;
	}
	
	
	
	
	
	public List getDistance(State state, int[] start, int[] end) {
		
		initMarkers();
		return null;
	}
	
	
	
	
	
	
	private List getFreeNeighbours(int[] pos) {
		
		int x = pos[0];
		int y = pos[1];
		
		return null;
		
	}
    
	
	
	
	
	private void initMarkers()
	{	
		markers = new boolean[X][Y];
		for(int i=0;i<X;i++) for(int j=0;j<Y;j++)
		{
			//tiles[i][j].
			markers[i][j] = true;
		}
	}
}
