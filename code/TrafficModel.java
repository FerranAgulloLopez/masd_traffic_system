import java.util.*;  
import java.util.logging.*;
import java.util.Random;

/** class that implements the Model of the Traffic System application */
public class TrafficModel {

    private Logger logger = Logger.getLogger("traffic_system.mas2j." + TrafficModel.class.getName());
	private Random random = new Random();
	
	private int rows;
	private int columns;
	private List<Vehicle> vehicles = new ArrayList<>();
	private Map<String, Intersection> intersections = new HashMap<>();
	private Map<Integer, Road> roads = new HashMap<>();
	private List<String> modelChanges = new ArrayList<>();
	

    public TrafficModel(int rows, int columns) {
		this.rows = rows + 2;
		this.columns = columns + 2;
		
		// create intersections
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				Intersection intersection = new Intersection(i + 1, j + 1, 0);
				intersections.put(intersection.getId(), intersection);
			}
		}
		
		// create vertical roads
		Road.initializeStaticValues(this.rows, this.columns);
		for (int column = 1; column < (this.columns - 1); ++column) {
			for (int row = 0; row < (this.rows - 1); ++row) {
				Road verticalRoad = new Road(true, row, column, 0, false);
				this.roads.put(verticalRoad.getId(), verticalRoad);
			}
		}		
    }
	
	public void createRepairVehicle() {
		Queue<Integer> aux = new LinkedList<>();
		aux.add(2);
		aux.add(2);
		Vehicle repairVehicle = new Vehicle(0, 1, "repair", aux);  // TODO change static init position to random
		Road road = this.roads.get(Road.generateId(true, 0, 1));
		road.addVehicle();
		this.vehicles.add(repairVehicle);
		System.out.println(this.vehicles);
	}
	
	public void moveVehicles() {
		logger.info("move vehicles");
		List<Vehicle> vehiclesFinished = new ArrayList<>();
		for (Vehicle vehicle: this.vehicles) {
			Road road = this.roads.get(Road.generateId(true, vehicle.getRow(), vehicle.getColumn()));
			road.deleteVehicle();
			boolean arrived = vehicle.move();
			if (arrived) {
				vehiclesFinished.add(vehicle);
				if (vehicle.getType().equals("repair")) {
					this.modelChanges.add("-roadFailureBel");
					road.setRoadFailure(false);
				}
			} else {
				road = this.roads.get(Road.generateId(true, vehicle.getRow(), vehicle.getColumn()));
				road.addVehicle();
			}
		}
		this.vehicles.removeAll(vehiclesFinished);
		System.out.println(this.vehicles);
	}
	
	public void generateRouteFailure() {
		logger.info("generating route failure");
		List<Integer> road_keys = new ArrayList<>(this.roads.keySet());
		if (this.roads.size() > 1) {
			// sample random road that is not in the edge
			Road chosen_road = null;
			boolean roadNotInEdge = false;
			while (!roadNotInEdge) {
				int road_key = road_keys.get(this.random.nextInt(road_keys.size()));
				chosen_road = this.roads.get(road_key);
				roadNotInEdge = !chosen_road.isInEdge();
			}
			chosen_road.setRoadFailure(true);
			this.modelChanges.add("+roadFailureBel");
		}
	}
	
	public Map<Integer, Road> getRoads() {
		return this.roads;
	}
	
	public List<String> getModelChanges() {
		return this.modelChanges;
	}
	
	public int getRows(){
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public List<String> resetModelChanges() {
		return this.modelChanges = new ArrayList<>();
	}
	
}
