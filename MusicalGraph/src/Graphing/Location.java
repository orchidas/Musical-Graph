package Graphing;
//each ellipse represents a vertex (chord) and a button.
//The X and Y coordinates of the button are represented by the class, Location.
public class Location {
	private int x;
	private int y;
	//private String name;
	
	public Location(){
		x = 0;
		y = 0;
		//name = "";
	}
	
	public Location(int x, int y){
		this.x = x;
		this.y = y;
		//this.name = name;
	}
	
	public int getXPos(){
		return x;
	}
	public int getYPos(){
		return y;
	}
	

}
