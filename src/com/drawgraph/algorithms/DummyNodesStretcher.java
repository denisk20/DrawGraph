package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.LayeredPositionedGraphImpl;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: Nov 11, 2010
 * Time: 6:22:24 PM
 *
 * @author denisk
 */
public class DummyNodesStretcher implements PositionedGraphTransformer {
	public static final int DELTA = 30;

	@Override
	public LayeredPositionedGraph transform(LayeredPositionedGraph graph) {
		LayeredPositionedGraphImpl copy = graph.copy();
//		System.out.println("Nodes are equals before stretch: " + copy.getNodes().equals(graph.getNodes()));
		stretchDummyLines(copy.getNodes(), copy.getLayers());
//		stretchDummyLines(graph.getNodes(), graph.getLayers());

//		System.out.println("Nodes are equals after stretch: " + copy.getNodes().equals(graph.getNodes()));
		return copy;
	}

	private void stretchDummyLines(HashSet<PositionedNode> positionedNodes,
								   List<? extends List<PositionedNode>> layers) {
		GraphUtils gu = new GraphUtils();
		HashSet<PositionedNode> trailingDummies = new HashSet<PositionedNode>();
		HashSet<PositionedNode> leadingDummies = new HashSet<PositionedNode>();
		for (List<PositionedNode> layer : layers) {
			int layerSize = layer.size();
			for (PositionedNode n : layer) {
				if (n.isDummy()) {
					int index = gu.getLayerIndexForNode(n, layers);
					if (index <= layerSize / 2) {
						leadingDummies.add(n);
					} else {
						trailingDummies.add(n);
					}
				}
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
						 List<? extends List<? extends Node>> layers,
						 int layer0,
						 int layer1,
						 GraphUtils gu) {
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
								List<? extends List<? extends Node>> layers,
								GraphUtils gu) {
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
}
