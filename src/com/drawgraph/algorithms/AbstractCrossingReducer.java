package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.LayeredPositionedGraphImpl;
import com.drawgraph.model.PositionedNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Oct 25, 2010
 * Time: 4:26:56 PM
 *
 * @author denisk
 */
public abstract class AbstractCrossingReducer implements CrossingReducer{
	@Override
	public LayeredPositionedGraph reduce(LayeredPositionedGraph source) {
		List<List<PositionedNode>> layers = source.getLayers();
		List<List<PositionedNode>> resultingLayers = new ArrayList<List<PositionedNode>>();
		int layersCount = layers.size();
		if (layersCount > 1) {
			final HashSet<PositionedNode> allResultNodes = new HashSet<PositionedNode>();

			List<PositionedNode> firstLayer = layers.get(0);
			allResultNodes.addAll(firstLayer);
			resultingLayers.add(firstLayer);
			for (int i = 1; i < layersCount; i++) {
				List<PositionedNode> bottomLayer = layers.get(i-1);
				List<PositionedNode> currentLayer = layers.get(i);

				List<Map.Entry<PositionedNode,Integer>> positions = getNodeWeights(currentLayer, bottomLayer);
				List<PositionedNode> reorderedCurrentLayer = reorder(positions);

				resultingLayers.add(reorderedCurrentLayer);
				allResultNodes.addAll(reorderedCurrentLayer);
			}
			LayeredPositionedGraph result = new LayeredPositionedGraphImpl(source.getId(), resultingLayers);
			result.setRadius(source.getRadius());
			result.getNodes().addAll(allResultNodes);
			result.getLines().addAll(source.getLines());

			return result;
		} else {
			return source;
		}
	}

	protected List<PositionedNode> reorder(List<Map.Entry<PositionedNode,Integer>> positions) {
		ArrayList<PositionedNode> result = new ArrayList<PositionedNode>();
		ArrayList<Map.Entry<PositionedNode,Integer>> nodes =
				new ArrayList<Map.Entry<PositionedNode,Integer>>(positions);

		boolean oneMoreTime = true;

		while (oneMoreTime) {
			oneMoreTime = false;
			for (int i = 1; i < nodes.size(); i++) {
				Map.Entry<PositionedNode, Integer> previousEntry = nodes.get(i-1);
				Map.Entry<PositionedNode, Integer> currentEntry = nodes.get(i);
				if (previousEntry.getValue() > currentEntry.getValue()) {
					//change them
					PositionedNode previousNode = previousEntry.getKey();
					PositionedNode currentNode = currentEntry.getKey();

					int previousX = previousNode.getX();
					int currentX = currentNode.getX();

					previousNode.setX(currentX);
					currentNode.setX(previousX);

					nodes.remove(previousEntry);
					nodes.remove(currentEntry);

					nodes.add(i-1, currentEntry);
					nodes.add(i, previousEntry);

					oneMoreTime = true;
				}
			}
		}

		for (Map.Entry<PositionedNode, Integer> entry : nodes) {
			result.add(entry.getKey());
		}
		return result;
	}

	protected abstract List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer, List<PositionedNode> bottomLayer);
}
