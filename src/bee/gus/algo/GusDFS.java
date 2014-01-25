package bee.gus.algo;





public class GusDFS {


    public String getName()				{return "gus.math.maze.solve.algo.depthfirstsearch";}
    public String getCreationDate()		{return "2011.08.16";}


    private boolean[][] maze;
    private int[] start;
    private int[] end;
    
    private int maze_X;
    private int maze_Y;
    
    private int[][] path;
    private int index;
    
    
    

    public void register(String key, Object obj) throws Exception
    {
    	if(key.equals("maze"))
    	{
    		initMaze((boolean[][]) obj);
    		return;
    	}
    	if(key.equals("start"))
    	{
    		start = (int[]) obj;
    		if(start.length!=2) throw new Exception("Invalid size for start int[]: "+start.length);
    		return;
    	}
    	if(key.equals("end"))
    	{
    		end = (int[]) obj;
    		if(end.length!=2) throw new Exception("Invalid size for end int[]: "+start.length);
    		return;
    	}
        throw new Exception("Unknown key: "+key);
    }

    
    


    public Object retrieve(String key) throws Exception
    {
    	if(key.equals("path")) return finalPath();
    	if(key.equals("location")) return path[index];
    	if(key.equals("keys")) return new String[]{"path","location"};
        throw new Exception("Unknown key: "+key);
    }



    public void run()
    {
    	if(!maze[end[0]][end[1]])
    	{searchOver();return;}
    	
    	path = new int[(int)(maze_X*maze_Y*0.5)][2];
    	index = 0;
    	handleNewPosition(start);
    	
    	keepSearching();
    }
    
    
    
    
    
    
    
    private void keepSearching()
    {
    	int[] move = findNextMove();
    	if(move!=null) 
    	{
    		index++; // go forward
    		handleNewPosition(move);
        	if(isEnd(move)) {searchOver();return;}
        	moved();
        	keepSearching();
    	}
    	else // cul-de-sac
    	{
    		index--; // go backward
    		if(index==-1) {searchOver();return;}
    		moved();
    		keepSearching();
    	}
    }
    
    
    
    
    
    
    private void handleNewPosition(int[] p)
    {
    	path[index] = p;
    	maze[p[0]][p[1]] = false;
    }
    
    
    
    
    
    private int[] findNextMove()
    {
    	int x = path[index][0];
    	int y = path[index][1];
    	
    	int[] v_min = null;
    	int d_min = maze_X+maze_Y;
    	
    	if(isFree(x-1,y))
    	{
    		int[] v = new int[]{x-1,y};
    		int d = distanceToEnd(v);
    		
    		if(d<d_min) {v_min = v;d_min = d;}
    	}
    	if(isFree(x+1,y))
    	{
    		int[] v = new int[]{x+1,y};
    		int d = distanceToEnd(v);
    		
    		if(d<d_min) {v_min = v;d_min = d;}
    	}
    	if(isFree(x,y-1))
    	{
    		int[] v = new int[]{x,y-1};
    		int d = distanceToEnd(v);
    		
    		if(d<d_min) {v_min = v;d_min = d;}
    	}
    	if(isFree(x,y+1))
    	{
    		int[] v = new int[]{x,y+1};
    		int d = distanceToEnd(v);
    		
    		if(d<d_min) {v_min = v;d_min = d;}
    	}
    	return v_min;
    }
    
    
    
    
    
    
    private boolean isFree(int x, int y)
    {
    	if(x<0 || x>=maze_X) return false;
    	if(y<0 || y>=maze_Y) return false;
    	return maze[x][y];
    }

    
    
    
    private int distanceToEnd(int[] p)
    {return Math.abs(end[0]-p[0]) + Math.abs(end[1]-p[1]);}

    
    private boolean isEnd(int[] p)
    {return p[0]==end[0] && p[1]==end[1];}
    
    
    
    private int[][] finalPath()
    {
    	if(path==null || index==-1) return null;
    	int[][] path1 = new int[index+1][2];
    	for(int i=0;i<index+1;i++) path1[i] = path[i];
    	return path1;
    }
    
    
    
    
    
    private void initMaze(boolean[][] m) throws Exception
    {
		maze_X = m.length;
		if(maze_X==0)  throw new Exception("Invalid maze_X value = 0");
    	maze_Y = m[0].length;
    	if(maze_Y==0)  throw new Exception("Invalid maze_Y value = 0");
		
    	maze = new boolean[maze_X][maze_Y];
    	for(int i=0;i<maze_X;i++)
    	for(int j=0;j<maze_Y;j++)
    		maze[i][j] = m[i][j];
    }
    
    
    
    
    
    private void moved()
    {
    	//send(this,"moved()");
    }
    
    private void searchOver()
    {
    	//send(this,"searchOver()");
    }
}

