package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.Node;

import java.util.List;

/**
 * Date: Oct 22, 2010
 * Time: 5:50:52 PM
 *
 * @author denisk
 */
public interface LayeredGraphOrder<T extends Node> extends GraphOrder {
	int getLayerLength();

	LayeredGraph<T> getLayeredGraph(Graph<? extends T> g);

	void setLayerLength(int layerLength);

}
