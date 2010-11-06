package com.drawgraph.graphics.prefuse;

import prefuse.data.Graph;

import javax.swing.*;

/**
 * Date: Nov 6, 2010
 * Time: 8:25:04 PM
 *
 * @author denisk
 */
public interface PreFuseCanvas {
	JComponent getCanvas();

	void setGraph(Graph g);
}
