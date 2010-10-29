package com.drawgraph;

import com.drawgraph.algorithms.AbstractCrossingReducer;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.graphics.GraphScalerImpl;
import com.drawgraph.graphics.SimpleGraphDrawer;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Date: Oct 29, 2010
 * Time: 6:39:52 PM
 *
 * @author denisk
 */
public class AbstractCrossingReducerTest {

	private MockAbstractCrossingReducer testable = new MockAbstractCrossingReducer();
	private static final int LAYER_LENGTH = 4;

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

	@Test
	public void testReduce() throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> graph = GraphMLTestUtils.parseGraph(GraphMLTestUtils.DIGRAPH_FILE_NAME);

		GraphScalerImpl scaler = new GraphScalerImpl();
		scaler.setLayerOffset(20);
		scaler.setLeftOffset(30);
		scaler.setMinDistance(40);
		scaler.setTopOffset(30);

		LayeredPositionedGraph positionedGraph =
				scaler.scale(graph, new SimpleLayeredGraphOrder(LAYER_LENGTH));

		LayeredPositionedGraph reducedGraph = testable.reduce(positionedGraph);

		List<List<PositionedNode>> initialLayers = positionedGraph.getLayers();
		List<List<PositionedNode>> reducedLayers = reducedGraph.getLayers();
		int layersCount = initialLayers.size();
		assertEquals(layersCount, reducedLayers.size());

		for (int i = 0; i < layersCount; i++) {
			List<PositionedNode> initialLayer = initialLayers.get(i);
			List<PositionedNode> reducedLayer = reducedLayers.get(i);

			assertTrue(initialLayer.containsAll(reducedLayer));
			assertTrue(reducedLayer.containsAll(initialLayer));
		}
	}
	private List<Map.Entry<PositionedNode,Integer>> getNodeswithWeights() {
		List<Map.Entry<PositionedNode,Integer>> nodes = new ArrayList<Map.Entry<PositionedNode, Integer>>();

		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test1", 1, LAYER_LENGTH), 12));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test2", 2, LAYER_LENGTH), 2));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test3", 3, LAYER_LENGTH), 1));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test4", 5, LAYER_LENGTH), 6));
		nodes.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(new PositionedNodeImpl("_test5", 7, LAYER_LENGTH), 3));

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
			List<Map.Entry<PositionedNode, Integer>> result = new ArrayList<Map.Entry<PositionedNode, Integer>>();
			for (int i = currentLayer.size() - 1; i >= 0; i--) {
				result.add(new AbstractMap.SimpleImmutableEntry<PositionedNode, Integer>(currentLayer.get(i), i));
			}
			return result;
		}
	}
}
