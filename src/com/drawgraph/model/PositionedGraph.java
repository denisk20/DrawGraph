package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 22, 2010
 * Time: 6:00:48 PM
 *
 * @author denisk
 */
public interface PositionedGraph extends Graph {
	HashSet<PositionedNode> getPositionedNodes();

	HashSet<PositionedLine> getPositionedLines();
}
