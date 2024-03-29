package com.drawgraph.graphics;

import com.drawgraph.algorithms.GraphUtils;
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
import java.util.Set;

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
	private int shift;
	private static final int DELTA = 30;

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
	public void setShift(int shift) {
		this.shift = shift;
	}

	@Override
	public <T extends Node<T>> LayeredPositionedGraph scale(LayeredGraph<T> graphWithDummies) {
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

		int horizontalShift = shift;
		int curX = dummiesEdge;
		int curY = topOffset;
		int curDummyX = dummiesEdge- minDistance;

		HashSet<PositionedNode> leadingDummiesSet = new HashSet<PositionedNode>();

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
				leadingDummiesSet.add(positionedNode);
				positionedLayer.add(0, positionedNode);

				curDummyX -= minDistance;
			}
			positionedLayers.add(positionedLayer);

			curX = dummiesEdge + horizontalShift;
			curDummyX = dummiesEdge- minDistance + horizontalShift;

			horizontalShift= horizontalShift + (horizontalShift/10);

			
			curY += layerOffset;
		}
		HashSet<T> sourceNodes = graphWithDummies.getNodes();
		assignSourcesSinks(sourceNodes, positionedNodes);

//		stretchDummyLines(positionedNodes, positionedLayers, leadingDummiesSet);
		if (REVERSE) {
			//put it back
			Collections.reverse(positionedLayers);
		}
		LayeredPositionedGraph result = new LayeredPositionedGraphImpl(graphWithDummies.getId(), positionedLayers);

		result.getNodes().addAll(positionedNodes);
		result.getLines().addAll(graphWithDummies.getLines());

		return result;
	}

	//todo think how to make this method more generic (eliminate PositionedNode)

	private <T extends Node<T>> void assignSourcesSinks(HashSet<T> sourceNodes, HashSet<PositionedNode> destNodes) {
		ArrayList<PositionedNode> positionedNodes = new ArrayList<PositionedNode>(destNodes);
		for (T n : sourceNodes) {
			int index = getNodeIndexInList(positionedNodes, n);
			PositionedNode equalNode = positionedNodes.get(index);
			for (T source : n.getSources()) {
				int sourceIndex = getNodeIndexInList(positionedNodes, source);
				PositionedNode positionedSource = positionedNodes.get(sourceIndex);
				equalNode.addSource(positionedSource);
			}
			for (T sink : n.getSinks()) {
				int sinkIndex = getNodeIndexInList(positionedNodes, sink);
				PositionedNode positionedSink = positionedNodes.get(sinkIndex);
				equalNode.addSink(positionedSink);
			}
		}
	}
	private int getNodeIndexInList(List<? extends Node> positionedNodes, Node n) {
		for (int i = 0; i < positionedNodes.size(); i++) {
			Node currentNode = positionedNodes.get(i);
			if (currentNode.getId().equals(n.getId())) {
				return i;
			}
		}
		throw new IllegalArgumentException("No node " + n + " in list " + positionedNodes);
		//		return positionedNodes.indexOf(n);
	}

	//todo this method should be moved into separate PositionedGraphTransformer and covered with unit test
	private void stretchDummyLines(HashSet<PositionedNode> positionedNodes,
								   List<? extends List<PositionedNode>> layers, HashSet<PositionedNode> leadingDummies) {
		GraphUtils gu = new GraphUtils();
//		HashSet<PositionedNode> trailingDummies = new HashSet<PositionedNode>();
//		HashSet<PositionedNode> leadingDummies = new HashSet<PositionedNode>();
//		for (List<PositionedNode> layer : layers) {
//			int layerSize = layer.size();
//			for (PositionedNode n : layer) {
//				if (n.isDummy()) {
//					int index = gu.getLayerIndexForNode(n, layers);
//					if (index <= layerSize / 2) {
//						leadingDummies.add(n);
//					} else {
//						trailingDummies.add(n);
//					}
//				}
//			}
//		}
//

		HashSet<PositionedNode> trailingDummies = new HashSet<PositionedNode>();
		HashSet<PositionedNode> temp = new HashSet<PositionedNode>(positionedNodes);
//		temp.removeAll(leadingDummies);
		for (PositionedNode node : temp) {
			if (node.isDummy()) {
				trailingDummies.add(node);
			}
		}

		for (PositionedNode node : positionedNodes) {
			if (node.isDummy()) {
				Set<PositionedNode> sources = node.getSources();

				if (sources.size() == 1 && !sources.iterator().next().isDummy()) {
					HashSet<PositionedNode> dummyChain = new HashSet<PositionedNode>();
					dummyChain.add(node);
					int layer0 = gu.getLayerIndexForNode(node, layers);
					if (leadingDummies.contains(node)) {
						//right
						int minX = node.getX();
						while (node.isDummy()) {
							Set<PositionedNode> localSinks = node.getSinks();
							if (localSinks.size() != 1) {
								throw new IllegalStateException("Multiple sinks for dummy node  " + node);
							}
							node = localSinks.iterator().next();
							if (node.isDummy()) {
								dummyChain.add(node);
								int x = node.getX();
								if (x < minX) {
									minX = x;
								}
							}
						}
						int layer1 = gu.getLayerIndexForNode(node, layers);
						assignX(dummyChain, leadingDummies, DELTA, minX, layers, layer0, layer1, gu);
					} else {
						//left
						int maxX = node.getX();
						while (node.isDummy()) {
							Set<PositionedNode> localSinks = node.getSinks();
							if (localSinks.size() != 1) {
								throw new IllegalStateException("Multiple sinks for dummy node  " + node);
							}
							node = localSinks.iterator().next();
							if (node.isDummy()) {
								dummyChain.add(node);
								int x = node.getX();
								if (x > maxX) {
									maxX = x;
								}
							}
						}
						int layer1 = gu.getLayerIndexForNode(node, layers);
						assignX(dummyChain, trailingDummies, -DELTA, maxX, layers, layer0, layer1, gu);
					}
				}
			}
		}
	}

	private void assignX(HashSet<PositionedNode> dummyChain,
						 HashSet<PositionedNode> allNodes,
						 int delta,
						 int x,
						 List<? extends List<? extends Node>> layers, int layer0, int layer1, GraphUtils gu) {
		x = makeComfortable(x, layer0, layer1, allNodes, delta, layers, gu);

		for (PositionedNode node : dummyChain) {
			node.setX(x);
		}
	}

	//todo this method should be rethought
	private int makeComfortable(int x,
								int layer0,
								int layer1,
								HashSet<PositionedNode> allNodes,
								int delta,
								List<? extends List<? extends Node>> layers, GraphUtils gu) {
		for (PositionedNode node : allNodes) {
			if (node.isDummy()) {
				int layerIndex = gu.getLayerIndexForNode(node, layers);
				if ((layerIndex >= layer0 && layerIndex <= layer1) || (layerIndex <= layer0 && layerIndex >= layer1)) {
					int nodeX = node.getX();
					if (Math.abs(Math.abs(x) - nodeX) < Math.abs(delta)) {
						return makeComfortable(x + delta, layer0, layer1, allNodes, delta, layers, gu);
					}
				}
			}
		}

		return x;
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
