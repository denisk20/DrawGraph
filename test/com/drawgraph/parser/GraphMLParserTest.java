package com.drawgraph.parser;

import com.drawgraph.GraphMLTestUtils;
import com.drawgraph.model.Graph;
import com.drawgraph.model.Line;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.callbacks.LineCallback;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Date: Oct 22, 2010
 * Time: 11:55:56 AM
 *
 * @author denisk
 */
public class GraphMLParserTest {

	public GraphMLParserTest() {
	}

	@Test
	public void parseGraphMLDocument() throws IOException, SAXException, ParserConfigurationException {
		System.out.println("Starting test");
		final Graph<SimpleNode> graph = GraphMLTestUtils.parseGraph();
		assertEquals(GraphMLTestUtils.GRAPH_NAME, graph.getId());

		final HashSet<SimpleNode> nodes = graph.getNodes();
		for (Node n : nodes) {
			assertTrue(GraphMLTestUtils.NODES_SET.contains(n.getId()));
			HashSet<Node> sources = GraphMLTestUtils.getSourcesForNode(n);
			HashSet<Node> sinks = GraphMLTestUtils.getSinksForNode(n);

			assertEquals(sources, n.getSources());
			assertEquals((sinks), n.getSinks());

			sources.addAll(sinks);
			assertEquals(sources, n.getNeighbours());
		}
		
		for (Line l : graph.getLines()) {
			String id = l.getId();
			String source = l.getSource().getId();
			String sink = l.getSink().getId();
			LineCallback.LineSkeleton skeleton =
					new LineCallback.LineSkeleton(id, source, sink);

			assertTrue(GraphMLTestUtils.LINES_SET.contains(skeleton));
		}
	}
}
