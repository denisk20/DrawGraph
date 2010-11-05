package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.Node;

/**
 * Date: Oct 22, 2010
 * Time: 5:50:52 PM
 *
 * @author denisk
 */
public interface LayeredGraphOrder extends GraphOrder {
	int getLayerLength();

	<T extends Node<T>> LayeredGraph<T> getLayeredGraph(Graph<T> g);

	void setLayerLength(int layerLength);

}
