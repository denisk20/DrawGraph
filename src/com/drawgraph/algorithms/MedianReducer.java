package com.drawgraph.algorithms;

import com.drawgraph.model.PositionedNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Oct 29, 2010
 * Time: 2:19:45 PM
 *
 * @author denisk
 */
public class MedianReducer extends AbstractCrossingReducer {
	@Override
	protected List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer) {
		List<Map.Entry<PositionedNode, Integer>> result = new ArrayList<Map.Entry<PositionedNode, Integer>>();

		for (int i = 0; i<currentLayer.size(); i++) {
			PositionedNode node = currentLayer.get(i);
			int weight = 0;
			Set<PositionedNode> sources = node.getSources();
			int sourcesCount = sources.size();

			ArrayList<PositionedNode> positionedNode = new ArrayList<PositionedNode>(sources);
			Collections.sort(positionedNode, new PositionedNodeHorizontalComparator());
			if (sourcesCount > 0) {
				int middleIndex = sourcesCount / 2;
				PositionedNode middleNode = positionedNode.get(middleIndex);
				weight = middleNode.getX();
			}
			result.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(node, weight));
		}
		return result;
	}

	private static class PositionedNodeHorizontalComparator implements Comparator<PositionedNode>{
		@Override
		public int compare(PositionedNode o1, PositionedNode o2) {
			return new Integer(o1.getX()).compareTo(new Integer(o2.getX()));
		}

	}
}
