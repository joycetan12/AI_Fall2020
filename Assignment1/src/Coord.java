// Author: Joyce Tan

public class Coord {
	private int xCoord;
	private int yCoord;
	
	Coord(int x, int y){
		xCoord = x;
		yCoord = y;
	}
	
	public int getXCoord() {
		return xCoord;
	}
	
	public int getYCoord() {
		return yCoord;
	}
	
	@Override
	public String toString() {
		return "(" + xCoord + "," + yCoord + ")";
	}
}
