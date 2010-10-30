package com.drawgraph;

import com.drawgraph.algorithms.AbstractCrossingReducer;
import com.drawgraph.model.PositionedNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: Oct 30, 2010
 * Time: 6:55:49 PM
 *
 * @author denisk
 */
public class MockAbstractCrossingReducer  extends AbstractCrossingReducer {
			@Override
		public List<PositionedNode> reorder(List<Map.Entry<PositionedNode, Integer>> positions) {
			return super.reorder(positions);
		}

		@Override
		protected List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer) {
			List<Map.Entry<PositionedNode, Integer>> result = new ArrayList<Map.Entry<PositionedNode, Integer>>();
			for (int i = currentLayer.size() - 1; i >= 0; i--) {
				result.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(currentLayer.get(i), i));
			}
			return result;
		}

}
