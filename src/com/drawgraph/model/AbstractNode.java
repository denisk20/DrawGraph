package com.drawgraph.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: Oct 23, 2010
 * Time: 7:53:12 PM
 *
 * @author denisk
 */
public abstract class AbstractNode<T extends Node> implements Node<T> {
	private String id;
	private HashSet<T> sources = new HashSet<T>();
	private HashSet<T> sinks = new HashSet<T>();

	public AbstractNode(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Set<T> getSources() {
		return Collections.unmodifiableSet(sources);
	}

	public Set<T> getSinks() {
		return Collections.unmodifiableSet(sinks);
	}

	@Override
	public void addSource(T source) {
		sources.add(source);
	}

	@Override
	public void addSink(T sink) {
		sinks.add(sink);
	}

	public Set<T> getNeighbours() {
		Set<T> result = new HashSet<T>();

		result.addAll(sources);
		result.addAll(sinks);

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Node)) {
			return false;
		}

		final Node that = (Node) o;

		if (id != null ? !id.equals(that.getId()) : that.getId() != null) {
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
