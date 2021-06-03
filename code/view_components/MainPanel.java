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
	private RoadInfoRectangle roadInfoRectangle;
	private IntersectionLights intersectionLights;
	
	public MainPanel(int rows, int columns, TrafficModel model) {			
		this.rows = rows;
		this.columns = columns;
		this.model = model;
	}
	
	private void doDrawing(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
							 RenderingHints.VALUE_RENDER_QUALITY);
		
		Dimension dimension = getSize();
		int total_height = dimension.height;
		int total_width = dimension.width;
		
		float height_split_size = total_height / (this.rows - 1);
		float width_split_size = total_width / (this.columns - 1);
		float edge_free_space = 10;
		BasicStroke lineStroke = new BasicStroke(3.0f);
		this.roadInfoRectangle = new RoadInfoRectangle(g2d, total_height, total_width);
		this.intersectionLights = new IntersectionLights(g2d, Math.round(edge_free_space));
		
		// draw horizontal lines
		for (int row = 1; row < (this.rows - 1); ++row) {
			float height = row * height_split_size;
			g2d.setStroke(lineStroke);
			g2d.setPaint(Color.black);
			g2d.draw(new Line2D.Double(edge_free_space, height, total_width - edge_free_space, height));
			
			// draw info rectangles
			int y_rectangle_corner = this.roadInfoRectangle.computeYRectangleCorner(false, height_split_size, row);
			for (int column = 0; column < (this.columns - 1); ++column) {
				int x_rectangle_corner = this.roadInfoRectangle.computeXRectangleCorner(false, width_split_size, column);
				this.drawInfoRectangle(y_rectangle_corner, x_rectangle_corner, false, row, column);		
			}
		}

		// draw vertical lines
		for (int column = 1; column < (this.columns - 1); ++column) {
			float width = column * width_split_size;
			g2d.setStroke(lineStroke);
			g2d.setPaint(Color.black);
			g2d.draw(new Line2D.Double(width, edge_free_space, width, total_height - edge_free_space));
			
			// draw info rectangles
			int x_rectangle_corner = this.roadInfoRectangle.computeXRectangleCorner(true, width_split_size, column);
			for (int row = 0; row < (this.rows - 1); ++row) {
				int y_rectangle_corner = this.roadInfoRectangle.computeYRectangleCorner(true, height_split_size, row);
				this.drawInfoRectangle(y_rectangle_corner, x_rectangle_corner, true, row, column);		
			}
		}
		
		// draw intersection lights
		for (int row = 1; row < (this.rows - 1); ++row) {
			int intersectionY = this.intersectionLights.computeIntersectionY(height_split_size, row);
			for (int column = 1; column < (this.columns - 1); ++column) {
				int intersectionX = this.intersectionLights.computeIntersectionX(width_split_size, column);
				Intersection intersection = this.model.getIntersections().get(Intersection.generateId(row, column));
				this.intersectionLights.draw(intersectionY, intersectionX, intersection);
			}
		}
	}
	
	private void drawInfoRectangle(int y_rectangle_corner, int x_rectangle_corner, boolean isVertical, int row, int column)  {
		Road road = this.model.getRoads().get(Road.generateId(isVertical, row, column));
		this.roadInfoRectangle.draw(y_rectangle_corner, x_rectangle_corner, road);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		doDrawing(g);
	}

}
