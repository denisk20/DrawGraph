package com.drawgraph;

import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.GraphImpl;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Date: Oct 23, 2010
 * Time: 1:44:44 PM
 *
 * @author denisk
 */
public class LayeredGraphOrderTest {
	private static final int NODES_COUNT = 10;
	private static final int REAL_GRAPH_EXPECTED_LAYERS_COUNT = 2;
	private static final int LAYER_LENGTH = 2;
	private static final int EXPECTED_LAYERS_COUNT = 5;

	private SimpleLayeredGraphOrder testable = new SimpleLayeredGraphOrder(LAYER_LENGTH);

	@Test
	public void testSimpleLayeredGraphOrder() throws IOException, SAXException, ParserConfigurationException {
		createGraphAndGetLayers(NODES_COUNT, LAYER_LENGTH, EXPECTED_LAYERS_COUNT);
	}

	@Test
	public void extraPlacesInLayer() {
		createGraphAndGetLayers(NODES_COUNT, NODES_COUNT -1 , 2);
	}
	private void createGraphAndGetLayers(int nodesCount, int layerLength, int expectedLayersCount) {
		Graph<SimpleNode> g = new GraphImpl("test graph");
		g.getNodes().addAll(createNodes(nodesCount));

		testable.setLayerLength(layerLength);

		LayeredGraph<SimpleNode> layeredGraph = testable.getLayeredGraph(g);
		assertEquals(expectedLayersCount, layeredGraph.getLayers().size());
	}

	@Test
	public void realGraph() throws IOException, SAXException, ParserConfigurationException {
		Graph<SimpleNode> g = GraphMLTestUtils.parseGraph();
		testable.setLayerLength(LAYER_LENGTH);
		LayeredGraph<SimpleNode> layeredGraph = testable.getLayeredGraph(g);
		assertEquals(REAL_GRAPH_EXPECTED_LAYERS_COUNT, layeredGraph.getLayers().size());
	}


	private ArrayList<SimpleNode> createNodes(int count) {
		ArrayList<SimpleNode> result = new ArrayList<SimpleNode>();
		for (int i = 0; i < count; i++) {
			SimpleNode node = new SimpleNode("_test_" + i);
			result.add(node);
		}

		return result;
	}
}
