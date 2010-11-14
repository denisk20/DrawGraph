package com.drawgraph.graphics;

import com.drawgraph.model.Line;
import com.drawgraph.model.PositionedGraph;
import com.drawgraph.model.PositionedNode;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Date: Oct 24, 2010
 * Time: 9:20:12 AM
 *
 * @author denisk
 */
public class SimpleGraphDrawer implements GraphDrawer<PositionedGraph> {
	@Override
	public void drawGraph(PositionedGraph g, Graphics2D canvas) {
		int radius = g.getRadius();
		for (Line l : g.getLines()) {
			PositionedNode source = g.getNodeById(l.getSource().getId());
			PositionedNode sink = g.getNodeById(l.getSink().getId());

			if (source.isDummy() || sink.isDummy()) {
				canvas.setPaint(Color.RED);
			}
			canvas.drawLine(source.getX(), source.getY(), sink.getX(), sink.getY());
			int dia;
			if (sink.isDummy()) {
				dia = radius / 2;
			} else {
				dia = radius;
			}
			drawArrow(canvas, new Point(source.getX(), source.getY()), new Point(sink.getX(), sink.getY()), dia, 15, Math.toRadians(20));
			canvas.setPaint(Color.BLACK);

			
		}

		Paint paint = canvas.getPaint();
		for (PositionedNode node : g.getNodes()) {
			Ellipse2D shape;
			if (node.isDummy()) {

				shape = new Ellipse2D.Double(node.getX() - radius / 4, node.getY() - radius / 4, radius/2, radius/2);

				canvas.setPaint(Color.YELLOW);
				canvas.fill(shape);
				canvas.setPaint(Color.BLACK);
//				canvas.drawString(node.getId(), node.getX(), node.getY() +6);

			} else {
				shape = new Ellipse2D.Double(node.getX() - radius / 2, node.getY() - radius / 2, radius, radius);
				canvas.setPaint(Color.ORANGE);
				canvas.fill(shape);
				canvas.setPaint(Color.BLACK);
				Font f = new Font("Serif", Font.PLAIN, 14);
				canvas.setFont(f);
				int x;
				if (node.getId().length() > 2) {
					x = node.getX() - 12;
				} else {
					x = node.getX() - 7;

				}
				canvas.drawString(node.getId(), x, node.getY() +6);
			}
			canvas.setPaint(Color.black);
			canvas.draw(shape);
		}
		canvas.setPaint(paint);
	}

	public void drawArrow(Graphics2D g2, Point p1, Point p2, double dia, double barb, double phi) {
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.red);
		double dy = p2.y - p1.y;
		double dx = p2.x - p1.x;
		double theta = Math.atan2(dy, dx);
        double x1 = p1.x + (dia/2)*Math.cos(theta);
        double y1 = p1.y + (dia/2)*Math.sin(theta);
        theta += Math.PI;
        double x2 = p2.x + (dia/2)*Math.cos(theta);
        double y2 = p2.y + (dia/2)*Math.sin(theta);
        g2.setPaint(Color.green.darker());
        double x = x2 + barb*Math.cos(theta+phi);
        double y = y2 + barb*Math.sin(theta+phi);
        g2.draw(new Line2D.Double(x2, y2, x, y));
        x = x2 + barb*Math.cos(theta-phi);
        y = y2 + barb*Math.sin(theta-phi);
        g2.draw(new Line2D.Double(x2, y2, x, y));

	}
}
