package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.PositionedNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Date: Nov 11, 2010
 * Time: 6:22:24 PM
 *
 * @author denisk
 */
public class DummyNodesStretcher implements PositionedGraphTransformer{
	@Override
	public LayeredPositionedGraph transform(LayeredPositionedGraph graph) {
		HashMap<Integer, List<PositionedNode>> leadingDummies = getLeadingDummies(graph);
		HashMap<Integer, List<PositionedNode>> trailingDummies = getTrailingDummies(graph);

		HashMap<PositionedNode, Integer> shifts = getLeadingShifts(leadingDummies);
		return null;
	}

	private HashMap<PositionedNode, Integer> getLeadingShifts(HashMap<Integer, List<PositionedNode>> leadingDummies) {
		Set<Integer> layerIndexes = leadingDummies.keySet();
		for (Integer layerIndex : layerIndexes) {
			List<PositionedNode> layer = leadingDummies.get(layerIndex);
			int size = layer.size();
			for (int i = 0; i < size; i++) {
				PositionedNode node = layer.get(i);

				Set<PositionedNode> sources = node.getSources();
				Set<PositionedNode> sinks = node.getSinks();

				if (sources.size() == 1 && !sources.iterator().next().isDummy()) {
					HashSet<PositionedNode> dummyChain = new HashSet<PositionedNode>();
					dummyChain.add(node);
					//left
					int minX = node.getX();
					while (node.isDummy()) {
						Set<PositionedNode> localSinks = node.getSinks();
						if (localSinks.size() != 1) {
							throw new IllegalStateException("Multiple sinks for dummy node  " + node);
						}
						node = localSinks.iterator().next();
						if (node.isDummy()) {
							int x = node.getX();
							if (x < minX) {
								minX = x;
							}
						}
					}
//					assignX(dummyChain, minX);
				}

			}
		}
		return null;
	}

	private HashMap<Integer, List<PositionedNode>> getLeadingDummies(LayeredPositionedGraph source) {
		HashMap<Integer, List<PositionedNode>> leadingDummies = new HashMap<Integer, List<PositionedNode>>();
		List<List<PositionedNode>> layers = source.getLayers();
		for (int i = 0; i < layers.size(); i++) {
			List<PositionedNode> layer = layers.get(i);
			ArrayList<PositionedNode> currentLeadingDummies = new ArrayList<PositionedNode>();
			Iterator<PositionedNode> iterator = layer.iterator();
			PositionedNode leadingNode = iterator.next();
			while (leadingNode.isDummy()) {
				currentLeadingDummies.add(leadingNode);
				leadingNode = iterator.next();
			}

			leadingDummies.put(i, currentLeadingDummies);
		}

		return leadingDummies;
	}

	private HashMap<Integer, List<PositionedNode>> getTrailingDummies(LayeredPositionedGraph source) {
		HashMap<Integer, List<PositionedNode>> trailingDummies = new HashMap<Integer, List<PositionedNode>>();
		List<List<PositionedNode>> layers = source.getLayers();
		for (int i = 0; i < layers.size(); i++) {
			List<PositionedNode> layer = layers.get(i);
			int index = layer.size() - 1;
			PositionedNode trailingNode = layer.get(index);
			ArrayList<PositionedNode> currentTrailingDummies = new ArrayList<PositionedNode>();
			while (trailingNode.isDummy()) {
				currentTrailingDummies.add(trailingNode);
				index--;
				trailingNode = layer.get(index);
			}

			trailingDummies.put(i, currentTrailingDummies);
		}

		return trailingDummies;
	}
}
