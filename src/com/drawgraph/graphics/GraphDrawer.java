package com.drawgraph.graphics;

import com.drawgraph.model.PositionedGraph;

import java.awt.*;

/**
 * Date: Oct 23, 2010
 * Time: 10:06:14 PM
 *
 * @author denisk
 */
public interface GraphDrawer <T extends PositionedGraph>{
	void drawGraph(T g, Graphics2D canvas);
}
