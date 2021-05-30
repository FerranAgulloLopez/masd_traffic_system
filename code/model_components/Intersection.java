import java.util.*;  


public class Intersection {
	private int id;
	private int row;
	private int column;
	private int greenDirection;  // 0 -> up, 1 -> right, 2 -> down, 3 -> left  --> inverse clockwise
	
	private static int numberRows;
	private static int numberColumns;
	
	public Intersection(int row, int column, int greenDirection) {
		this.id = this.generateId(row, column);
		this.row = row;
		this.column = column;
		this.greenDirection = greenDirection;
	}
	
	public static int generateId(int row, int column) {
		return row * numberColumns + column;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getGreenDirection() {
		return this.greenDirection;
	}
	
	public int getGreenRoadId() {
		int id = 0;
		switch (greenDirection) {
			case 0:
				id = Road.generateId(true, this.row - 1, this.column);
				break;
			case 1:
				id = Road.generateId(false, this.row, this.column);
				break;
			case 2:
				id = Road.generateId(true, this.row, this.column);
				break;
			case 3:
				id = Road.generateId(false, this.row, this.column - 1);
				break;
		}
		return id;
	}
	
	public void changeGreenDirection(Road road) {
		if (road.isVertical() && (road.getRow() < this.row)) {
			this.greenDirection = 0;
		} else if (road.isVertical() && (road.getRow() == this.row)) {
			this.greenDirection = 2;
		} else if (!road.isVertical() && (road.getColumn() < this.column)) {
			this.greenDirection = 3;
		} else if (!road.isVertical() && (road.getColumn() == this.column)) {
			this.greenDirection = 1;
		}
	}
	
	public List<Integer> getSurroundingRoadIds() {
		List<Integer> surroundingRoadIds = new ArrayList<>();
		
		// up
		surroundingRoadIds.add(Road.generateId(true, this.row - 1, this.column));
		
		// down
		surroundingRoadIds.add(Road.generateId(true, this.row, this.column));
		
		// left
		surroundingRoadIds.add(Road.generateId(false, this.row, this.column - 1));
		
		// right
		surroundingRoadIds.add(Road.generateId(false, this.row, this.column));
		
		return surroundingRoadIds;
		
	}
	
	public List<Integer> getSurroundingIntersectionIds() {
		List<Integer> surroundingIntersectionIds = new ArrayList<>();
		
		// up
		if ((this.row - 1) > 0) {
			surroundingIntersectionIds.add(Intersection.generateId(this.row - 1, this.column));
		} else {
			surroundingIntersectionIds.add(-1);
		}
		
		// down
		if ((this.row + 1) < (numberRows - 1)) {
			surroundingIntersectionIds.add(Intersection.generateId(this.row + 1, this.column));
		} else {
			surroundingIntersectionIds.add(-1);
		}
		
		// left
		if ((this.column - 1) > 0) {
			surroundingIntersectionIds.add(Intersection.generateId(this.row, this.column - 1));
		} else {
			surroundingIntersectionIds.add(-1);
		}
		
		// right
		if ((this.column + 1) < (numberColumns - 1)) {
			surroundingIntersectionIds.add(Intersection.generateId(this.row, this.column + 1));
		} else {
			surroundingIntersectionIds.add(-1);
		}
		
		return surroundingIntersectionIds;
		
	}
	
	
	public static void initializeStaticValues(int rows, int columns) {
		numberRows = rows;
		numberColumns = columns;
	}
	
	public String toString(){
		return "intersection " + this.row + " " + this.column + " " + this.greenDirection + "\n";  
	}
}
