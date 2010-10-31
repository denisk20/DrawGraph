package com.drawgraph.graphics;

import com.drawgraph.algorithms.DummyNodesAssigner;
import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.LayeredPositionedGraphImpl;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	private static final boolean REVERSE = true;

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
	public LayeredPositionedGraph scale(LayeredGraph<? extends Node> graphWithDummies) {
		List<? extends List<? extends Node>> layers = new ArrayList<List<? extends Node>>(graphWithDummies.getLayers());

		if (REVERSE) {
			Collections.reverse(layers);
		}

		HashSet<PositionedNode> positionedNodes = new HashSet<PositionedNode>();

		List<List<PositionedNode>> positionedLayers = new ArrayList<List<PositionedNode>>();
		int maxDummiesCount = 0;
		HashMap<Integer, List<Node>> leadingDummies = new HashMap<Integer, List<Node>>();
		for (int i=0; i<layers.size(); i++) {
			List<? extends Node> layer = layers.get(i);
			List<Node> currentLeadingDummies = new ArrayList<Node>();
			int dummiesCount = 0;
			Node n = layer.get(dummiesCount);
			while (n.isDummy()) {
				dummiesCount++;
				currentLeadingDummies.add(n);
				n = layer.get(dummiesCount);
			}
			leadingDummies.put(i, currentLeadingDummies);

			if (dummiesCount > maxDummiesCount) {
				maxDummiesCount = dummiesCount;
			}
		}

		int dummiesEdge = leftOffset + maxDummiesCount*minDistance;

		int curX = dummiesEdge;
		int curY = topOffset;
		int curDummyX = dummiesEdge- minDistance;
		for (int i = 0; i<layers.size(); i++) {
			List<? extends Node> layer = layers.get(i);

			List<PositionedNode> positionedLayer = new ArrayList<PositionedNode>();
			List<Node> withoutLeadingDummies = new ArrayList<Node>(layer);
			List<Node> currentLeadingDummies = leadingDummies.get(i);
			withoutLeadingDummies.removeAll(currentLeadingDummies);
			for (int j = 0; j<withoutLeadingDummies.size(); j++) {
				Node n = withoutLeadingDummies.get(j);
				int x = curX;
				int y = curY;
				PositionedNode positionedNode = new PositionedNodeImpl(n.getId(), x, y);
				if (n.isDummy()) {
					positionedNode.setDummy(true);
				}
				positionedNodes.add(positionedNode);

				positionedLayer.add(positionedNode);

				curX += minDistance;
			}
			for (int j = currentLeadingDummies.size() - 1; j >= 0; j--) {
				Node n = currentLeadingDummies.get(j);
				if (!n.isDummy()) {
					throw new IllegalArgumentException("Node is not dummy: "+n);
				}
				int x = curDummyX;
				int y = curY;

				PositionedNode positionedNode = new PositionedNodeImpl(n.getId(), x, y);
				positionedNode.setDummy(true);
				positionedNodes.add(positionedNode);

				positionedLayer.add(0, positionedNode);

				curDummyX -= minDistance;
			}
			positionedLayers.add(positionedLayer);
			curX = dummiesEdge;
			curDummyX = dummiesEdge- minDistance;
			curY += layerOffset;
		}
		assignSourcesSinks(graphWithDummies.getNodes(), positionedNodes);

		if (REVERSE) {
			//put it back
			Collections.reverse(positionedLayers);
		}
		LayeredPositionedGraph result = new LayeredPositionedGraphImpl(graphWithDummies.getId(), positionedLayers);

		result.getNodes().addAll(positionedNodes);
		result.getLines().addAll(graphWithDummies.getLines());

		return result;
	}

	private void assignSourcesSinks(HashSet<? extends Node> nodes, HashSet<PositionedNode> destNodes) {
		ArrayList<PositionedNode> positionedNodes = new ArrayList<PositionedNode>(destNodes);
		for (Node<Node> n : nodes) {
			int index = positionedNodes.indexOf(n);
			PositionedNode equalNode = positionedNodes.get(index);
			for (Node source : n.getSources()) {
				int sourceIndex = positionedNodes.indexOf(source);
				PositionedNode positionedSource = positionedNodes.get(sourceIndex);
				equalNode.addSource(positionedSource);
			}
			for (Node sink : n.getSinks()) {
				int sinkIndex = positionedNodes.indexOf(sink);
				PositionedNode positionedSink = positionedNodes.get(sinkIndex);
				equalNode.addSink(positionedSink);
			}
		}
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
