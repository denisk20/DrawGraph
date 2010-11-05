package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.Node;

/**
 * Date: Oct 29, 2010
 * Time: 10:21:26 PM
 *
 * @author denisk
 */
public interface DummyNodesAssigner {
	<T extends Node<T>> LayeredGraph<T> assignDummyNodes(LayeredGraph<T> source);
}
