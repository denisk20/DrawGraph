package com.drawgraph;

import com.drawgraph.graphics.SimpleLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.GraphImpl;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;

/**
 * Date: Oct 23, 2010
 * Time: 1:44:44 PM
 *
 * @author denisk
 */
public class LayeredGraphOrderTest {
	private SimpleLayeredGraphOrder testable = new SimpleLayeredGraphOrder();
	private static final int NODES_COUNT = 10;
	private static final int LAYER_LENGTH = 2;
	private static final int EXPECTED_LAYERS_COUNT = 5;

	@Test
	public void testSimpleLayeredGraphOrder() throws IOException, SAXException, ParserConfigurationException {
		Graph g = new GraphImpl("test graph");
		g.getNodes().addAll(createNodes(NODES_COUNT));

		testable.setLayerLength(LAYER_LENGTH);

		List<List<Node>> layers = testable.getLayers(g);
		assertEquals(EXPECTED_LAYERS_COUNT, layers.size());
	}

	private ArrayList<Node> createNodes(int count) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (int i = 0; i < count; i++) {
			SimpleNode node = new SimpleNode("_test_" + i);
			result.add(node);
		}

		return result;
	}
}
