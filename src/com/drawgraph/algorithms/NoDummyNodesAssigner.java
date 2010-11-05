package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.Node;

/**
 * Date: Oct 29, 2010
 * Time: 10:24:44 PM
 *
 * @author denisk
 */
public class NoDummyNodesAssigner implements DummyNodesAssigner {
	@Override
	public <T extends Node<T>> LayeredGraph<T> assignDummyNodes(LayeredGraph<T> source) {
		//do nothing
		return source;
	}
}
