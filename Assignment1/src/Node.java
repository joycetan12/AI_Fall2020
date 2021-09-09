// Author: Joyce Tan

import java.util.Hashtable;

public class Node {
	private String path;
	private Node parent;
	private Hashtable<Integer,Coord> state;
	private int g;
	private int h;
	private int f;
	
	// constructor for start state node
	Node(Hashtable<Integer,Coord> state) {
		this.state = state;
		g = 0;
		h = 0;
		f = 0;
	}
	
	// constructor for all nodes with a parent
	Node(Node parent, String move, Hashtable<Integer,Coord> currentState, Hashtable<Integer,Coord> goalState, boolean manhattan){
		this.parent = parent;
		
		// do not include null in the string of moves 
		if(parent.path == null)
			path = move;
		else
			path = parent.path + " " + move;
		
		state = currentState; 
		
		g = parent.getG() + 1;
		
		// calculate h depending on which herustic function you are using
		if(manhattan)
			h = calculateManhattanH(currentState,goalState);
		else 
			h = calculateMisplacedH(currentState,goalState);
		
		f = g + h;
	}
	
	public String getPath() {
		return path;
	}
	public Node getParent() {
		return parent;
	}
	
	public int getF() {
		return f;
	}
	
	public int getH() {
		return h;
	}
	
	public int getG() {
		return g;
	}
	
	public Hashtable<Integer,Coord> getState() {
		return state;
	}
	
	// calculate h by using the manhattan heuristic
	public int calculateManhattanH(Hashtable<Integer,Coord> currentState, Hashtable<Integer,Coord> goalState) {
		int h = 0;
		
		// for each tile find the manhattan distance from the current tile to the goal tile and add the distance to the h value 
		for(int i = 1; i <= 8; i++) {
			int currentX = currentState.get(i).getXCoord();
			int currentY = currentState.get(i).getYCoord();
			int goalX = goalState.get(i).getXCoord();
			int goalY = goalState.get(i).getYCoord();
			
			int diffX = Math.abs(currentX - goalX);
			h += diffX;
			
			int diffY = Math.abs(currentY - goalY);
			h += diffY;
		}

		return h;
	}
	
	// calculate h by using the misplaced heuristic 
	public int calculateMisplacedH(Hashtable<Integer,Coord> currentState, Hashtable<Integer,Coord> goalState) {
		int h = 0;
		
		// for each tile check if the tile is in the right location, if the tile is misplaced then add one to the h value 
		for(int i = 1; i <= 8; i++) {
			if(currentState.get(i).getXCoord() == goalState.get(i).getXCoord() && currentState.get(i).getYCoord() == goalState.get(i).getYCoord()) 
				continue;
			else h = h + 1;
		}
		
		return h;
	}
	
	// update node with a new parent and also update the new g and f values
	public void updateParent(Node newParent) {
		parent = newParent;
		g = parent.getG() + 1;
		f = g + h;
	}
	
}
