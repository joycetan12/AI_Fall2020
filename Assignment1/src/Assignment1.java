// Author: Joyce Tan

import java.util.Hashtable;
import java.util.ArrayList;

public class Assignment1 {
	
	public static void main(String[] args) {
		
		// storing each tile in easy board in hash table with their coordinates 
		int[][] easy = {{1,3,4}, {8,6,2}, {7,0,5}};
		Hashtable<Integer,Coord> easyBoard = new Hashtable<>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Coord tile = new Coord(i,j);
				easyBoard.put(easy[i][j], tile);
			}
		}

		// storing each tile in medium board in hash table with their coordinates 
		int[][] medium = {{2,8,1}, {0,4,3}, {7,6,5}};
		Hashtable<Integer,Coord> mediumBoard = new Hashtable<>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Coord tile = new Coord(i,j);
				mediumBoard.put(medium[i][j], tile);
			}
		}
		
		// storing each tile in hard board in hash table with their coordinates 
		int[][] hard = {{2,8,1}, {4,6,3}, {0,7,5}};
		Hashtable<Integer,Coord> hardBoard = new Hashtable<>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Coord tile = new Coord(i,j);
				hardBoard.put(hard[i][j], tile);
			}
		}
		
		// storing each tile in worst board in hash table with their coordinates 
		int[][] worst = {{5,6,7}, {4,0,8}, {3,2,1}};
		Hashtable<Integer,Coord> worstBoard = new Hashtable<>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Coord tile = new Coord(i,j);
				worstBoard.put(worst[i][j], tile);
			}
		}
		
		// storing each tile in goal board in hash table with their coordinates 
		int[][] goal = {{1,2,3}, {8,0,4},{7,6,5}};
		Hashtable<Integer,Coord> goalBoard = new Hashtable<>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Coord tile = new Coord(i,j);
				goalBoard.put(goal[i][j], tile);
			}
		}
		
		System.out.println("easy board A* search misplaced tiles:");
		a_star_search(easyBoard,goalBoard,false);
		System.out.print("\n");
		
		System.out.println("easy board A* search manhattan:");
		a_star_search(easyBoard,goalBoard,true);
		System.out.print("\n");
		
		System.out.println("easy board IDA* search manhattan:");
		IDA_star_search(easyBoard,goalBoard);
		System.out.print("\n");
		
		System.out.println("easy board DFBnB search manhattan:");
		DFBnB_search(easyBoard, goalBoard, Integer.MAX_VALUE);
		System.out.print("\n");
		
		System.out.println("medium board A* search misplaced tiles:");
		a_star_search(mediumBoard,goalBoard,false);
		System.out.print("\n");
		
		System.out.println("medium board A* search manhattan:");
		a_star_search(mediumBoard,goalBoard,true);
		System.out.print("\n");
		
		System.out.println("medium board IDA* search manhattan:");
		IDA_star_search(mediumBoard,goalBoard);
		System.out.print("\n");
	
		System.out.println("medium board DFBnB search manhattan:");
//		DFBnB_search(mediumBoard, goalBoard, Integer.MAX_VALUE);
		System.out.println("OUT OF MEMORY ERROR");
		System.out.print("\n");

		System.out.println("hard board A* search misplaced tiles:");
		a_star_search(hardBoard,goalBoard,false);
		System.out.print("\n");
		
		System.out.println("hard board A* search manhattan:");
		a_star_search(hardBoard,goalBoard,true);
		System.out.print("\n");
		
		System.out.println("hard board IDA* search manhattan:");
		IDA_star_search(hardBoard,goalBoard);
		System.out.print("\n");

		System.out.println("hard board DFBnB search manhattan:");
//		DFBnB_search(hardBoard, goalBoard, Integer.MAX_VALUE);
		System.out.println("OUT OF MEMORY ERROR");
		System.out.print("\n");

		System.out.println("worst board A* search misplaced tiles:");
//		a_star_search(worstBoard,goalBoard,false);
		System.out.println("TAKES TOO LONG TO SOLVE");
		System.out.print("\n");
		
		System.out.println("worst board A* search manhattan:");
		a_star_search(worstBoard,goalBoard,true);
		System.out.print("\n");
		
		System.out.println("worst board IDA* search manhattan:");
//		IDA_star_search(worstBoard,goalBoard);
		System.out.println("OUT OF MEMORY ERROR");
		System.out.print("\n");
		
		System.out.println("worst board DFBnB search manhattan:");
//		DFBnB_search(worstBoard, goalBoard, Integer.MAX_VALUE);
		System.out.println("OUT OF MEMORY ERROR");
		System.out.print("\n");
	}		
	
	// A* search where h(n) is the misplaced tiles when boolean manhattan = false
	// A* search where h(n) is the manhattan heuristic function when boolean manhattan = true
	public static void a_star_search(Hashtable<Integer,Coord> start, Hashtable<Integer,Coord> goal, boolean manhattan) {
		
		// start keeping track of time
		long startTime = System.currentTimeMillis();
		
		// create the open list of nodes, initially containing only the starting node
		ArrayList<Node> openList = new ArrayList<>();
		Node firstNode = new Node(start);
		openList.add(firstNode);
		
		// create the closed list (list of expanded nodes)
		ArrayList<Node> closedList = new ArrayList<>();
		
		// the best node is the first node when we start the puzzle
		Node bestNode = firstNode;
		boolean done = false;
		
		while(!done) {
			// remove best node (lowest f value) from open list and add to closed list
			bestNode = openList.remove(0);
			closedList.add(bestNode);
			
			// get the x and y location of empty tile 
			Hashtable<Integer,Coord> currentState = bestNode.getState();
			int x = currentState.get(0).getXCoord();
			int y = currentState.get(0).getYCoord();

			// check board boundaries - if you can move empty tile "right" 
			if(y+1 <= 2) {
				Hashtable<Integer,Coord> rightChildState= swapTiles(currentState, x, y, x, y+1);
				Node rightChildNode = new Node(bestNode, "right", rightChildState, goal, manhattan);
				boolean existInClosed = false;
				boolean existInOpen = false;
				
				// check if current state after the move "right" already exist in the closed list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				// and move the node from the closed list to the open list
				for(int i = 0; i < closedList.size(); i++) {
					existInClosed = checkStates(closedList.get(i).getState(), rightChildState);
					if(existInClosed && rightChildNode.getG() < closedList.get(i).getG()) {
						closedList.get(i).updateParent(bestNode);
						openList.add(closedList.remove(i));
						break;
					}
					else continue;
				}
				
				// check if current state after the move "right" already exist in the open list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				for(int i = 0; i < openList.size(); i++) {
					existInOpen = checkStates(openList.get(i).getState(), rightChildState);
					if(existInOpen && rightChildNode.getG() < openList.get(i).getG()) {
						openList.get(i).updateParent(bestNode);
						break;
					}
					else continue;
				}
				
				// if the current state did not exist in either the closed list or open list then add this node to the open list
				if(!existInClosed && !existInOpen) 
					openList.add(rightChildNode);
			}
			
			// check board boundaries - if you can move empty tile "down"
			if(x+1 <= 2) {
				Hashtable<Integer,Coord> downChildState= swapTiles(currentState, x, y, x+1, y);
				Node downChildNode = new Node(bestNode, "down", downChildState, goal, manhattan);
				boolean existInClosed = false;
				boolean existInOpen = false;
				
				// check if current state after the move "down" already exist in the closed list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				// and move the node from the closed list to the open list
				for(int i = 0; i < closedList.size(); i++) {
					existInClosed = checkStates(closedList.get(i).getState(), downChildState);
					if(existInClosed && downChildNode.getG() < closedList.get(i).getG()) {
						closedList.get(i).updateParent(bestNode);
						openList.add(closedList.remove(i));
						break;
					}
					else continue;
				}
				
				// check if current state after the move "down" already exist in the open list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				for(int i = 0; i < openList.size(); i++) {
					existInOpen = checkStates(openList.get(i).getState(), downChildState);
					if(existInOpen && downChildNode.getG() < openList.get(i).getG()) {
						openList.get(i).updateParent(bestNode);
						break;
					}
					else continue;
				}
				
				// if the current state did not exist in either the closed list or open list then add this node to the open list
				if(!existInClosed && !existInOpen)
					openList.add(downChildNode);	
			}	
			
			// check board boundaries - if you can move empty tile "left" 
			if(y-1 >= 0) {
				Hashtable<Integer,Coord> leftChildState = swapTiles(currentState, x, y, x, y-1);
				Node leftChildNode = new Node(bestNode, "left", leftChildState, goal, manhattan);
				boolean existInClosed = false;
				boolean existInOpen = false;
				
				// check if current state after the move "left" already exist in the closed list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				// and move the node from the closed list to the open list
				for(int i = 0; i < closedList.size(); i++) {
					existInClosed = checkStates(closedList.get(i).getState(), leftChildState);
					if(existInClosed && leftChildNode.getG() < closedList.get(i).getG()) {
						closedList.get(i).updateParent(bestNode);
						openList.add(closedList.remove(i));
						break;
					}
					else continue;
				}
				
				// check if current state after the move "left" already exist in the open list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				for(int i = 0; i < openList.size(); i++) {
					existInOpen = checkStates(openList.get(i).getState(), leftChildState);
					if(existInOpen && leftChildNode.getG() < openList.get(i).getG()) {
						openList.get(i).updateParent(bestNode);
						break;
					}
					else continue;
				}
				
				// if the current state did not exist in either the closed list or open list then add this node to the open list
				if(!existInClosed && !existInOpen) 
					openList.add(leftChildNode);
			}
			
			// check board boundaries - if you can move empty tile "up"
			if(x-1 >= 0) {
				Hashtable<Integer,Coord> upChildState = swapTiles(currentState, x, y, x-1, y);
				Node upChildNode = new Node(bestNode, "up", upChildState, goal, manhattan);
				boolean existInClosed = false;
				boolean existInOpen = false;
				
				// check if current state after the move "up" already exist in the closed list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				// and move the node from the closed list to the open list
				for(int i = 0; i < closedList.size(); i++) {
					existInClosed = checkStates(closedList.get(i).getState(), upChildState);
					if(existInClosed && upChildNode.getG() < closedList.get(i).getG()) {
						closedList.get(i).updateParent(bestNode);
						openList.add(closedList.remove(i));
						break;
					}
					else continue;
				}
				
				// check if current state after the move "up" already exist in the open list
				// if current state exists in the closed list and the current g value is lower then we update the parent of the node
				for(int i = 0; i < openList.size(); i++) {
					existInOpen = checkStates(openList.get(i).getState(), upChildState);
					if(existInOpen && upChildNode.getG() < openList.get(i).getG()) {
						openList.get(i).updateParent(bestNode);
						break;
					}
					else continue;
				}
		
				// if the current state did not exist in either the closed list or open list then add this node to the open list
				if(!existInClosed && !existInOpen)
					openList.add(upChildNode);
			}
			
			//sort open list by f values and check if you are done with the puzzle
			openList.sort(new NodeCompare());
			done = checkStates(bestNode.getState(),goal);
		}
		
		// find difference between start time and end time 
		long endTime = System.currentTimeMillis();
		long timeUsed = endTime - startTime;
		
		System.out.println("Moves: " + bestNode.getPath());
		System.out.println("# of moves = " + bestNode.getG());
		System.out.println("# of nodes expanded = " + closedList.size());
		System.out.println("Execution time in milliseconds: " + timeUsed);
	}
	
	// Iterative deepening A* search with the Manhattan heuristic function
	public static void IDA_star_search(Hashtable<Integer,Coord> start, Hashtable<Integer,Coord> goal) {
		
		// start keeping track of time
		long startTime = System.currentTimeMillis();
		
		// current level list to keep track of what nodes are on current level
		ArrayList<Node> currentLevelList = new ArrayList<>();
		Node startNode = new Node(start);
		currentLevelList.add(startNode);
		
		int bound = startNode.getF();
		boolean done = false;
		int expandedNodes = 0;
		
		while(!done) {
			// calls DFBnB search with bound equal to current lowest f(n) on current level
			// returns number of nodes expanded each time its called to keep track of how many nodes expanded in total for IDA* search
			expandedNodes += DFBnB_search(start, goal, bound);
		
			// child list to keep track of what nodes are on the next level
			ArrayList<Node> childList = new ArrayList<>();
			
			// find the children of each node on the current level of the graph you are on 
			for(int i = 0; i < currentLevelList.size(); i++) {
				Node currentNode = currentLevelList.get(i);
				
				// check if current state is equal to goal state, if true print the solution
				if(checkStates(currentNode.getState(),goal)) {
					// find difference between start time and end time 
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					System.out.println("Moves: " + currentNode.getPath());
					System.out.println("# of moves: " + currentNode.getG());
					System.out.println("# of nodes expanded = " + expandedNodes);
					System.out.println("Execution time in milliseconds: " + timeUsed);
					done = true;
					break;
				}
					
				// get the x and y location of empty tile 
				Hashtable<Integer,Coord> currentState = currentNode.getState();
				int x = currentState.get(0).getXCoord();
				int y = currentState.get(0).getYCoord();
				
				// right child
				if(y+1 <= 2) {
					Hashtable<Integer,Coord> rightChildState = swapTiles(currentState, x, y, x, y+1);
					Node rightChildNode = new Node(currentNode, "right", rightChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the neighbor node is not a parent then add it to the child list
					if(rightChildNode.getParent() != null && rightChildState == rightChildNode.getParent().getState())
						continue;
					else 
						childList.add(rightChildNode);
				}		
				
				// down child
				if(x+1 <= 2) {
					Hashtable<Integer,Coord> downChildState = swapTiles(currentState, x, y, x+1, y);
					Node downChildNode = new Node(currentNode, "down", downChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the neighbor node is not a parent then add it to the child list 
					if(downChildNode.getParent() != null && downChildState == downChildNode.getParent().getState())
						continue;
					else 
						childList.add(downChildNode);
				}	
				
				// left child
				if(y-1 >= 0) {
					Hashtable<Integer,Coord> leftChildState = swapTiles(currentState, x, y, x, y-1);
					Node leftChildNode = new Node(currentNode, "left", leftChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the neighbor node is not a parent then add it to the child list 
					if(leftChildNode.getParent() != null && leftChildState == leftChildNode.getParent().getState())
						continue;
					else 
						childList.add(leftChildNode);
				}
				
				// up child
				if(x-1 >= 0) {
					Hashtable<Integer,Coord> upChildState = swapTiles(currentState, x, y, x-1, y);
					Node upChildNode = new Node(currentNode, "up", upChildState, goal, true);
				
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the neighbor node is not a parent then add it to the child list 
					if(upChildNode.getParent() != null && upChildState == upChildNode.getParent().getState())
						continue;
					else 
						childList.add(upChildNode);
				}	
			}
			
			// for all the child nodes on the current level look for the lowest f(n) and set the bound equal to the lowest f(n)
			int minF = Integer.MAX_VALUE;
			for(int i = 0; i < childList.size(); i++) {
				Node currentNode = childList.get(i);
				if(currentNode.getF() < minF)
					minF = currentNode.getF();
			}
			bound = minF;
			
			// set the current level list of nodes equal to the next level of nodes, to keep track of the nodes you have in the next level
			currentLevelList = childList;
		}
	}
	
	// Depth-first Branch and Bound with the Manhattan heuristic function
	public static int DFBnB_search(Hashtable<Integer,Coord> start, Hashtable<Integer,Coord> goal, int bound) {
		
		// start keeping track of time
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		
		// create the open list of nodes, initially containing only the starting node
		ArrayList<Node> openList = new ArrayList<>();
		Node currentNode = new Node(start);
		openList.add(currentNode);
		
		int L = bound;
		Node solutionNode = new Node(start);
		int expandedNodes = 0;
		
		while(!openList.isEmpty()) {
			currentNode = openList.remove(0);
			
			// get the x and y location of empty tile 
			Hashtable<Integer,Coord> currentState = currentNode.getState();
			int x = currentState.get(0).getXCoord();
			int y = currentState.get(0).getYCoord();
			
			// check if the current node is equal to the goal state
			// if the states are equal, then bound is equal to the minimum of the current bound and the current cost of the node 
			// update best solution and update time to find this solution
			if(checkStates(currentState, goal)) {
				L = Math.min(L,currentNode.getF());
				solutionNode = currentNode;
				endTime = System.currentTimeMillis();
			}
			else {
				// expand current node and find all the child of the current node
				expandedNodes += 1;
				ArrayList<Node> childList = new ArrayList<>();
				
				// right child
				if(y+1 <= 2) {
					Hashtable<Integer,Coord> rightChildState = swapTiles(currentState, x, y, x, y+1);
					Node rightChildNode = new Node(currentNode, "right", rightChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the f(n) of the child node is greater than the current bound we do not add it to the child list
					if(rightChildNode.getParent() != null && rightChildState == rightChildNode.getParent().getState())
						continue;
					if(rightChildNode.getF() > L) 
						continue;
					else 
						childList.add(rightChildNode);
				}	
				
				// down child
				if(x+1 <= 2) {
					Hashtable<Integer,Coord> downChildState = swapTiles(currentState, x, y, x+1, y);
					Node downChildNode = new Node(currentNode, "down", downChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the f(n) of the child node is greater than the current bound we do not add it to the child list
					if(downChildNode.getParent() != null && downChildState == downChildNode.getParent().getState())
						continue;
					if(downChildNode.getF() > L) 
						continue;
					else 
						childList.add(downChildNode);
				}	
				
				// left child
				if(y-1 >= 0) {
					Hashtable<Integer,Coord> leftChildState = swapTiles(currentState, x, y, x, y-1);
					Node leftChildNode = new Node(currentNode, "left", leftChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the f(n) of the child node is greater than the current bound we do not add it to the child list
					if(leftChildNode.getParent() != null && leftChildState == leftChildNode.getParent().getState())
						continue;
					if(leftChildNode.getF() > L) 
						continue;
					else 
						childList.add(leftChildNode);
				}	
				
				// up child
				if(x-1 >= 0) {
					Hashtable<Integer,Coord> upChildState = swapTiles(currentState, x, y, x-1, y);
					Node upChildNode = new Node(currentNode, "up", upChildState, goal, true);
					
					// check to make sure the parent of the current node is not considered a child or the current node 
					// if the f(n) of the child node is greater than the current bound we do not add it to the child list
					if(upChildNode.getParent() != null && upChildState == upChildNode.getParent().getState())
						continue;
					if(upChildNode.getF() > L) 
						continue;
					else 
						childList.add(upChildNode);
				}	
				
				// sort the children of the current node by lowest f(n) first then add them to the beginning of the open list
				childList.sort(new NodeCompare());
				for(int i = childList.size() - 1; i >= 0; i--) {
					openList.add(0, childList.get(i));
				}
			}	
		}
		
		// do not print out the solution when called by IDA* search method
		if(bound == Integer.MAX_VALUE) {
			long timeUsed = endTime - startTime;
			System.out.println("Moves: " + solutionNode.getPath());
			System.out.println("# of moves: " + solutionNode.getG());
			System.out.println("# of nodes expanded = " + expandedNodes);
			System.out.println("Execution time in milliseconds: " + timeUsed);
		}
		
		// return count of expanded nodes for when IDA* search calls this method to keep track of the number of expanded nodes for IDA* search
		return expandedNodes;
	}
	
	// swap empty tile with another tile 
	public static Hashtable<Integer,Coord> swapTiles(Hashtable<Integer,Coord> currentState, int x, int y, int newX, int newY) {
		Hashtable<Integer,Coord> newState = (Hashtable<Integer, Coord>) currentState.clone();
		
		// find the value of the tile located in (newX,newY)
		// then replace the location of this tile with the location of the empty tile
		for(int i = 0; i <= 8; i++) {
			if(newState.get(i).getXCoord() == newX && newState.get(i).getYCoord() == newY) {
				Coord newCoord = new Coord(x,y);
				newState.replace(i,newCoord);
				break;
			}
			else continue;
		}
		
		// replace the location of the blank tile with the (newX,newY) location
		Coord newBlankCoord = new Coord(newX,newY);
		newState.replace(0, newBlankCoord);
		
		return newState;	
	}
	
	// check if two states are equal to each other
	public static boolean checkStates(Hashtable<Integer,Coord> currentState, Hashtable<Integer,Coord> goalState) {
		boolean equal = false;
		
		// check if every tile on each board are equal to each other
		for(int i = 0; i < 8; i++) {
				if(currentState.get(i).getXCoord() == goalState.get(i).getXCoord() && currentState.get(i).getYCoord() == goalState.get(i).getYCoord()) {
					equal = true;
					continue;
				}
				else {
					equal = false; 
					break;
				}
		}
		
		return equal;
	}
}
