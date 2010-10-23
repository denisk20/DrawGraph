package com.drawgraph.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:32:10 AM
 *
 * @author denisk
 */
public class SimpleNode implements Node {
	private String id;
	private HashSet<Node> sources = new HashSet<Node>();
	private HashSet<Node> sinks = new HashSet<Node>();

	public SimpleNode(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Set<Node> getSources() {
		return Collections.unmodifiableSet(sources);
	}

	public Set<Node> getSinks() {
		return Collections.unmodifiableSet(sinks);
	}

	@Override
	public void addSource(Node source) {
		sources.add(source);
	}

	@Override
	public void addSink(Node sink) {
		sinks.add(sink);
	}

	public Set<Node> getNeighbours() {
		Set<Node> result = new HashSet<Node>();

		result.addAll(sources);
		result.addAll(sinks);

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SimpleNode)) {
			return false;
		}

		final SimpleNode that = (SimpleNode) o;

		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "SimpleNode{" + "id='" + id + '\'' + '}';
	}
}
