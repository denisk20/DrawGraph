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
		if (sinks != null ? !sinks.equals(that.sinks) : that.sinks != null) {
			return false;
		}
		if (sources != null ? !sources.equals(that.sources) : that.sources != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (sources != null ? sources.hashCode() : 0);
		result = 31 * result + (sinks != null ? sinks.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SimpleNode{" + "id='" + id + '\'' + '}';
	}
}
