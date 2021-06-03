// Environment code for project traffic_system.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import jason.asSyntax.parser.*;

import java.util.*;  
import java.util.logging.*;

public class MainEnvironment extends TimeSteppedEnvironment {

    private Logger logger = Logger.getLogger("traffic_system.mas2j." + MainEnvironment.class.getName());

	private TrafficModel model;
	private TrafficView view;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
		super.init(new String[] { "3000" } ); // set step timeout
        setOverActionsPolicy(OverActionsPolicy.queue);
		
		this.model = new TrafficModel(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		this.view = new TrafficView(this.model.getRows(), this.model.getColumns(), this.model);
		this.view.setVisible(true);
		
		// create road agents
		Map<Integer, Road> roadsMap = this.model.getRoads();
		for (Map.Entry<Integer, Road> entry : roadsMap.entrySet()) {
			String roadAgentName = "road_agent_" + entry.getKey();
			try {
				getEnvironmentInfraTier().getRuntimeServices().createAgent(roadAgentName, "./agents/road.asl", null, null, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// intialize road agents
		for (Map.Entry<Integer, Road> entry : roadsMap.entrySet()) {
			String roadAgentName = "road_agent_" + entry.getKey();
			try {
				String intersectionAgentTopLeftName = "intersection_agent_" + entry.getValue().computeIntersectionTopLeftId();
				String intersectionAgentBottomRightName = "intersection_agent_" + entry.getValue().computeIntersectionBottomRightId();
				addPercept(roadAgentName, ASSyntax.parseLiteral("initialization(\"" + intersectionAgentTopLeftName + "\",\"" + intersectionAgentBottomRightName + "\")"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// create intersection agents
		Map<Integer, Intersection> intersectionsMap = this.model.getIntersections();
		for (Map.Entry<Integer, Intersection> entry : intersectionsMap.entrySet()) {
			String intersectionAgentName = "intersection_agent_" + entry.getKey();
			try {
				getEnvironmentInfraTier().getRuntimeServices().createAgent(intersectionAgentName, "./agents/intersection.asl", null, null, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// initialize intersection agents
		for (Map.Entry<Integer, Intersection> entry : intersectionsMap.entrySet()) {
			String intersectionAgentName = "intersection_agent_" + entry.getKey();
			try {
				String greenRoadAgent = "road_agent_" + entry.getValue().getGreenRoadId();
				List<Integer> surroundingRoadIds = entry.getValue().getSurroundingRoadIds();
				List<Integer> surroundingIntersectionIds = entry.getValue().getSurroundingIntersectionIds();
				String surroundingRoadIntersectionAgents = "[";
				for (int i = 0; i < surroundingRoadIds.size(); ++i) {
					int roadId = surroundingRoadIds.get(i);
					int intersectionId = surroundingIntersectionIds.get(i);
					if (intersectionId != -1) {
						surroundingRoadIntersectionAgents += "[\"road_agent_" + roadId + "\", \"intersection_agent_" + intersectionId + "\"],";
					} else {
						surroundingRoadIntersectionAgents += "[\"road_agent_" + roadId + "\", \"null\"],";
					}
				}
				surroundingRoadIntersectionAgents = surroundingRoadIntersectionAgents.substring(0, surroundingRoadIntersectionAgents.length() - 1) + "]"; // substract last comma and add last ]
				addPercept(intersectionAgentName, ASSyntax.parseLiteral("initialization(\"" + greenRoadAgent + "\"," + surroundingRoadIntersectionAgents + ")"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
		logger.info("executing: " + action);
		boolean output = true;
		switch (action.getFunctor()) {
			case "sendRepairCarEnvAction":
				this.model.createRepairVehicle();
				break;
			case "update_green_direction":
				String greenDirection = action.getTerms().get(0).toString();
				Road road = this.model.getRoads().get(Integer.parseInt(greenDirection.substring(greenDirection.lastIndexOf("_") + 1, greenDirection.length() - 1)));
				int intersectionId = Integer.parseInt(agName.substring(agName.lastIndexOf("_") + 1));
				Intersection intersection = this.model.getIntersections().get(intersectionId);
				intersection.changeGreenDirection(road);
				break;
			default:
				logger.info("but not implemented!");
				output = false;
		}
        return true;
    }
	
	private void updatePercepts() {
		clearPercepts("road_agent_6");
		List<String> beliefChanges = this.model.getModelChanges();
		try {
			for (String beliefChange: beliefChanges) {
				String[] parts = beliefChange.split(">");
				String agentName = parts[0];
				String agentPercept = parts[1];
				char firstChar = agentPercept.charAt(0);
				if (firstChar == '+') {
					addPercept(agentName, ASSyntax.parseLiteral(agentPercept.substring(1)));
				}
				else if (firstChar == '-') {
					removePercept(agentName, ASSyntax.parseLiteral(agentPercept.substring(1)));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.model.resetModelChanges();
	}
	
	@Override
    protected void stepStarted(int step) {
        logger.info("start step " + step);
		if (this.model != null) {
			this.view.repaint();
			this.model.moveVehicles();
			
			if (step == 2) {
				this.model.generateRouteFailure();
			}
			try {
				addPercept(ASSyntax.parseLiteral("timer"));
			} catch (ParseException e) {
			e.printStackTrace();
			}
			this.updatePercepts();
		}
    }
}

