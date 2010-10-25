package com.drawgraph.graphics;

import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.LayeredPositionedGraphImpl;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Date: Oct 23, 2010
 * Time: 12:46:53 PM
 *
 * @author denisk
 */
public class GraphScalerImpl implements GraphScaler {
	private int minDistance;
	private int layerOffset;

	private int topOffset;
	private int leftOffset;

	@Override
	public void setMinDistance(int dist) {
		minDistance = dist;
	}

	@Override
	public void setLayerOffset(int layerOffset) {
		this.layerOffset = layerOffset;
	}

	@Override
	public void setTopOffset(int off) {
		topOffset = off;
	}

	@Override
	public void setLeftOffset(int off) {
		leftOffset = off;
	}

	@Override
	public LayeredPositionedGraph scale(Graph<Node> g, LayeredGraphOrder<Node> order) {
		final List<List<Node>> layers = order.getLayers(g);

		HashSet<PositionedNode> positionedNodes = new HashSet<PositionedNode>();

		List<List<PositionedNode>> positionedLayers = new ArrayList<List<PositionedNode>>();
		int curX = leftOffset;
		int curY = topOffset;
		for (List<Node> layer : layers) {
			List<PositionedNode> positionedLayer = new ArrayList<PositionedNode>();
			for (Node n : layer) {
				int x = curX;
				int y = curY;
				PositionedNode positionedNode = new PositionedNodeImpl(n.getId(), x, y);
				positionedNodes.add(positionedNode);

				positionedLayer.add(positionedNode);

				curX += minDistance;
			}
			positionedLayers.add(positionedLayer);
			curX = leftOffset;
			curY += layerOffset;
		}
		LayeredPositionedGraph result = new LayeredPositionedGraphImpl(g.getId(), positionedLayers);

		result.getNodes().addAll(positionedNodes);
		result.getLines().addAll(g.getLines());

		return result;
	}

	@Override
	public int getMinDistance() {
		return minDistance;
	}

	@Override
	public int getLayerOffset() {
		return layerOffset;
	}

	@Override
	public int getTopOffset() {
		return topOffset;
	}

	@Override
	public int getLeftOffset() {
		return leftOffset;
	}
}
