package com.drawgraph.graphics;

import com.drawgraph.model.Line;
import com.drawgraph.model.PositionedGraph;
import com.drawgraph.model.PositionedNode;

import java.awt.*;

/**
 * Date: Oct 24, 2010
 * Time: 9:20:12 AM
 *
 * @author denisk
 */
public class SimpleGraphDrawer implements GraphDrawer<PositionedGraph> {
	@Overridehg 
	public void drawGraph(PositionedGraph g, Graphics2D canvas) {
		for (Line l : g.getLines()) {
			PositionedNode source = g.getNodeById(l.getSource().getId());
			PositionedNode sink = g.getNodeById(l.getSink().getId());

			canvas.drawLine(source.getX(), source.getY(), sink.getX(), sink.getY());
		}

		int radius = g.getRadius();
		for (PositionedNode node : g.getNodes()) {
			if (node.isDummy()) {
				canvas.drawOval(node.getX() - radius / 4, node.getY() - radius / 4, radius/2, radius/2);

			} else {
				canvas.drawOval(node.getX() - radius / 2, node.getY() - radius / 2, radius, radius);

			}
		}
	}
}
