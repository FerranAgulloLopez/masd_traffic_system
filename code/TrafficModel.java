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
	private Map<Integer, Intersection> intersections = new HashMap<>();
	private Map<Integer, Road> roads = new HashMap<>();
	private List<String> modelChanges = new ArrayList<>();
	

    public TrafficModel(int rows, int columns) {
		this.rows = rows + 2;
		this.columns = columns + 2;
		
		// create intersections
		Intersection.initializeStaticValues(this.rows, this.columns);
		for (int row = 1; row < (this.rows - 1); ++row) {
			for (int column = 1; column < (this.columns - 1); ++column) {
				//int greenDirection = this.random.nextInt(4);
				int greenDirection = 2; //TODO do well
				Intersection intersection = new Intersection(row, column, greenDirection);
				this.intersections.put(intersection.getId(), intersection);			
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
		
		// create horizontal roads
		for (int row = 1; row < (this.rows - 1); ++row) {
			for (int column = 0; column < (this.columns - 1); ++column) {
				Road horizontalRoad = new Road(false, row, column, 0, false);
				this.roads.put(horizontalRoad.getId(), horizontalRoad);
			}
		}
    }
	
	public void createRepairVehicle() {
		Queue<Integer> aux = new LinkedList<>(); // TODO ask routeAgent for computing ideal path
		aux.add(5);
		aux.add(2);
		aux.add(2);
		Road road = this.roads.get(Road.generateId(true, 0, 1));
		Vehicle repairVehicle = new Vehicle(road, "repair", aux);  // TODO change static init position to random
		road.addVehicle();
		this.vehicles.add(repairVehicle);
		String roadAgentName = "road_agent_" + road.getId();
		String intersectionAgentName = "intersection_agent_" + road.computeIntersectionTopLeftId();
		this.modelChanges.add(roadAgentName + ">+vehicle_enter_percept(\"" + intersectionAgentName + "\", 1)");
	}
	
	public void moveVehicles() {
		logger.info("move vehicles");
		List<Vehicle> vehiclesFinished = new ArrayList<>();
		for (Vehicle vehicle: this.vehicles) {
			Road road = vehicle.getRoad();
			Intersection intersection = this.intersections.get(road.computeIntersectionBottomRightId()); // TODO implement depending of the vehicle move
			if (vehicle.canMove(intersection)) {
				road.deleteVehicle();
				String roadAgentName = "road_agent_" + road.getId();
				String intersectionAgentName = "intersection_agent_" + intersection.getId();
				this.modelChanges.add(roadAgentName + ">+vehicle_exit_percept(\"" + intersectionAgentName + "\", 1)"); // TODO implement to work with more vehicles
				boolean arrived = vehicle.move(this.roads);
				if (arrived) {
					vehiclesFinished.add(vehicle);
					if (vehicle.getType().equals("repair")) {
						this.modelChanges.add(roadAgentName + ">-road_failure_solved_percept");
						road.setRoadFailure(false);
					}
				} else {
					road = vehicle.getRoad();
					roadAgentName = "road_agent_" + road.getId();
					intersectionAgentName = "intersection_agent_" + road.computeIntersectionTopLeftId();
					this.modelChanges.add(roadAgentName + ">+vehicle_enter_percept(\"" + intersectionAgentName + "\", 1)");
					road.addVehicle();
				}
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
			/*boolean roadNotInEdge = false;
			while (!roadNotInEdge) {
				int road_key = road_keys.get(this.random.nextInt(road_keys.size()));
				chosen_road = this.roads.get(road_key);
				roadNotInEdge = !chosen_road.isInEdge();
			}*/
			chosen_road = this.roads.get(Road.generateId(true, 1, 2));  // static for the demo
			chosen_road.setRoadFailure(true);
			String roadAgentName = "road_agent_" + chosen_road.getId();
			this.modelChanges.add(roadAgentName + ">+road_failure_percept");
		}
	}
	
	public Map<Integer, Road> getRoads() {
		return this.roads;
	}
	
	public Map<Integer, Intersection> getIntersections() {
		return this.intersections;
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
