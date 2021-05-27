// Environment code for project traffic_system.mas2j

import jason.asSyntax.*;
import jason.environment.*;
import jason.asSyntax.parser.*;

import java.util.*;  
import java.util.logging.*;

public class MainEnvironment extends TimeSteppedEnvironment {

    private Logger logger = Logger.getLogger("traffic_system.mas2j." + MainEnvironment.class.getName());

	private TrafficModel model;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
		super.init(new String[] { "3000" } ); // set step timeout
        setOverActionsPolicy(OverActionsPolicy.queue);
		
		this.model = new TrafficModel(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		this.model.generateRouteFailure();
		this.updatePercepts();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
		logger.info("executing: " + action);
		switch (action.getFunctor()) {
			case "sendRepairCarEnvAction":
				this.model.createRepairVehicle();
				break;
			default:
				logger.info("but not implemented!");
				return false;
		}
        return true;
    }
	
	private void updatePercepts() {
		List<String> beliefChanges = this.model.getModelChanges();
		try {  // TODO do better
			for (String beliefChange: beliefChanges) {
				char firstChar = beliefChange.charAt(0);;
				if (firstChar == '+') {
					addPercept(ASSyntax.parseLiteral(beliefChange.substring(1)));
				}
				else if (firstChar == '-') {
					removePercept(ASSyntax.parseLiteral(beliefChange.substring(1)));
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
			this.model.moveVehicles();
			this.updatePercepts();	
		}
    }
}

