package com.drawgraph;

import com.drawgraph.algorithms.BarycenterReducer;
import com.drawgraph.algorithms.MedianReducer;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.graphics.GraphScalerImpl;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
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
	public void medianBarycenterTest() throws IOException, SAXException, ParserConfigurationException {
		BarycenterReducer barycenterReducer = new BarycenterReducer();
		MedianReducer medianReducer = new MedianReducer();
		ArrayList<LayeredPositionedGraph> positionedGraphs = getPositionedGraphs(GraphMLTestUtils.getFilesInDirectories(GraphMLTestUtils.DAGS_DIRECTORY, GraphMLTestUtils.DIGRAPHS_DIRECTORY));

		for (LayeredPositionedGraph positionedGraph: positionedGraphs) {
			LayeredPositionedGraph barycenterReducedGraph = barycenterReducer.reduce(positionedGraph);
			LayeredPositionedGraph medianReducedGraph = medianReducer.reduce(positionedGraph);

			assertReducedGraph(positionedGraph, barycenterReducedGraph);
			assertReducedGraph(positionedGraph, medianReducedGraph);
		}
	}

	private ArrayList<LayeredPositionedGraph> getPositionedGraphs(ArrayList<File> files) throws IOException, SAXException, ParserConfigurationException {
		ArrayList<LayeredPositionedGraph> result = new ArrayList<LayeredPositionedGraph>(files.size());
		for (File file: files) {
			Graph<Node> graph = GraphMLTestUtils.parseGraph(file);

			GraphScalerImpl scaler = new GraphScalerImpl();
			scaler.setLayerOffset(20);
			scaler.setLeftOffset(30);
			scaler.setMinDistance(40);
			scaler.setTopOffset(30);

			SimpleLayeredGraphOrder layeredGraphOrder = new SimpleLayeredGraphOrder(LAYER_LENGTH);
			LayeredGraph<Node> layeredGraph = layeredGraphOrder.getLayeredGraph(graph);

					LayeredPositionedGraph positionedGraph = scaler.scale(layeredGraph);
			result.add(positionedGraph);
		}
		return result;
	}

	private void assertReducedGraph(LayeredPositionedGraph positionedGraph, LayeredPositionedGraph reducedGraph) {
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

}
