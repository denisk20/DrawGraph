package com.drawgraph;

import com.drawgraph.algorithms.GraphUtils;
import com.drawgraph.algorithms.SimpleDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LineImpl;
import com.drawgraph.model.Node;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;
/**
 * Date: Oct 30, 2010
 * Time: 8:30:33 AM
 *
 * @author denisk
 */
public class SimpleDummyNodesAssignerTest {

	private SimpleDummyNodesAssigner testable = new SimpleDummyNodesAssigner();
	private static final int LAYER_LENGTH = 5;

	private HashMap<Node, HashSet<Node>> nodeDummyLines = new HashMap<Node, HashSet<Node>>();
	@Test
	public void testGetLayersWithDummiesAssigned() throws IOException, SAXException, ParserConfigurationException {
		assertDummies(GraphMLTestUtils.DIGRAPH_FILE_NAME);
	}

	@Test
	public void testSimpleGraph() throws IOException, SAXException, ParserConfigurationException {
		assertDummies(GraphMLTestUtils.PURE_SOURCE_SINK_FILE_NAME);
	}

	private void assertDummies(String fileName) throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> graph = GraphMLTestUtils.parseGraph(fileName);
		SimpleLayeredGraphOrder graphOrder = new SimpleLayeredGraphOrder(LAYER_LENGTH);

		LayeredGraph<Node> layeredGraph = graphOrder.getLayeredGraph(graph);

		List<List<Node>> initialLayers = layeredGraph.getLayers();
		HashMap<Integer, Integer> expectedDummiesCountMap = getExpectedDummiesCount(initialLayers);

		LayeredGraph<Node> layeredWithDummies = testable.assignDummyNodes(layeredGraph);
		List<List<Node>> layers = layeredWithDummies.getLayers();
		GraphUtils gu = new GraphUtils();
		for (int i = 0; i<layers.size(); i++) {
			List<Node> layer = layers.get(i);

			int expectedDummiesCount = expectedDummiesCountMap.get(i);
			int actualDummiesCount = gu.getDummiesCount(layer);

			assertEquals(expectedDummiesCount, actualDummiesCount);
		}

		for (Map.Entry<Node, HashSet<Node>> dummyLine : nodeDummyLines.entrySet()) {
			Node mainSource = dummyLine.getKey();
			HashSet<Node> sinks = dummyLine.getValue();

			for (Node sink : sinks) {
				assertFalse(mainSource.getSinks().contains(sink));
				Set<Node> sources = sink.getSources();
				assertFalse(sources.contains(mainSource));

				boolean success=false;
				for (Node source : sources) {
					if (source.isDummy()) {

						Node<Node> current = source;
						Node<Node> previous = sink;

						while (current.isDummy()) {
							LineImpl line = new LineImpl(previous, current, previous.getId() + "->" + current.getId());
							//todo this should be done as well
							assertTrue(layeredWithDummies.getNodes().contains(current));
							assertTrue(layeredWithDummies.getLines().contains(line));
							Set<Node> dummiesSources = current.getSources();
							Set<Node> dummiesSinks = current.getSinks();
							assertTrue(dummiesSources.size() == 1);
							assertTrue(dummiesSinks.size() == 1);
							assertEquals(dummiesSinks.iterator().next(), previous);

							previous = current;
							current = previous.getSources().iterator().next();
						}
						LineImpl line = new LineImpl(previous, current, previous.getId() + "->" + current.getId());
						assertTrue(layeredWithDummies.getLines().contains(line));

						if (current.equals(mainSource)) {
							success = true;
							break;
						}
					}
				}
				assertTrue(success);
			}
		}
		
	}

	private HashMap<Integer, Integer> getExpectedDummiesCount(List<List<Node>> layers) {
		GraphUtils gu = new GraphUtils();
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (int i = 0; i < layers.size(); i++) {
			result.put(i, 0);
		}
		for (int i = 2; i < layers.size(); i++) {
			List<Node> layer = layers.get(i);
			for (Node<Node> n : layer) {
				for (Node sink : n.getSinks()) {
					int sinkIndex = gu.getLayerIndexForNode(sink, layers);
					if (i - sinkIndex > 1) {
						for (int j = sinkIndex + 1; j < i; j++) {
							int value = result.get(j);
							result.put(j, ++value);

							HashSet<Node> dummyLines;
							if (nodeDummyLines.containsKey(n)) {
								dummyLines = nodeDummyLines.get(n);
							} else {
								dummyLines = new HashSet<Node>();
								nodeDummyLines.put(n, dummyLines);
							}
							dummyLines.add(sink);
						}
					}
				}
			}
		}

		return result;
	}

}
