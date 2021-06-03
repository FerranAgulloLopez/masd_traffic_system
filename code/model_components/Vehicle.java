import java.util.*;  
import java.util.logging.*;
	
public class Vehicle {
	private Road road;
	private String type;
	private Queue<Integer> path;  // moving to top/right: 0 -> up, 1 -> right, 2 -> down, 3 -> left  --> inverse clockwise
								  // moving to bottom/left: 4 -> up, 5 -> right, 6 -> down, 7 -> left  --> inverse clockwise
	
	public Vehicle(Road road, String type, Queue<Integer> path) {
		this.road = road;
		this.type = type;
		this.path = path;
	}
	
	public boolean canMove(Intersection intersection) {
		int greenDirection = intersection.getGreenDirection();
		int movement = this.path.peek();
		boolean canMove = false;
		if (this.road.isVertical()) {
			if (movement < 4) {
				canMove = (greenDirection == 2);
			} else {
				canMove = (greenDirection == 0);
			}
		} else {
			if (movement < 4) {
				canMove = (greenDirection == 3);
			} else {
				canMove = (greenDirection == 1);
			}
		}
		return canMove;
	}
	
	public boolean move(Map<Integer, Road> roads) {
		int movement = this.path.poll();
		if (road.isVertical()) {
			switch (movement) {
				case 0:
					this.road = roads.get(Road.generateId(true, this.road.getRow() - 1, this.road.getColumn()));
					break;
				case 1:
					this.road = roads.get(Road.generateId(false, this.road.getRow(), this.road.getColumn()));
					break;
				case 2:
					// no change
					break;
				case 3:
					this.road = roads.get(Road.generateId(false, this.road.getRow(), this.road.getColumn() - 1));
					break;
				case 4:
					// no change
					break;
				case 5:
					this.road = roads.get(Road.generateId(false, this.road.getRow() + 1, this.road.getColumn()));
					break;
				case 6:
					this.road = roads.get(Road.generateId(true, this.road.getRow() + 1, this.road.getColumn()));
					break;
				case 7:
					this.road = roads.get(Road.generateId(false, this.road.getRow() + 1, this.road.getColumn() - 1));
					break;
			}
		} else {
			switch (movement) {
				case 0:
					this.road = roads.get(Road.generateId(true, this.road.getRow() - 1, this.road.getColumn() + 1));
					break;
				case 1:
					this.road = roads.get(Road.generateId(false, this.road.getRow(), this.road.getColumn() + 1));
					break;
				case 2:
					this.road = roads.get(Road.generateId(true, this.road.getRow(), this.road.getColumn() + 1));
					break;
				case 3:
					// no change
					break;
				case 4:
					this.road = roads.get(Road.generateId(true, this.road.getRow() - 1, this.road.getColumn()));
					break;
				case 5:
					// no change
					break;
				case 6:
					this.road = roads.get(Road.generateId(true, this.road.getRow(), this.road.getColumn()));
					break;
				case 7:
					this.road = roads.get(Road.generateId(false, this.road.getRow(), this.road.getColumn() - 1));
					break;
			}
		}
		
		if (this.path.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Road getRoad() {
		return this.road;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String toString(){
		return "vehicle " + this.road + " " + this.type + " " + this.path;  
	}  
}
