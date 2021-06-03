import java.util.*;  
import java.lang.Math;
import java.util.logging.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


/** class that implements the View of the Traffic System application */
public class IntersectionLights {
	
	private static final BasicStroke circleStroke = new BasicStroke(1.0f);
	private int circle_size;
	private Graphics2D g2d;

    public IntersectionLights(Graphics2D g2d, int circle_size) {
		this.g2d = g2d;
		this.circle_size = circle_size;
    }
	
	public void draw(int intersection_y, int intersection_x, Intersection intersection) {
		g2d.setStroke(circleStroke);
		g2d.setPaint(Color.black);
		g2d.drawOval(intersection_x - this.circle_size, intersection_y - this.circle_size, this.circle_size, this.circle_size);
		g2d.drawOval(intersection_x + this.circle_size, intersection_y - this.circle_size, this.circle_size, this.circle_size);
		g2d.drawOval(intersection_x - this.circle_size, intersection_y + this.circle_size, this.circle_size, this.circle_size);
		g2d.drawOval(intersection_x + this.circle_size, intersection_y + this.circle_size, this.circle_size, this.circle_size);
		
		//g2d.setPaint(new GradientPaint(intersection_x - this.circle_size, intersection_y - this.circle_size, Color.red, intersection_x, intersection_y, Color.white));
		if (intersection.getGreenDirection() == 0) {
			g2d.setPaint(Color.green);
		} else {
			g2d.setPaint(Color.red);
		}
		g2d.fillOval(intersection_x - this.circle_size, intersection_y - this.circle_size, this.circle_size, this.circle_size);
		if (intersection.getGreenDirection() == 1) {
			g2d.setPaint(Color.green);
		} else {
			g2d.setPaint(Color.red);
		}
		g2d.fillOval(intersection_x + this.circle_size, intersection_y - this.circle_size, this.circle_size, this.circle_size);
		if (intersection.getGreenDirection() == 2) {
			g2d.setPaint(Color.green);
		} else {
			g2d.setPaint(Color.red);
		}
		g2d.fillOval(intersection_x + this.circle_size, intersection_y + this.circle_size, this.circle_size, this.circle_size);
		if (intersection.getGreenDirection() == 3) {
			g2d.setPaint(Color.green);
		} else {
			g2d.setPaint(Color.red);
		}
		g2d.fillOval(intersection_x - this.circle_size, intersection_y + this.circle_size, this.circle_size, this.circle_size);
	}
	
	public int computeIntersectionX(float width_split_size, int column) {
		return Math.round(column * width_split_size - this.circle_size / 2);
	}
	
	public int computeIntersectionY(float height_split_size, int row) {
		return Math.round(row * height_split_size - this.circle_size / 2);
	}
}
