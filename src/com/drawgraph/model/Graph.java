package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 10:22:06 PM
 *
 * @author denisk
 */
public interface Graph {
	String getId();
	HashSet<Node> getNodes();

	HashSet<Line> getLines();
}
