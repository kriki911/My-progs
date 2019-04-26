import java.awt.*;
import javax.swing.*;
public class Box implements Comparable<Box>{
	int x,y;
	boolean visited = false;
	int[] walls = new int[] {1,1,1,1};
	//Experimental
	
	public int g; //number of steps from start to current state
	public int h; //number of steps from current state to goal
	public int f; //g + h
	int search;
	//
	
	boolean startPoint = false;
	boolean endPoint = false;
	
	Box parent;
	public Box() {
		//g = Integer.MAX_VALUE;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getSearch() {return search;}
	public int setSearch(int newSearch) {return search = newSearch;}
	public void setWallofBox(int index) {
		walls[index] = 1;
	}
	
	public void removeWallofBox(int index) {
		walls[index] = 0;
	}
	
	public boolean checkWalls() {
		if(walls[0] == 1 && walls[1] == 1
				&& walls[2] == 1 && walls[3] == 1) {
			return true;
		}
		return false;
	}
	//Experimental
	public void setBoxColor() {
		
	}
	
	public void setSart() {
		if(endPoint) {return;}
		startPoint = true;
	}
	
	public void setEnd() {
		if(startPoint) {
			return;
		}
		endPoint = true;
	}
	//
	public void printBox() {
		System.out.println(" " + walls[0] + " ");
		System.out.println(walls[3]+ "  " + walls[1]);
		System.out.println(" " + walls[2] + " ");
		
	}

//	@Override
	public int compareTo(Box arg0) {
		//this.f > arg0.f
		if(this.f < arg0.f) {
			return 1;
		}
		else if(this.f > arg0.f) {
			return -1;
		}
		else { 
			if(this.g < arg0.g) {return 1;}
			return -1;
			}
	}
}
