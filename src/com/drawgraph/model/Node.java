package com.drawgraph.model;

import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:38:44 AM
 *
 * @author denisk
 */
public interface Node {
	String getId();

	/**
	 * This method returns immutable collection. For sources
	 * manipulation please use addSource method
	 */
	Set<Node> getSources();
	/**
	 * This method returns immutable collection. For sinks
	 * manipulation please use addSink method
	 */
	Set<Node> getSinks();

	void addSource(Node source);

	void addSink(Node sink);

	Set<Node> getNeighbours();
}
