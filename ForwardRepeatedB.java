import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import java.io.*;



public class ForwardRepeatedB extends JFrame {
	int sizeX = 101;
	int sizeY = 101;
	int boxSize = 7;
	ArrayList<Box> path = new ArrayList<Box>();
	ArrayList<ArrayList<Box>> pathsTried = new ArrayList<ArrayList<Box>>();
	Box start, end;
	ArrayList<Box> estimatedPath;
	ArrayList<Box> movedPath = new ArrayList<Box>();

	Maze myMaze = new Maze(sizeX, sizeY);
	public ForwardRepeatedB() {
		long startTime = System.nanoTime();
		Random rnd = new Random();
		
		int x = rnd.nextInt(myMaze.columns);
		int y = rnd.nextInt(myMaze.rows);

		start = myMaze.mazeArray[y][x];
		myMaze.startBox = start;
		start.setSart();

		end = myMaze.mazeArray[sizeY-1][sizeX-1];

		end.setEnd();
		calculateH(myMaze);

		
		path = repeatedForward(myMaze, start);
		startUI();
		long endTime = System.nanoTime();
		System.out.println("Took "+((double)(endTime - startTime)/1000000000) + " seconds"); 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ForwardRepeatedB mainMaze = new ForwardRepeatedB();
				mainMaze.setVisible(true);				
			}
		});
	}
	
	public void startUI() {
		setTitle("Maze Display");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((boxSize * sizeY) + 100, (boxSize * sizeX) + 100);
		MazeDisplay displayMaze = new MazeDisplay(boxSize, myMaze, path, pathsTried, movedPath);
		add(displayMaze);
		setContentPane(displayMaze);
		displayMaze.setFocusable(true);
		setLocationRelativeTo(null);
	}
	
	public ArrayList<Box> reconstructPath(Box goal) {
		ArrayList<Box> path = new ArrayList<Box>();
		while(goal.parent != null) {
			path.add(goal);
			goal = goal.parent;
		}
		return path;
}
	int counter;
	public void checking(Box newCurrent) {
		//
		resetParents();
		boolean test = true;
		while(test) {
			counter++;
			newCurrent.g = 0;
			newCurrent.setSearch(counter);
			end.g = Integer.MAX_VALUE;
			end.setSearch(counter);
			PriorityQueue<Box> open = new PriorityQueue<Box>();
			newCurrent.f = newCurrent.g + newCurrent.h;
			open.add(newCurrent);
			pathB =  searchMaze(myMaze,newCurrent);
			test= false;
		}
		//for(Box b: pathB) {System.out.println(b.getX()+","+b.getY());}
	}
	ArrayList<Box> pathB; ArrayList<Box> pathA;
	ArrayList<Box> visitedNodes = new ArrayList<Box>();
	public ArrayList<Box> repeatedForward(Maze myMaze , Box curr){
		boolean test = true;
		counter = 0;
		Box newCurrent = curr;
		checking(newCurrent);									//initial search
		pathA= pathB;
		//for(Box b: estimatedPath) {System.out.println(b.getX()+","+b.getY());}
		System.out.println("Start: " +  start.getY()  + "," +  start.getX() );
		int index = 0;
		while(test) { 
			
			System.out.println("NewCurrent: "+newCurrent.y+", "+newCurrent.x);
//			if(pathB == null) {
//				//System.out.println("Path is null");
//				pathB= pathA;
//			}
			
			index = pathB.indexOf(newCurrent);
			if(index <= 0) {
				//newCurrent = prevPos;
				index = pathB.indexOf(newCurrent);
			}
			Box temp = pathB.get(pathB.size()-1);
			newCurrent = myMaze.mazeArray[temp.y][temp.x];				//agent's new position according to search
			System.out.println("moved agent according to search");
			System.out.println("Moved To : "+ newCurrent.y +", "+newCurrent.x);
			movedPath.add(newCurrent);
			if(newCurrent.x == end.getX()){
				if(newCurrent.y == end.getY()) {test = false;}
			}
			visitedNodes.add(newCurrent);
			expanded++;
			calculatePath(newCurrent);									//estimated path of the agent
			newCurrent = moveAgent(newCurrent);							//move the agent according to that path
			System.out.println("moved agent according to path");
			System.out.println("Moved To : "+newCurrent.y+", "+newCurrent.x);
			if(newCurrent.x == end.getX()){
				if(newCurrent.y == end.getY()) {test = false;}
			}
			checking(newCurrent);
			//for(Box b: pathB) {System.out.println(b.getX()+","+b.getY());}
		}
		System.out.println("End: " +  end.getX()  + "," +  end.getX() );
		System.out.println("this is # of expanded: " + expanded);
		return pathA;
	}
	public Box moveAgent(Box c) {
		Box newPos = c;
		boolean blocked = false;
		int index = 0;
		//System.out.println("moving");
		while(!blocked) {
			ArrayList<Box> endNeighbors = 	allAction(end);

			if(endNeighbors.contains(newPos)) {
				System.out.println("end neighbor");
				return end;}		//otherwise check if the path agent knows is not blocked
			
			ArrayList<Box> neighbors = 	allAction(newPos);									
				// check if we are next to the end then next position is the end
			Box tempPos = estimatedPath.get(index);
			//System.out.println("Next according to path: " + tempPos.getY() + "," + tempPos.getX());

			if(neighbors.contains(tempPos) && !visitedNodes.contains(tempPos)) {			//if the neighbor is in the path its not blocked.
				System.out.println("not blocked");
				System.out.println("changing pos");
				newPos = tempPos;
				visitedNodes.add(newPos);
				expanded++;
				movedPath.add(newPos);
				index++;
			}
			else {
				System.out.println("blocked");
			 blocked = true;	
			}
		}
		return newPos;
	}
	int expanded = 0;
	public ArrayList<Box> searchMaze (Maze myMaze, Box curr) {
		 Comparator<Box> smallFbigG = new Comparator<Box>() {
	            @Override
	            public int compare(Box b1, Box b2) {
	        		if(b1.f < b2.f) {
	        			return 1;
	        		}
	        		else if(b1.f> b2.f) {
	        			return -1;
	        		}
	        		else { 
	        			if(b1.g > b2.g) {return 1;}
	        			return -1;
	        			}
	        	}
	        };
			PriorityQueue<Box> openList = new PriorityQueue<>(smallFbigG);
			PriorityQueue<Box> closedList = new PriorityQueue<>(smallFbigG);
		//pointer
		Box currentPointer = new Box();

		//add starting position to openList	
		openList.add(curr);
		
		while(!openList.isEmpty()) {
			//look at the queue
			//System.out.println(openList.size());
			currentPointer = openList.remove();

			if(!closedList.contains(currentPointer)) {
			//if currentPointer f value equals target g value, stop
				if(currentPointer.x == end.x && currentPointer.y == end.y) {
				return reconstructPath(end);
			}
				//remove it from openlist
			closedList.add(currentPointer);
			
			//System.out.println(currentPointer.x + "    " + currentPointer.y);
				//explore the children..
				//check current box for walls. if wall is 0, then it is open. 
				//walls [NORTH, EAST, SOUTH, WEST] THAT'S THE DIRECTION/DISPLAY
			ArrayList<Integer> currentWalls = getNeighbors(currentPointer);
				//System.out.print(currentWalls.get(0));
			for(int i = 0; i < currentWalls.size(); i++) {
				Box neighbor = new Box();
				if(currentWalls.get(i) == 0) {
					if(i == 0) {
							//neighbor is myMaze.mazeArray[currenty][currentx-1] or north wall open
						neighbor = myMaze.mazeArray[currentPointer.y][currentPointer.x-1];
					}
					else if(i == 1) {
						//neighbor is myMaze.mazeArray[currentX+1][currentY] or east wall open
						neighbor = myMaze.mazeArray[currentPointer.y+1][currentPointer.x];
					}
					else if(i == 2) {
						//neighbor is myMaze.mazeArray[currentX][currentY+1] or south wall open
						neighbor = myMaze.mazeArray[currentPointer.y][currentPointer.x+1];
					}
					else {
						//neighbor is myMaze.mazeArray[currentX-1][currentY] or west wall open
						neighbor = myMaze.mazeArray[currentPointer.y-1][currentPointer.x];
						}

				if(!closedList.contains(neighbor)) {
						//calculateValues(myMaze, currentPointer.x, currentPointer.y);
					int predicted = neighbor.h;
						//System.out.println(neighbor.h);
					int neighborDistance = 1; //using manhattan distance
					int currentToStart = currentPointer.g;//calculateDistanceToStart(currentPointer);
					int totalDistance = currentToStart + neighborDistance + predicted;
							//check if total distance is smaller than neighbor total distance
						
					if(totalDistance <= neighbor.f || !openList.contains(neighbor) ) {
							if(neighbor.search < counter) {
								neighbor.g = Integer.MAX_VALUE;
								neighbor.search = counter;
								}
							if(neighbor.g > currentPointer.g + 1) {
								neighbor.g = currentPointer.g + 1;
								neighbor.parent = currentPointer;
								if(openList.contains(neighbor)) {
									openList.remove(neighbor);
									}
								neighbor.f = neighbor.g + neighbor.h;
								openList.add(neighbor);
							}
						}
					}
					}
					//sort the openList so next is always smallest.
				}
			}
		}
			//System.out.print(currentPointer.y + "    " + currentPointer.x);
		System.out.println("path not found in search");
		return null;
	}		
	
	public ArrayList<Integer> getNeighbors(Box current) {
		ArrayList<Integer> wall = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
				wall.add(current.walls[i]);
				//System.out.println(wall.toString());
		}
		return wall;
	}
	public ArrayList<Box> allAction(Box tempBox){
		ArrayList<Integer> currentWalls = getNeighbors(tempBox);
		ArrayList<Box> nextState = new ArrayList<Box>();
		for(int i = 0; i < currentWalls.size(); i++) {
			Box neighbor = new Box();
			if(currentWalls.get(i)==0) {
				if(i == 0) {
					//neighbor is myMaze.mazeArray[currenty][currentx-1] or north wall open
					neighbor = myMaze.mazeArray[tempBox.y][tempBox.x-1];
					nextState.add(neighbor);
				}
				else if(i == 1) {
					//neighbor is myMaze.mazeArray[currentX+1][currentY] or east wall open
					neighbor = myMaze.mazeArray[tempBox.y+1][tempBox.x];
					nextState.add(neighbor);
				}
				else if(i == 2) {
					//neighbor is myMaze.mazeArray[currentX][currentY+1] or south wall open
					neighbor = myMaze.mazeArray[tempBox.y][tempBox.x+1];
					nextState.add(neighbor);
				}
				else {
					//neighbor is myMaze.mazeArray[currentX-1][currentY] or west wall open
					neighbor = myMaze.mazeArray[tempBox.y-1][tempBox.x];
					nextState.add(neighbor);
				}
			}
		}
		return nextState;
	}
	public void calculatePath(Box currentPos) {
		int i = currentPos.getY();
		int j = currentPos.getX();
		estimatedPath = new ArrayList<Box>();
		Random rnd = new Random();
		
		int r = rnd.nextInt(10);
		if(r<5) {
		
			while(j != end.getX()) {
				j++;
				estimatedPath.add(myMaze.mazeArray[i][j]);
			}
			while(i != end.getY()) {
				i++;
				estimatedPath.add(myMaze.mazeArray[i][j]);
			}}
		else {
			while(i != end.getY()) {
				i++;
				estimatedPath.add(myMaze.mazeArray[i][j]);
			}
			while(j != end.getX()) {
				j++;
				estimatedPath.add(myMaze.mazeArray[i][j]);
			}}
		pathsTried.add(estimatedPath);
	}
	public void resetParents() {
		for(int i = 0; i < myMaze.rows; i++ ) {			//rows are going RIGHT/LEFT
			for(int j = 0; j < myMaze.columns; j++) { 	//columns are going DOWN
				Box tempBOX = myMaze.mazeArray[i][j];
				tempBOX.parent = null;
				tempBOX.g = 0;
				tempBOX.f = 0;
				tempBOX.setSearch(0);
				counter = 0;
			}
		}
	}
	public void calculateH(Maze myMaze) {
		for(int i = 0; i < myMaze.rows; i++ ) {			//rows are going RIGHT/LEFT
			for(int j = 0; j < myMaze.columns; j++) { 	//columns are going DOWN
				Box tempBOX = myMaze.mazeArray[i][j];
				tempBOX.h = Math.abs(end.y - tempBOX.y) + Math.abs(end.x - tempBOX.x);
				tempBOX.search = 0;
			}
		}
	}
	
}