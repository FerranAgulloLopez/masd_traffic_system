import java.util.*;  
import java.lang.Math;
import java.util.logging.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/** class that implements the View of the Traffic System application */
public class TrafficView extends JFrame {
	
    private Logger logger = Logger.getLogger("traffic_system.mas2j." + TrafficView.class.getName());	

    public TrafficView(int rows, int columns, TrafficModel model) {
		add(new MainPanel(rows, columns, model));
        
        setTitle("Traffic System");
        setSize(350, 250);
        setLocationRelativeTo(null);           
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	//public generateRouteFailure(int intersection1_key, intersection2_key);
	
}
