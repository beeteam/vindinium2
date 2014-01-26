package bee.gus.algo;

import bee.gus.client.Board;

public class GusSimpleAlgo {

	
	private Board board;
	private int[] start;
	private int[] end;
	
	
	public GusSimpleAlgo(Board board, int[] start, int[] end)
	{
		this.board = board;
		this.start = start;
		this.end = end;
	}

	
	
	
	
	public int[][] getDistanceArray() throws Exception
	{
		int x0 = start[0];
		int y0 = start[1];
		
		int x1 = start[0];
		int y1 = start[1];
		
		int size = Math.abs(end[0]-start[0]) + Math.abs(end[1]-start[1]);
		
		int[][] path = new int[size][2];
		int dx = end[0]>start[0]?1:-1;
		int dy = end[1]>start[1]?1:-1;
		
		//for(int i=start[0];)
		
		return null;
	}
}
