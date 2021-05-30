import java.util.*;  
import java.lang.Math;
import java.util.logging.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


/** class that implements the View of the Traffic System application */
// TODO could be fully static
public class RoadInfoRectangle {
	
	private static final String rectangle_text1 = "Number vehicles -> 0";
	private static final String rectangle_text2_1 = "Road state -> correct";
	private static final String rectangle_text2_2 = "Road state -> failure";
	private static final float dash[] = {5.0f};
	private static final BasicStroke rectangleStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
	private int char_height;
	private float rectangle_space_from_line;
	private float rectangle_height;
	private float rectangle_width;
	private int text_lateral_space;
	private Graphics2D g2d;

    public RoadInfoRectangle(Graphics2D g2d, int total_height, int total_width) {
		this.g2d = g2d;
		
		FontMetrics fontMetrics = g2d.getFontMetrics();
		this.char_height = fontMetrics.getHeight();
		int text_width1 = fontMetrics.stringWidth(rectangle_text1);
		int text_width2 = fontMetrics.stringWidth(rectangle_text2_1);
		int text_descent = fontMetrics.getDescent();
		this.text_lateral_space = fontMetrics.stringWidth(" ");
		
		//this.rectangle_space_from_line = 0.005f * total_width;
		this.rectangle_height = char_height * 2 + text_descent * 2;
		this.rectangle_width = Math.max(text_width1, text_width2) + this.text_lateral_space * 2;
    }
	
	public void draw(int y_corner, int x_corner, Road road) {
		g2d.setStroke(this.rectangleStroke);
		g2d.setPaint(Color.gray);
		RoundRectangle2D rectangle = new RoundRectangle2D.Double(x_corner, y_corner, Math.round(this.rectangle_width), Math.round(this.rectangle_height), 3, 3);
		g2d.fill(rectangle);
		g2d.setPaint(Color.black);
		g2d.draw(rectangle);
		String text = this.rectangle_text1.substring(0, this.rectangle_text1.length() - 1) + road.getNumberVehicles();
		if (road.getNumberVehicles() > 0) {
			g2d.setColor(Color.blue);
			g2d.drawString(text, x_corner + this.text_lateral_space, y_corner + this.char_height);
			g2d.setColor(Color.black);
		} else {
			g2d.drawString(text, x_corner + this.text_lateral_space, y_corner + this.char_height);
		}
		if (road.getRoadFailure()) {
			g2d.setColor(Color.red);
			g2d.drawString(this.rectangle_text2_2, x_corner + this.text_lateral_space, y_corner + this.char_height * 2);
			g2d.setColor(Color.black);
		} else {
			g2d.drawString(this.rectangle_text2_1, x_corner + this.text_lateral_space, y_corner + this.char_height * 2);
		}
	}
	
	public float getRectangleHeight() {
		return this.rectangle_height;
	}
	
	public float getRectangleWidth() {
		return this.rectangle_width;
	}
	
	public int computeYRectangleCorner(float height_split_size, int row) {
		return Math.round(height_split_size * row + height_split_size / 2 - this.rectangle_height / 2);
	}
	
	public int computeXRectangleCorner(float width_split_size, int column) {
		return Math.round(width_split_size * column - this.rectangle_width / 2);
	}
}
