import java.util.*;  


public class Intersection {
	private String id;
	private int row;
	private int column;
	private int greenDirection;  // inverse clock -> north, west, south and east
	
	public Intersection(int row, int column, int greenDirection) {
		this.id = this.generateId(row, column);
		this.row = row;
		this.column = column;
		this.greenDirection = greenDirection;
	}
	
	private static String generateId(int row, int column) {
		return row + "-" + column;
	}
	
	public String getId() {
		return this.id;
	}
}
