package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 10:26:29 PM
 *
 * @author denisk
 */
public class GraphImpl implements Graph {
	private String id;
	private HashSet<Node> nodes = new HashSet<Node>();
	private HashSet<Line> lines = new HashSet<Line>();

	public GraphImpl(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public HashSet<Node> getNodes() {
		return nodes;
	}

	public HashSet<Line> getLines() {
		return lines;
	}
}
