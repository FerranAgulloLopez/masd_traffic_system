import java.util.*;  
import java.lang.Math;
import java.util.logging.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


public class MainPanel extends JPanel {
	
	private int rows;
	private int columns;
	private TrafficModel model;
	
	public MainPanel(int rows, int columns, TrafficModel model) {			
		this.rows = rows;
		this.columns = columns;
		this.model = model;
	}
	
	private void doDrawing(Graphics g) {
		//TODO check why it appears two times at the start of the execution
		
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
							 RenderingHints.VALUE_RENDER_QUALITY);

		//this.setBackground(Color.white);
		
		Dimension dimension = getSize();
		int total_height = dimension.height;
		int total_width = dimension.width;
		
		float height_split_size = total_height / (this.rows - 1);
		float width_split_size = total_width / (this.columns - 1);
		float edge_free_space = 10;
		BasicStroke lineStroke = new BasicStroke(3.0f);
		RoadInfoRectangle roadInfoRectangles = new RoadInfoRectangle(g2d, total_height, total_width);
		
		// draw horizontal lines
		for (int row = 0; row < (this.rows - 1); ++row) {
			float height = (row + 1) * height_split_size;
			g2d.setStroke(lineStroke);
			g2d.setPaint(Color.black);
			g2d.draw(new Line2D.Double(edge_free_space, height, total_width - edge_free_space, height));
		}

		// draw vertical lines
		for (int column = 1; column < (this.columns - 1); ++column) {
			float width = column * width_split_size;
			g2d.setStroke(lineStroke);
			g2d.setPaint(Color.black);
			g2d.draw(new Line2D.Double(width, edge_free_space, width, total_height- edge_free_space));
			
			// draw info rectangles
			int x_rectangle_corner = Math.round(width_split_size * column - roadInfoRectangles.getRectangleWidth() / 2);
			for (int row = 0; row < (this.rows - 1); ++row) {
				int y_rectangle_corner = Math.round(height_split_size * row + height_split_size / 2 - roadInfoRectangles.getRectangleHeight() / 2);
				Road road = this.model.getRoads().get(Road.generateId(true, row, column));
				roadInfoRectangles.draw(y_rectangle_corner, x_rectangle_corner, road);				
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		doDrawing(g);
	}

}
