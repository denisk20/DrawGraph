package com.drawgraph.algorithms;

import com.drawgraph.model.PositionedNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Oct 29, 2010
 * Time: 2:19:45 PM
 *
 * @author denisk
 */
public class MedianReducer extends AbstractCrossingReducer{
	@Override
	protected List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer) {
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
