import java.util.*;  
import java.util.logging.*;
import java.util.Random;

/** class that implements the Model of the Traffic System application */
public class TrafficModel {
	
	private class Intersection {  //TODO remove this class
		private String id;
		private int row;
		private int column;
		private int greenDirection;  // inverse clock -> north, west, south and east
		
		private Intersection(int row, int column, int greenDirection) {
			this.id = this.generateId(row, column);
			this.row = row;
			this.column = column;
			this.greenDirection = greenDirection;
		}
		
		private String generateId(int row, int column) {
			return row + "-" + column;
		}
		
		private String getId() {
			return this.id;
		}

	}
	
	private class Vehicle {
		private int row;
		private int column;
		private String type;
		private Queue<Integer> path;  // inverse clock -> north, west, south and east
		
		private Vehicle(int row, int column, String type, Queue<Integer> path) {
			this.row = row;
			this.column = column;
			this.type = type;  // TODO transform to enum
			this.path = path;
		}
		
		private boolean move() {
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
		
		private String getType() {
			return this.type;
		}
		
		public String toString(){
			return "vehicle " + this.row + " " + this.column + " " + this.type + " " + this.path;  
		}  
	}

    private Logger logger = Logger.getLogger("traffic_system.mas2j." + TrafficModel.class.getName());
	private Random random = new Random();
	
	private int rows;
	private int columns;
	private List<Vehicle> vehicles = new ArrayList<>();
	private Map<String, Intersection> intersections = new HashMap<>();
	private List<String> roadFailures = new ArrayList<>();
	private List<String> modelChanges = new ArrayList<>();
	

    public TrafficModel(int rows, int columns) {
		this.rows = rows + 2;
		this.columns = columns + 2;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				Intersection intersection = new Intersection(i + 1, j + 1, 0);
				intersections.put(intersection.getId(), intersection);
			}
		}
    }
	
	public void createRepairVehicle() {
		Queue<Integer> aux = new LinkedList<>();
		aux.add(2);
		aux.add(2);
		Vehicle repairVehicle = new Vehicle(0, 1, "repair", aux);
		this.vehicles.add(repairVehicle);
		System.out.println(this.vehicles);
	}
	
	public void moveVehicles() {
		logger.info("move vehicles");
		List<Vehicle> vehiclesFinished = new ArrayList<>();
		for (Vehicle vehicle: this.vehicles) {
			boolean arrived = vehicle.move();
			if (arrived) {
				vehiclesFinished.add(vehicle);
				if (vehicle.getType().equals("repair")) {
					this.modelChanges.add("-roadFailureBel");
				}
			}
		}
		this.vehicles.removeAll(vehiclesFinished);
		System.out.println(this.vehicles);
	}
	
	public void generateRouteFailure() {
		logger.info("generating route failure");
		List<String> intersection_keys = new ArrayList<String>(this.intersections.keySet());
		if (intersection_keys.size() > 1) {
			String intersection1_key = null;
			String intersection2_key = null;
			boolean equals = true;
			while (!equals) {
				intersection1_key = intersection_keys.get(this.random.nextInt(intersection_keys.size()));
				intersection2_key = intersection_keys.get(this.random.nextInt(intersection_keys.size()));
				equals = intersection1_key.equals(intersection2_key);
			}
			String routeId = this.generateRouteId(intersection1_key, intersection2_key);
			this.roadFailures.add(routeId);
			this.modelChanges.add("+roadFailureBel");
		}
	}
	
	private String generateRouteId(String intersection1, String intersection2) {
		return intersection1 + "_" + intersection2;
	}
	
	public List<String> getModelChanges() {
		return this.modelChanges;
	}
	
	public List<String> resetModelChanges() {
		return this.modelChanges = new ArrayList<>();
	}
	
}
