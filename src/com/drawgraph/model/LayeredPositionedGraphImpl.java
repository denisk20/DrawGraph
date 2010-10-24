package com.drawgraph.model;

import java.util.List;

/**
 * Date: Oct 24, 2010
 * Time: 8:44:03 AM
 *
 * @author denisk
 */
public class LayeredPositionedGraphImpl extends PositionedGraphImpl implements LayeredPositionedGraph {
	private List<List<PositionedNode>> layers;

	public LayeredPositionedGraphImpl(String id) {
		super(id);
	}

	public LayeredPositionedGraphImpl(String id, List<List<PositionedNode>> layers) {
		this(id);
		this.layers = layers;
	}

	@Override
	public List<List<PositionedNode>> getLayers() {
		return layers;
	}
}