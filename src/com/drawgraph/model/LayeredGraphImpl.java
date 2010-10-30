package com.drawgraph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Oct 30, 2010
 * Time: 3:49:08 PM
 *
 * @author denisk
 */
public class LayeredGraphImpl extends AbstractGraph<Node> implements LayeredGraph<Node>{
	private List<List<Node>> layers = new ArrayList<List<Node>>();

	public LayeredGraphImpl(String id, List<List<Node>> layers) {
		super(id);
		this.layers.addAll(layers);
	}

	@Override
	public List<List<Node>> getLayers() {
		return layers;
	}

	@Override
	public Graph<Node> copy() {
		Graph<Node> copy = new GraphImpl(getId());
		for (Node node : getNodes()) {
			SimpleNode copyNode = new SimpleNode(node.getId());
			copy.getNodes().add(copyNode);
		}

		addSourcesSinksLines(copy);
		return copy;
	}
}
