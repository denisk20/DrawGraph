package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 10:26:29 PM
 *
 * @author denisk
 */
public class OrientedGraphImpl implements OrientedGraph {
	private HashSet<Node> nodes;
	private HashSet<Line> lines;

	public OrientedGraphImpl(HashSet<Node> nodes, HashSet<Line> lines) {
		this.nodes = nodes;
		this.lines = lines;
	}

	public HashSet<Node> getNodes() {
		return nodes;
	}

	public HashSet<Line> getLines() {
		return lines;
	}
}
