package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 10:26:29 PM
 *
 * @author denisk
 */
public class GraphImpl implements Graph<Node> {
	private String id;
	private HashSet<Node> nodes = new HashSet<Node>();
	private HashSet<Line<Node>> lines = new HashSet<Line<Node>>();

	public GraphImpl(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public HashSet<Node> getNodes() {
		return nodes;
	}

	public HashSet<Line<Node>> getLines() {
		return lines;
	}
}
