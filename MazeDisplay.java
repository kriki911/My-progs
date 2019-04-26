import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class MazeDisplay extends JPanel implements KeyListener {
	
	public Maze myMaze ;
	int offSetX = 10;
	int offSetY = 10;
	int sizeB = 15;
	ArrayList<Box> path = new ArrayList<Box>();
	ArrayList<Box> movedPath = new ArrayList<Box>();
	ArrayList<ArrayList<Box>> pathsTried = new ArrayList<ArrayList<Box>>();
	int pColumn, pRow, oldColumn, oldRow;
	boolean remove;
	
	public MazeDisplay() {
		// TODO Auto-generated constructor stub
		myMaze = new Maze();
		pColumn = offSetX + sizeB / 2;
		pRow = offSetY + sizeB /2;
		oldColumn = pColumn;
		oldRow = pRow;
		
	}

	public MazeDisplay(Maze myMaze) {
		// TODO Auto-generated constructor stub
		this.myMaze = myMaze;
		pColumn = offSetX + sizeB / 2;
		pRow = offSetY + sizeB /2;
		oldColumn = pColumn;
		oldRow = pRow;
		
	}
	public MazeDisplay( int boxSize, Maze myMaze,ArrayList<Box> p) {
		// TODO Auto-generated constructor stub
		this.myMaze = myMaze;
		sizeB = boxSize;
		pColumn = offSetX + sizeB / 2;
		pRow = offSetY + sizeB /2;
		oldColumn = pColumn;
		oldRow = pRow;
		path = myMaze.getPath();
	}
	public MazeDisplay( int boxSize, Maze myMaze, ArrayList<Box> p,ArrayList<ArrayList<Box>> pt,  ArrayList<Box> mp) {
		// TODO Auto-generated constructor stub
		this.myMaze = myMaze;
		sizeB = boxSize;
		path = p;
		pathsTried = pt;
		movedPath = mp;
		pColumn = offSetX + sizeB / 2;
		pRow = offSetY + sizeB /2;
		oldColumn = pColumn;
		oldRow = pRow;
		
	}
	private void traceMaze(Graphics g) {
		//Collections.reverse(path);
		Graphics2D g2d = (Graphics2D) g;

		int x,y;
		for(int i = 0; i < myMaze.rows ; i++) {
			y = i * sizeB + offSetY;
			for(int j = 0; j < myMaze.columns; j++) {
			
				x = j * sizeB + offSetX;
				Box tempBox = myMaze.mazeArray[i][j];
				for(ArrayList<Box> estimatedPath: pathsTried) {
				if(estimatedPath.contains(tempBox)) {g2d.setColor(Color.gray);
													g2d.fillRect(y+ 2, x + 2, sizeB/2 + 1 , sizeB/2 + 1);}}
				
			
				if(movedPath.contains(tempBox)) {g2d.setColor(Color.cyan);
														g2d.fillRect(y+ 2, x + 2, sizeB/2 + 1 , sizeB/2 + 1);}
					
					
				if(path.contains(tempBox)) {
					if(tempBox.endPoint) {
						g2d.setColor(Color.blue);
						g2d.fillRect(y + 2, x + 2, sizeB /2 , sizeB /2);}
					else {
						g2d.setColor(Color.orange);
						g2d.fillRect(y+ 2, x + 2, sizeB/2 + 1 , sizeB/2 + 1);
					}
				}
				
			}
		}
		int endX, endY;
		g2d.setColor(Color.blue);
		
		endX = myMaze.endBox.x * sizeB + offSetX;
		endY = myMaze.endBox.y * sizeB + offSetY;
		g2d.fillRect(endY, endX, sizeB /2 , sizeB /2);
		
		int startX, startY;
		g2d.setColor(Color.green);
		
		startX = myMaze.startBox.x * sizeB + offSetX;
		startY = myMaze.startBox.y * sizeB + offSetY;
		g2d.fillRect(startY, startX, sizeB/2, sizeB /2);
	}
	private void drawMaze(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.red);
		//Experimental
		
		Dimension size = getSize();
		Insets insets = getInsets();
		
		int height = size.height - insets.top - insets.bottom;
		int width = size.width- insets.left - insets.right;
		
		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, width, height);
		Path2D mazeShape = new Path2D.Double();
		g2d.setStroke(new BasicStroke(2));
		int x,y;
		//System.out.println("entering the for loop in mazedisplay");
		for(int i = 0; i < myMaze.rows ; i++) {   //for(int i = 0; i < myMaze.rows; i++) {
			//System.out.println("outer loop in mazedisplay");
			y = i * sizeB + offSetY;
			for(int j = 0; j < myMaze.columns; j++) { //for(int j = 0; j < myMaze.columns; j++) {
				//System.out.println("inner loop in mazedisplay");
				x = j * sizeB + offSetX;
				
				
				if(myMaze.mazeArray[i][j].walls[0]==1) {
					mazeShape.moveTo(y,x);
					mazeShape.lineTo(y + sizeB, x);
					g2d.drawLine(y , x,  y + sizeB, x);
				}
				if(myMaze.mazeArray[i][j].walls[1]==1) {
					mazeShape.moveTo(y + sizeB, x);
					mazeShape.lineTo(y + sizeB, x + sizeB);
					g2d.drawLine(y + sizeB , x,  y + sizeB, x + sizeB);
				}
				if(myMaze.mazeArray[i][j].walls[2]==1) {
					mazeShape.moveTo(y,x + sizeB);
					mazeShape.lineTo(y + sizeB,x+sizeB);
					g2d.drawLine(y, x+ sizeB,  y + sizeB, x + sizeB);
				}
				if(myMaze.mazeArray[i][j].walls[3]==1) {
					mazeShape.moveTo(y,x);
					mazeShape.lineTo(y,x+ sizeB);
					g2d.drawLine(y, x, y, x + sizeB);
				}
			}
		}
		
	}
    synchronized protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        path = myMaze.getPath();
        movedPath = myMaze.movedPath;
        if(path !=null) {
        for(Box b : path) {System.out.println(+b.y+" , "+b.x);}}
        traceMaze(g);

    }

	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		System.out.println("something");
		if(key.getKeyCode() == key.VK_SPACE ||key.getKeyCode() == key.VK_ENTER) {
			System.out.println("space");
			TrialTwo.moveAgent();
			repaint();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
