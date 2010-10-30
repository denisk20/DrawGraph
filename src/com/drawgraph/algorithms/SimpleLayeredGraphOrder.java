package com.drawgraph.algorithms;

import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredGraphImpl;
import com.drawgraph.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Date: Oct 23, 2010
 * Time: 12:44:35 PM
 *
 * @author denisk
 */
public class SimpleLayeredGraphOrder implements LayeredGraphOrder<Node> {
	private int layerLength;
	private int layersCount;

	public SimpleLayeredGraphOrder(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public void setLayerLength(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public LayeredGraph<Node> getLayeredGraph(Graph<? extends Node> g) {
		ArrayList<List<Node>> layers = new ArrayList<List<Node>>();

		if (layerLength != 0) {

			boolean moreNodes = true;
			Stack<Node> nodeStack = new Stack<Node>();
			nodeStack.addAll(g.getNodes());

			while (moreNodes) {
				ArrayList<Node> layer = new ArrayList<Node>();
				for (int j = 0; j < layerLength; j++) {
					if (!nodeStack.isEmpty()) {
						Node n = nodeStack.pop();
						layer.add(n);
					} else {
						break;
					}
				}
				layers.add(layer);
				if (nodeStack.isEmpty()) {
					moreNodes = false;
				}
			}

		}
		layersCount = layers.size();

		LayeredGraphImpl result = new LayeredGraphImpl(g.getId(), layers);
		result.getLines().addAll(g.getLines());
		result.getNodes().addAll(g.getNodes());

		return result;
	}

	public int getLayersCount() {
		return layersCount;
	}
}
