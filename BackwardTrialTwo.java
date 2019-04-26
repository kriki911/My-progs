
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.io.*;
//Forward


public class BackwardTrialTwo extends JFrame {
	JPanel contentPane;
	static int sizeX = 30;
	static int sizeY = 30;
	int boxSize = 15;
	public static ArrayList<Box> path = new ArrayList<Box>();
	static ArrayList<Box> estimatedPath = new ArrayList<Box>();
	static ArrayList<Box> movedPath = new ArrayList<Box>();
	Box start, end, newStart;
	static Box newEnd;
	static Box thisBox;
	public JButton moveBtn;
	
	
	static Maze myMaze_2 = new Maze(sizeX, sizeY);
	static Maze myMaze = new Maze(sizeX, sizeY); 
	public BackwardTrialTwo() {
		long startTime = System.nanoTime();
		Random rnd = new Random();
		
		int x = rnd.nextInt(sizeX);
		int y = rnd.nextInt(sizeY);
		System.out.println("Goal: " + y + " " + x);
		
		start = myMaze_2.mazeArray[sizeY-1][sizeX-1];  			
		myMaze_2.startBox = start;         	
		start.setSart();
		end = myMaze_2.mazeArray[y][x];
		end.setEnd();
		myMaze_2.endBox = end;
		calculateH(myMaze_2, end);					
		
		setMaze();
		myMaze.mazeArray[sizeY-1][sizeX-1] = start;
		newStart = myMaze.mazeArray[sizeY-1][sizeX-1] ;
		myMaze.startBox = start;
		newStart.setSart();
		//myMaze.mazeArray[y][x] = end;
		newEnd = myMaze.mazeArray[y][x] ;
		newEnd.setEnd();
		myMaze.endBox = newEnd;
		calculateH(myMaze, newEnd);
		
		path = repeatedForward(myMaze, newStart);
		myMaze.setPath(path);
		startUI();
		long endTime = System.nanoTime();
		System.out.println("Took "+((double)(endTime - startTime)/1000000000) + " seconds"); 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BackwardTrialTwo mainMaze = new BackwardTrialTwo();
				mainMaze.setVisible(true);				
			}
		});
	}
	
	public void startUI() {
		setTitle("Maze Display");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setSize((boxSize * sizeY) + 80, (boxSize * sizeX) + 80);
		setBounds(100, 100, (boxSize * sizeY) + 80,(boxSize * sizeX) + 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		contentPane.setLayout(new BorderLayout(0,0));
		setContentPane(contentPane);
		
		MazeDisplay displayMaze = new MazeDisplay(boxSize, myMaze, path);
		contentPane.add(displayMaze, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();

		
		moveBtn = new JButton("Click");
		moveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked");
				ArrayList<Box> endNeighbors = 	allAction(newEnd, myMaze);
				if(endNeighbors.contains(thisBox)) {
					System.out.println("end neighbor");
					return;}
//				if(thisBox == myMaze.mazeArray[newEnd.x+1][newEnd.x] || thisBox == myMaze.mazeArray[newEnd.x][newEnd.x+1]
//						||thisBox == myMaze.mazeArray[newEnd.x][newEnd.x] 
//								||thisBox == myMaze.mazeArray[newEnd.x-1][newEnd.x] 
//										||thisBox == myMaze.mazeArray[newEnd.x][newEnd.x-1] ) {return;}
				moveAgent();
				displayMaze.repaint();
			}
		});
		
		controlPanel.add(moveBtn);
		//MazeDisplay displayMaze = new MazeDisplay(myMaze_2);
		//setContentPane(displayMaze);
		displayMaze.setFocusable(true);	
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		
	}
	
	public static ArrayList<Box> reconstructPath(Box goal) {
//		System.out.println("In construction " );
		ArrayList<Box> path = new ArrayList<Box>();
		while(goal.parent != null) {
			path.add(goal);
			goal = goal.parent;
		}
//		for(Box b : path) {
//			System.out.println(b.y + ", " + b.x);
//		}
		return path;
}
	
	public static void checking(Box newCurrent, Box end, Maze myMaze) {
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
	
	static int counter;
	ArrayList<Box> pathA;
	static ArrayList<Box> pathB;
	ArrayList<Box> visitedNodes = new ArrayList<Box>();
	public static ArrayList<Box> repeatedForward(Maze myMaze , Box curr){
		thisBox = curr;
		movedPath.add(thisBox);
		myMaze.movedPath = movedPath;
		Box newCurrent = curr;
		counter = 0;
		checking(newCurrent, newEnd, myMaze);
		System.out.println("New Current: " + newCurrent.y + ", " + newCurrent.x);
		//calculatePath(newCurrent, newEnd, myMaze);

		//for(Box b: pathB) {System.out.println(b.getX()+","+b.getY());}
		return pathB;
	}


	public static ArrayList<Box> searchMaze (Maze myMaze, Box curr) {
		
		 Comparator<Box> smallFbigG = new Comparator<Box>() {
	            @Override
	            public int compare(Box b1, Box b2) {
	        		if(b1.f > b2.f) {
	        			return 1;
	        		}
	        		else if(b1.f < b2.f) {
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
		int expanded = 0;
		
		while(!openList.isEmpty()) {
			//look at the queue
			currentPointer = openList.remove();

			if(!closedList.contains(currentPointer)) {
			//if currentPointer f value equals target g value, stop
				if(currentPointer.x == newEnd.x && currentPointer.y == newEnd.y ) {
					//System.out.println("this is # of expanded: " + expanded);
				return reconstructPath(newEnd);
			}
				//remove it from openlist
			closedList.add(currentPointer);
			
				//explore the children..
				//check current box for walls. if wall is 0, then it is open. 
				//walls [NORTH, EAST, SOUTH, WEST] THAT'S THE DIRECTION/DISPLAY
			ArrayList<Integer> currentWalls = getNeighbors(currentPointer);

			for(int i = 0; i < currentWalls.size(); i++) {
				Box neighbor = new Box();
				if(currentWalls.get(i) == 0) {
					if(i == 0) {
						//neighbor is myMaze.mazeArray[currenty][currentx-1] or north wall open
						if(currentPointer.x-1 >= 0) {
							neighbor = myMaze.mazeArray[currentPointer.y][currentPointer.x-1];
						}
					}
					else if(i == 1) {
						//neighbor is myMaze.mazeArray[currentX+1][currentY] or east wall open
						if(currentPointer.y+1 < sizeY ) {
							neighbor = myMaze.mazeArray[currentPointer.y+1][currentPointer.x];
							}
					}
					else if(i == 2) {
						//neighbor is myMaze.mazeArray[currentX][currentY+1] or south wall open
						if(currentPointer.x+1 < sizeX ) {
							neighbor = myMaze.mazeArray[currentPointer.y][currentPointer.x+1];
						}
					}
					else  if(i == 3){
						//neighbor is myMaze.mazeArray[currentX-1][currentY] or west wall open
						if(currentPointer.y-1 >= 0) {
							neighbor = myMaze.mazeArray[currentPointer.y-1][currentPointer.x];
						}
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
			}}}}
					//sort the openList so next is always smallest.
		}}}
		System.out.print("Path is null");
			
		return null;
	}	
	
	public static ArrayList<Integer> getNeighbors(Box current) {
		ArrayList<Integer> wall = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
				wall.add(current.walls[i]);
		}
		return wall;
	}
	public static ArrayList<Box> allAction(Box tempBox, Maze myMaze){
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
	
	public void initializeBox() {
		for(int i = 0; i < sizeY; i++) {
			for(int j = 0 ; j < sizeX; j++) {
				myMaze.mazeArray[i][j] = new Box();
				myMaze.mazeArray[i][j].x = j;
				myMaze.mazeArray[i][j].y = i;
				myMaze.mazeArray[i][j].walls = new int[] {0,0,0,0};
				if(i==0) {myMaze.mazeArray[i][j].walls[3]=1;}
				if(j==0) {myMaze.mazeArray[i][j].walls[0]=1;}
				if(i==sizeY-1) {myMaze.mazeArray[i][j].walls[1]=1;}
				if(j==sizeX-1) {myMaze.mazeArray[i][j].walls[2]=1;}
			
			}
		}
	}
	public static void resetParents() {
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
	public void setMaze() {
		myMaze.mazeArray = new Box[sizeY][sizeX];
		initializeBox();

		ArrayList<Box> neighbours = allAction(start, myMaze_2);
		for(Box n : neighbours) {	
			int r = n.y; int c = n.x;
			myMaze.mazeArray[r][c].walls = n.walls;	
		}
		
	}

	public static void calculatePath(Box currentPos, Box end, Maze myMaze) {
		estimatedPath = new ArrayList<Box>();
		int i = currentPos.getY();
		int j = currentPos.getX();
		
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

	}
	public static void moveAgent() {
		thisBox = path.get(path.size() - 1);
		path = repeatedForward(myMaze, thisBox);
		myMaze.setPath(path);
		
		//for(Box b : path) {System.out.println(+b.y+" , "+b.x);}
		Box newCurrent = (myMaze_2.mazeArray[thisBox.getY()][thisBox.getX()]);
		ArrayList<Box> neighbours = allAction(newCurrent, myMaze_2);
		for(Box n : neighbours) {	
			int r = n.y; int c = n.x;
			myMaze.mazeArray[r][c].walls = n.walls;	
		}
				
		
	}

	public void calculateH(Maze myMaze, Box end) {
		for(int i = 0; i < myMaze.rows; i++ ) {			//rows are going RIGHT/LEFT
			for(int j = 0; j < myMaze.columns; j++) { 	//columns are going DOWN
				Box tempBOX = myMaze.mazeArray[i][j];
				tempBOX.h = Math.abs(end.y - tempBOX.y) + Math.abs(end.x - tempBOX.x);
				tempBOX.search = 0;
			}
		}
	}
	
}