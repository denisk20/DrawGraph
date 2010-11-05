package com.drawgraph.algorithms;

import com.drawgraph.model.PositionedNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Nov 5, 2010
 * Time: 6:20:35 PM
 *
 * This total class is nothing but a cheat
 *
 * @author denisk
 */
public class CoordinateAssignmentReducer extends AbstractCrossingReducer {
	private HashMap<Set<PositionedNode>, List<Integer>> cachedCalls = new HashMap<Set<PositionedNode>, List<Integer>>();
	@Override
	protected List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer) {
		List<Map.Entry<PositionedNode, Integer>> result = new ArrayList<Map.Entry<PositionedNode, Integer>>();

		int capacity = currentLayer.size();
		List<Integer> weights;
		HashSet<PositionedNode> positionedNodeHashSet = new HashSet<PositionedNode>(currentLayer);
		if (cachedCalls.containsKey(positionedNodeHashSet)) {
			weights = cachedCalls.get(positionedNodeHashSet);
		} else {
			weights = new ArrayList<Integer>(capacity);

			for (int i = 0; i < capacity; i++) {
				weights.add(i);
			}

			//oh boy, I love this stuff )))
			Collections.shuffle(weights);
			cachedCalls.put(positionedNodeHashSet, weights);
		}

		for (int i = 0; i < capacity; i++) {
			result.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(currentLayer.get(i), weights.get(i)));
		}

		return result;
	}
}
