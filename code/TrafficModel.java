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
	
	private TrafficView view;
	

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
		System.out.println("create!!!!!!!!!!!!!!! roads");
		Road.initializeStaticValues(rows, columns);
		for (int column = 1; column < (this.columns - 1); ++column) {
			for (int row = 0; row < (this.rows - 1); ++row) {
				Road verticalRoad = new Road(true, row, column, 0, false);
				this.roads.put(verticalRoad.getId(), verticalRoad);
			}
		}
		
		
		// run view
		this.view = new TrafficView(this.rows, this.columns, this);
		this.view.setVisible(true);
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
		//List<String> intersection_keys = new ArrayList<String>(this.intersections.keySet());
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
			
			
			/*String intersection1_key = null;
			String intersection2_key = null;
			boolean equals = true;
			while (!equals) {
				intersection1_key = intersection_keys.get(this.random.nextInt(intersection_keys.size()));
				intersection2_key = intersection_keys.get(this.random.nextInt(intersection_keys.size()));
				equals = intersection1_key.equals(intersection2_key);
			}
			String routeId = this.generateRouteId(intersection1_key, intersection2_key);*/
			//this.roadFailures.add(routeId);
			this.modelChanges.add("+roadFailureBel");
			//this.view.generateRouteFailure(intersection1_key, intersection2_key);
		}
	}
	
	/*private String generateRouteId(String intersection1, String intersection2) {
		return intersection1 + "_" + intersection2;
	}*/
	
	public Map<Integer, Road> getRoads() {
		return this.roads;
	}
	
	public List<String> getModelChanges() {
		return this.modelChanges;
	}
	
	public List<String> resetModelChanges() {
		return this.modelChanges = new ArrayList<>();
	}
	
}
