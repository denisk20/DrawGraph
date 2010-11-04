package com.drawgraph.experimental;

import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.model.LayeredPositionedGraph;

/**
 * Date: Nov 1, 2010
 * Time: 11:23:17 AM
 *
 * @author denisk
 */
public class Polygon {
	public static void main(String[] args) {
		PositionedNode pn = null;
		PositionedNode other = null;

		pn.getSources().add(pn);

		LayeredGraphOrder lgo = null;
		LayeredPositionedGraph layeredPositionedGraph = null;
		lgo.getLayeredGraph(layeredPositionedGraph);
	}

}
