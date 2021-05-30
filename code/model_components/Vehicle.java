import java.util.*;  
import java.util.logging.*;
	
public class Vehicle {
	private int row;
	private int column;
	private String type;
	private Queue<Integer> path;  // 0 -> up, 1 -> right, 2 -> down, 3 -> left  --> inverse clockwise
	
	public Vehicle(int row, int column, String type, Queue<Integer> path) {
		this.row = row;
		this.column = column;
		this.type = type;  // TODO transform to enum
		this.path = path;
	}
	
	public boolean canMove(Intersection intersection) {
		int greenDirection = intersection.getGreenDirection();
		int movement = this.path.peek();
		boolean canMove = false;
		switch (movement) {
			case 0:
				canMove = (greenDirection == 2);
				break;
			case 1:
				canMove = (greenDirection == 3);
				break;
			case 2:
				canMove = (greenDirection == 0);
				break;
			case 3:
				canMove = (greenDirection == 1);
				break;
		}
		return canMove;
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
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String toString(){
		return "vehicle " + this.row + " " + this.column + " " + this.type + " " + this.path;  
	}  
}
