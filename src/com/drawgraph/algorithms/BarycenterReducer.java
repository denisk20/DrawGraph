package com.drawgraph.algorithms;

import com.drawgraph.model.PositionedNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Oct 25, 2010
 * Time: 4:27:48 PM
 *
 * @author denisk
 */
public class BarycenterReducer extends AbstractCrossingReducer {
	@Override
	protected List<Map.Entry<PositionedNode,Integer>> getNodeWeights(List<PositionedNode> currentLayer) {
		List<Map.Entry<PositionedNode, Integer>> result = new ArrayList<Map.Entry<PositionedNode, Integer>>();

		for (int i = 0; i<currentLayer.size(); i++) {
			PositionedNode node = currentLayer.get(i);
			int weight = 0;
			Set<PositionedNode> sources = node.getSources();
			int sourcesCount = sources.size();
			if (sourcesCount > 0) {
				int totalPosition = 0;
				for (PositionedNode source : sources) {
					totalPosition += source.getX();
				}

				weight = totalPosition / sourcesCount;
			}

			result.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(node, weight));
		}
		return result;
	}
}
