package com.drawgraph.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:32:10 AM
 *
 * @author denisk
 */
public class SimpleOrientedNode implements OrientedNode {
	private HashSet<OrientedNode> sources = new HashSet<OrientedNode>();
	private HashSet<OrientedNode> sinks = new HashSet<OrientedNode>();

	public SimpleOrientedNode(HashSet<OrientedNode> sources, HashSet<OrientedNode> sinks) {
		this.sources = sources;
		this.sinks = sinks;
	}

	public Set<OrientedNode> getSources() {
		return sources;
	}

	public Set<OrientedNode> getSinks() {
		return sinks;
	}


	public Set<Node> getNeighbours() {
		Set<Node> result = new HashSet<Node>();

		result.addAll(sources);
		result.addAll(sinks);

		return result;
	}
}
