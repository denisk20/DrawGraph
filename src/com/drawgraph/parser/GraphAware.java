package com.drawgraph.parser;

import com.drawgraph.model.Graph;

/**
 * Date: Oct 22, 2010
 * Time: 1:37:04 PM
 *
 * @author denisk
 */
public interface GraphAware {
	Graph getGraph();

	void setGraph(Graph g);
}
