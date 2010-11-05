package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

/**
 * Date: Oct 22, 2010
 * Time: 1:37:04 PM
 *
 * @author denisk
 */
public interface GraphAware<T extends Node<T>> {
	Graph<T> getGraph();

	void setGraph(Graph<T> g);
}
