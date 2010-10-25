package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

import java.util.List;

/**
 * Date: Oct 25, 2010
 * Time: 4:11:01 PM
 *
 * @author denisk
 */
public class CoffmanGrahamLayeredGraphOrder implements LayeredGraphOrder<Node> {
	private int layerLength;
	public CoffmanGrahamLayeredGraphOrder(int value) {
		layerLength = value;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public List<List<Node>> getLayers(Graph<Node> g) {
		return null;  //todo implement this!
	}

	@Override
	public void setLayerLength(int layerLength) {
		this.layerLength = layerLength;
	}
}
