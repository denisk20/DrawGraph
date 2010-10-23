package com.drawgraph.model;

import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:38:44 AM
 *
 * @author denisk
 */
public interface Node<T extends Node> {
	String getId();

	/**
	 * This method returns immutable collection. For sources
	 * manipulation please use addSource method
	 */
	Set<T> getSources();
	/**
	 * This method returns immutable collection. For sinks
	 * manipulation please use addSink method
	 */
	Set<T> getSinks();

	void addSource(T source);

	void addSink(T sink);

	Set<T> getNeighbours();
}
