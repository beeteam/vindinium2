package bee.gus.algo;


public class GusBFS {

    

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
    	int[][] v = new int[][]{{-1,-1},{-1,-1},{-1,-1},{-1,-1}};
    	
    	int x = path[index][0];
    	int y = path[index][1];
    	
    	int k = 0;
    	int k1 = -1;
    	int dmin = maze_X+maze_Y;
    	
    	if(x>0 && maze[x-1][y])
    	{
    		v[k] = new int[]{x-1,y};
    		int d = distanceToEnd(v[k]);
    		if(d<dmin) {k1 = k;dmin = d;}
    		k++;
    	}
    	if(x<maze_X-1 && maze[x+1][y])
    	{
    		v[k] = new int[]{x+1,y};
    		int d = distanceToEnd(v[k]);
    		if(d<dmin) {k1 = k;dmin = d;}
    		k++;
    	}
    	if(y>0 && maze[x][y-1])
    	{
    		v[k] = new int[]{x,y-1};
    		int d = distanceToEnd(v[k]);
    		if(d<dmin) {k1 = k;dmin = d;}
    		k++;
    	}
    	if(y<maze_Y-1 && maze[x][y+1])
    	{
    		v[k] = new int[]{x,y+1};
    		int d = distanceToEnd(v[k]);
    		if(d<dmin) {k1 = k;dmin = d;}
    		k++;
    	}
    	return k1==-1?null:v[k1];
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

