import java.util.*;  
import java.util.logging.*;
import java.util.Random;

public class Road {
	private int id;
	private int row;
	private int column;
	private boolean isVertical;
	private int numberVehicles;
	private boolean roadFailure;  // false -> good state; true -> bad state
	
	private static int numberRows;
	private static int numberColumns;
	
	public Road(boolean isVertical, int row, int column, int numberVehicles, boolean roadFailure) {
		this.id = this.generateId(isVertical, row, column);
		this.row = row;
		this.column = column;
		this.isVertical = isVertical;
		this.numberVehicles = numberVehicles;
		this.roadFailure = roadFailure;
	}
	
	public static int generateId(boolean isVertical, int row, int column) {
		int aux = row * numberColumns + column;
		int constant = (isVertical) ? 0 : (numberRows * numberColumns);
		return aux + constant;
	}
	
	public int computeIntersectionTopLeftId() {
		if (this.isVertical) {
			return Intersection.generateId(this.row - 1, this.column);
		} else {
			return Intersection.generateId(this.row, this.column - 1);
		}
	}
	
	public int computeIntersectionBottomRightId() {
		if (this.isVertical) {
			return Intersection.generateId(this.row + 1, this.column);
		} else {
			return Intersection.generateId(this.row, this.column + 1);
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public boolean isVertical() {
		return this.isVertical;
	}
	
	public void setRoadFailure(boolean roadFailure){
		this.roadFailure = roadFailure;
	}
	
	public boolean getRoadFailure(){
		return this.roadFailure;
	}
	
	public void addVehicle() {
		this.numberVehicles += 1;
	}
	
	public void deleteVehicle() {
		this.numberVehicles -= 1;
	}
	
	public int getNumberVehicles() {
		return this.numberVehicles;
	}
	
	public boolean isInEdge() {
		return (this.row == 0 || this.column == 0 || this.row == (numberRows - 2) || this.column == (numberColumns - 1)) ? true : false;
	}
	
	public static void initializeStaticValues(int rows, int columns) {
		numberRows = rows;
		numberColumns = columns;
	}
	
	public String toString(){
		return "road " + this.row + " " + this.column + " " + this.numberVehicles + " " + this.roadFailure + "\n";  
	}  
}
