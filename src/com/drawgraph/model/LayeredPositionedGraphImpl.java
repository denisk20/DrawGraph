package com.drawgraph.model;

import java.util.ArrayList;
import java.util.HashSet;
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

	@Override
	public LayeredPositionedGraphImpl copy() {
		PositionedGraphImpl positionedGraphCopy = super.copy();

		List<List<PositionedNode>> layersCopy = createLayersCopy(positionedGraphCopy, layers);

		LayeredPositionedGraphImpl copy = new LayeredPositionedGraphImpl(getId(), layersCopy);

		copy.getNodes().addAll(positionedGraphCopy.getNodes());
		copy.getLines().addAll(positionedGraphCopy.getLines());

		copy.setRadius(getRadius());

		return copy;
	}

	private List<List<PositionedNode>> createLayersCopy(PositionedGraphImpl positionedGraphCopy, List<List<PositionedNode>> originalLayers) {
		ArrayList<List<PositionedNode>> result = new ArrayList<List<PositionedNode>>(originalLayers.size());
		for (int i = 0; i < originalLayers.size(); i++) {
			List<PositionedNode> originalLayer = originalLayers.get(i);
			List<PositionedNode> copyLayer = new ArrayList<PositionedNode>(originalLayer.size());
			for (int j = 0; j < originalLayer.size(); j++) {
				PositionedNode originalNode = originalLayer.get(j);
				PositionedNode copyNode = positionedGraphCopy.getNodeById(originalNode.getId());
				copyLayer.add(copyNode);
			}
			result.add(copyLayer);
		}

		return result;
	}
}
