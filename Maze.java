import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze implements Serializable{
	
	int columns = 101;   // x on the array
	int rows = 101;		// y on the array

	Box mazeArray[][];
	public Box startBox, endBox;
	public static ArrayList<Box> path = new ArrayList<Box>();
	public static ArrayList<Box> movedPath = new ArrayList<Box>();
	
	public Maze() {
		columns = 101;
		rows = 101;
		mazeArray = new Box[rows][columns];
		initializeBox();
		generateMaze();
	}
	public Maze(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		mazeArray = new Box[rows][columns];
		initializeBox();
		generateMaze();
	}
	
	public void setPath(ArrayList<Box> p) {
		path = p;
	}
	public ArrayList<Box> getPath() {
		return path;
	}
	
	
	public void printTheBoxes() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; i < columns; j++) {
				System.out.println(i + " " + j);
				mazeArray[i][j].printBox();
				System.out.println("\n");
			}
		}
	}
	
	
	String[] dir = new String[]{"UP","DOWN","LEFT","RIGHT"};
	int unvisited = rows * columns - 1;

	//Stack stack = new Stack();
	Box currentBox;
	
	
	public void generateMaze() {
		//currentBox = mazeArray[0][0];
		//boxStack.push(currentBox);
		Random rnd = new Random();
		
		int x = rnd.nextInt(columns);
		int y = rnd.nextInt(rows);
		Box currentBox = mazeArray[y][x];
		
		Stack<Box> boxStack = new Stack<Box>();
		int total = rows * columns;
		int visited = 1;
		
//		boxStack.push(currentBox);
//		if (boxStack.isEmpty()) {System.out.println("emptyStack");}
		
		ArrayList<VertexA> neighborsList = new ArrayList<VertexA>();
		VertexA tmpVert = new VertexA();

		while(visited < total) {
			//if (boxStack.isEmpty()) {System.out.println("emptyStack");}
			//boxStack.push(currentBox);
			neighborsList.clear();
			tmpVert = new VertexA();
			if((x-1) >= 0 && mazeArray[y][x-1].checkWalls() == true) {
				//System.out.println("entering 1");
				tmpVert.x1 = x;
				tmpVert.y1 = y;
				tmpVert.x2 = x-1;
				tmpVert.y2 = y;
				tmpVert.wall1 = 0;
				tmpVert.wall2 = 2;
				neighborsList.add(tmpVert);
			}
			
			tmpVert = new VertexA();
			if((x+1) < columns && mazeArray[y][x+1].checkWalls() == true) {
				//System.out.println("entering 2");
				tmpVert.x1 = x;
				tmpVert.y1 = y;
				tmpVert.x2 = x+1;
				tmpVert.y2 = y;
				tmpVert.wall1 = 2;
				tmpVert.wall2 = 0;
				neighborsList.add(tmpVert);
			}
			tmpVert = new VertexA();
			if((y-1) >= 0 && mazeArray[y-1][x].checkWalls() == true) {
				//System.out.println("entering 3");
				tmpVert.x1 = x;
				tmpVert.y1 = y;
				tmpVert.x2 = x;
				tmpVert.y2 = y-1;
				tmpVert.wall1 = 3;
				tmpVert.wall2 = 1;
				neighborsList.add(tmpVert);
				
			}
			tmpVert = new VertexA();
			if((y+1) < rows && mazeArray[y+1][x].checkWalls() == true) {
				//System.out.println("entering 4");
				tmpVert.x1 = x;
				tmpVert.y1 = y;
				tmpVert.x2 = x;
				tmpVert.y2 = y+1;
				tmpVert.wall1 = 1;
				tmpVert.wall2 = 3;
				neighborsList.add(tmpVert);
			}
			//if (boxStack.isEmpty()) {System.out.println("emptyStack");}
			if(neighborsList.size() >= 1) {
				int randNeighbor = rnd.nextInt(neighborsList.size());
				tmpVert = neighborsList.get(randNeighbor);
				mazeArray[tmpVert.y1][tmpVert.x1].walls[tmpVert.wall1] = 0;
				mazeArray[tmpVert.y2][tmpVert.x2].walls[tmpVert.wall2] = 0;
				boxStack.push(currentBox);
				currentBox = mazeArray[tmpVert.y2][tmpVert.x2];
				
				y = currentBox.getY();
				x = currentBox.getX();
				visited++;}
			else {
				currentBox = boxStack.pop();
				x = currentBox.getX();
				y = currentBox.getY();
				
				}
			}
		
			
		
		}
		
	
	public void setWallinMaze(int row, int column, int WallIndex){
		
		mazeArray[row][column].setWallofBox(WallIndex);
		
	}
	
	
	public void removeWallinMaze(int row, int column, int WallIndex){
		
		mazeArray[row][column].removeWallofBox(WallIndex);
		
	}
	

	public void initializeBox() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0 ; j < columns; j++) {
				mazeArray[i][j] = new Box();
				mazeArray[i][j].x = j;
				mazeArray[i][j].y = i;
				
			}
		}
	}
	
	public Box getNeighbors(int row, int column) {
		if(row > 0) {return mazeArray[row-1][column];}
		if(row < rows-1) {return mazeArray[row+1][column];}
		if(column > 0) {return mazeArray[row][column -1];}
		if(column > columns-1) {return mazeArray[row][column + 1];}
		return mazeArray[row][column];
	}
}
