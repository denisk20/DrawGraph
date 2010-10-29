package com.drawgraph;

import com.drawgraph.algorithms.AbstractCrossingReducer;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Date: Oct 29, 2010
 * Time: 6:39:52 PM
 *
 * @author denisk
 */
public class AbstractCrossingReducerTest {

	private MockAbstractCrossingReducer testable = new MockAbstractCrossingReducer();
	@Test
	public void testReorder() {
		List<Map.Entry<PositionedNode, Integer>> entries = getNodeswithWeights();

		HashMap<PositionedNode, Integer> weights = new HashMap<PositionedNode, Integer>();
		for (Map.Entry<PositionedNode, Integer> entry : entries) {
			weights.put(entry.getKey(), entry.getValue());
		}
		List<PositionedNode> result = testable.reorder(entries);
		for (int i = 1; i < result.size(); i++) {
			PositionedNode previousNode = result.get(i-1);
			PositionedNode currentNode = result.get(i);
			assertTrue(previousNode.getX() <= currentNode.getX());
			assertTrue(weights.get(previousNode) <= weights.get(currentNode));
		}

	}

	private List<Map.Entry<PositionedNode,Integer>> getNodeswithWeights() {
		List<Map.Entry<PositionedNode,Integer>> nodes = new ArrayList<Map.Entry<PositionedNode, Integer>>();

		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test1", 1, 4), 12));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test2", 2, 4), 2));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test3", 3, 4), 1));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test4", 5, 4), 6));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test5", 7, 4), 3));

		return nodes;
	}

	private static class MockAbstractCrossingReducer extends AbstractCrossingReducer {
		@Override
		public List<PositionedNode> reorder(List<Map.Entry<PositionedNode, Integer>> positions) {
			return super.reorder(positions);
		}

		@Override
		protected List<Map.Entry<PositionedNode, Integer>> getNodeWeights(List<PositionedNode> currentLayer,
																	List<PositionedNode> bottomLayer) {
			return null;
		}
	}
}
