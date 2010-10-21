package com.drawgraph.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:32:10 AM
 *
 * @author denisk
 */
public class SimpleNode implements Node {
	private HashSet<Node> sources = new HashSet<Node>();
	private HashSet<Node> sinks = new HashSet<Node>();

	public Set<Node> getSources() {
		return sources;
	}

	public Set<Node> getSinks() {
		return sinks;
	}


	public Set<Node> getNeighbours() {
		Set<Node> result = new HashSet<Node>();

		result.addAll(sources);
		result.addAll(sinks);

		return result;
	}
}
