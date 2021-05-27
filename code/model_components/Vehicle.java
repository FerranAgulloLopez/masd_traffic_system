import java.util.*;  
import java.util.logging.*;
	
public class Vehicle {
	private int row;
	private int column;
	private String type;
	private Queue<Integer> path;  // inverse clock -> north, west, south and east
	
	public Vehicle(int row, int column, String type, Queue<Integer> path) {
		this.row = row;
		this.column = column;
		this.type = type;  // TODO transform to enum
		this.path = path;
	}
	
	public boolean move() {
		int movement = this.path.poll();
		switch (movement) {
			case 0:
				this.row -= 1;
				break;
			case 1:
				this.column -= 1;
				break;
			case 2:
				this.row += 1;
				break;
			case 3:
				this.column += 1;
				break;
		}
		
		if (this.path.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getType() {
		return this.type;
	}
	
	public String toString(){
		return "vehicle " + this.row + " " + this.column + " " + this.type + " " + this.path;  
	}  
}
