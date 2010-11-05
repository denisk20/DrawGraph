package com.drawgraph;

import com.drawgraph.algorithms.GraphUtils;
import com.drawgraph.algorithms.SimpleDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LineImpl;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Date: Oct 30, 2010
 * Time: 8:30:33 AM
 *
 * @author denisk
 */
public class SimpleDummyNodesAssignerTest {

	private SimpleDummyNodesAssigner testable = new SimpleDummyNodesAssigner();
	private static final int LAYER_LENGTH = 5;

	private HashMap<SimpleNode, HashSet<SimpleNode>> nodeDummyLines = new HashMap<SimpleNode, HashSet<SimpleNode>>();
	@Test
	public void testGetLayersWithDummiesAssigned() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<File> files = GraphMLTestUtils
				.getFilesInDirectories(GraphMLTestUtils.DAGS_DIRECTORY, GraphMLTestUtils.DIGRAPHS_DIRECTORY);

		for (File file: files) {
			Graph<SimpleNode> graph = GraphMLTestUtils.parseGraph(file);
			assertDummies(graph);
			nodeDummyLines.clear();
		}
	}

	private void assertDummies(Graph<SimpleNode> graph) throws IOException, SAXException, ParserConfigurationException {
		SimpleLayeredGraphOrder graphOrder = new SimpleLayeredGraphOrder(LAYER_LENGTH);

		LayeredGraph<SimpleNode> layeredGraph = graphOrder.getLayeredGraph(graph);

		List<List<SimpleNode>> initialLayers = layeredGraph.getLayers();
		HashMap<Integer, Integer> expectedDummiesCountMap = getExpectedDummiesCount(initialLayers);

		LayeredGraph<SimpleNode> layeredWithDummies = testable.assignDummyNodes(layeredGraph);
		List<List<SimpleNode>> layers = layeredWithDummies.getLayers();
		GraphUtils gu = new GraphUtils();
		for (int i = 0; i<layers.size(); i++) {
			List<SimpleNode> layer = layers.get(i);

			int expectedDummiesCount = expectedDummiesCountMap.get(i);
			int actualDummiesCount = gu.getDummiesCount(layer);

			assertEquals(expectedDummiesCount, actualDummiesCount);
		}

		for (Map.Entry<SimpleNode, HashSet<SimpleNode>> dummyLine : nodeDummyLines.entrySet()) {
			SimpleNode mainSource = dummyLine.getKey();
			HashSet<SimpleNode> sinks = dummyLine.getValue();

			for (SimpleNode sink : sinks) {
				assertFalse(mainSource.getSinks().contains(sink));
				Set<SimpleNode> sources = sink.getSources();
				assertFalse(sources.contains(mainSource));

				boolean success=false;
				for (SimpleNode source : sources) {
					if (source.isDummy()) {

						SimpleNode current = source;
						SimpleNode previous = sink;

						while (current.isDummy()) {
							LineImpl line = new LineImpl(current, previous, current.getId() + "->" + previous.getId());
							//todo this should be done as well
							assertTrue(layeredWithDummies.getNodes().contains(current));
							assertTrue(layeredWithDummies.getLines().contains(line));
							Set<SimpleNode> dummiesSources = current.getSources();
							Set<SimpleNode> dummiesSinks = current.getSinks();
							assertTrue(dummiesSources.size() == 1);
							assertTrue(dummiesSinks.size() == 1);
							assertEquals(dummiesSinks.iterator().next(), previous);

							previous = current;
							current = previous.getSources().iterator().next();
						}
						LineImpl line = new LineImpl(current, previous, current.getId() + "->" + previous.getId());
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

	private HashMap<Integer, Integer> getExpectedDummiesCount(List<List<SimpleNode>> layers) {
		GraphUtils gu = new GraphUtils();
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (int i = 0; i < layers.size(); i++) {
			result.put(i, 0);
		}
		for (int i = 2; i < layers.size(); i++) {
			List<SimpleNode> layer = layers.get(i);
			for (SimpleNode n : layer) {
				for (SimpleNode sink : n.getSinks()) {
					int sinkIndex = gu.getLayerIndexForNode(sink, layers);
					if (i - sinkIndex > 1) {
						for (int j = sinkIndex + 1; j < i; j++) {
							int value = result.get(j);
							result.put(j, ++value);

							HashSet<SimpleNode> dummyLines;
							if (nodeDummyLines.containsKey(n)) {
								dummyLines = nodeDummyLines.get(n);
							} else {
								dummyLines = new HashSet<SimpleNode>();
								nodeDummyLines.put(n, dummyLines);
							}
							dummyLines.add(sink);
						}
					}
				}
			}
		}

		for (int i = layers.size() -3 ; i >=0; i--) {
			List<SimpleNode> layer = layers.get(i);
			for (SimpleNode n : layer) {
				for (SimpleNode sink : n.getSinks()) {
					int sinkIndex = gu.getLayerIndexForNode(sink, layers);
					if (sinkIndex - i > 1) {
						for (int j = i + 1; j < sinkIndex; j++) {
							int value = result.get(j);
							result.put(j, ++value);

							HashSet<SimpleNode> dummyLines;
							if (nodeDummyLines.containsKey(n)) {
								dummyLines = nodeDummyLines.get(n);
							} else {
								dummyLines = new HashSet<SimpleNode>();
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
