package com.drawgraph;

import com.drawgraph.algorithms.DummyNodesAssigner;
import com.drawgraph.algorithms.DummyNodesStretcher;
import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.algorithms.PositionedGraphTransformer;
import com.drawgraph.algorithms.SimpleDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.graphics.GraphScaler;
import com.drawgraph.graphics.GraphScalerImpl;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.LayeredPositionedGraphImpl;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;
import com.drawgraph.model.SimpleNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Date: Nov 14, 2010
 * Time: 2:15:06 PM
 *
 * @author denisk
 */
public class DummyNodesStretcherTest {
	private LayeredGraphOrder order = new SimpleLayeredGraphOrder(3);
	DummyNodesAssigner dummyNodesAssigner = new SimpleDummyNodesAssigner();
	private GraphScaler scaler = new GraphScalerImpl();

	private PositionedGraphTransformer stretcher = new DummyNodesStretcher();

	@Before
	public void setUp() {
		scaler.setMinDistance(30);
		scaler.setShift(30);
		scaler.setLayerOffset(20);
		scaler.setLeftOffset(20);
		scaler.setTopOffset(20);
	}
	@Test
	public void testStretch() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<File> files = GraphMLTestUtils
				.getFilesInDirectories(GraphMLTestUtils.DAGS_DIRECTORY, GraphMLTestUtils.DIGRAPHS_DIRECTORY);

		for (File file : files) {
			assertStretcher(file);
		}
	}

	private void assertStretcher(File file) throws IOException, SAXException, ParserConfigurationException {
		Graph<SimpleNode> graph = GraphMLTestUtils.parseGraph(file);
		LayeredGraph<SimpleNode> layeredGraph = order.getLayeredGraph(graph);
		LayeredGraph<SimpleNode> layeredWithDummies = dummyNodesAssigner.assignDummyNodes(layeredGraph);
		LayeredPositionedGraph layeredPositionedGraph = scaler.scale(layeredWithDummies);

		LayeredPositionedGraph stretched = stretcher.transform(layeredPositionedGraph);

		assertEquals(stretched.getNodes(), layeredPositionedGraph.getNodes());
		List<List<PositionedNode>> layers = stretched.getLayers();
		assertEquals(layers, layeredPositionedGraph.getLayers());

//		assertNodesDistance(layers, DummyNodesStretcher.DELTA);
		assertDummyChainsAreVertical(stretched);
	}

	private void assertDummyChainsAreVertical(LayeredPositionedGraph stretched) {
		HashSet<Set<PositionedNode>> dummyChains = new HashSet<Set<PositionedNode>>();
		for (PositionedNode node : stretched.getNodes()) {
			if (node.isDummy()) {
				Set<PositionedNode> sources = node.getSources();
				if (sources.size() == 1 && !sources.iterator().next().isDummy()) {
					HashSet<PositionedNode> dummyChain = new HashSet<PositionedNode>();
					dummyChain.add(node);
					while (node.isDummy()) {
							Set<PositionedNode> localSinks = node.getSinks();
							if (localSinks.size() != 1) {
								throw new IllegalStateException("Multiple sinks for dummy node  " + node);
							}
							node = localSinks.iterator().next();
							if (node.isDummy()) {
								dummyChain.add(node);
							}
					}
					dummyChains.add(dummyChain);
				}
			}
		}

		for (Set<PositionedNode> chain : dummyChains) {
			int x = chain.iterator().next().getX();
			for (PositionedNode dummy : chain) {
				assertEquals(x, dummy.getX());
			}
		}
	}

	private void assertNodesDistance(List<List<PositionedNode>> layers, int delta) {
		for (List<PositionedNode> layer : layers) {
			PositionedNode previous = null;
			PositionedNode next = layer.get(1);
			int layerSize = layer.size();
			for (int i = 0; i < layerSize; i++) {
				PositionedNode node = layer.get(i);
				if (node.isDummy()) {
					if (previous != null) {
						if (previous.isDummy()) {
							assertTrue(node.getX() - previous.getX() >= delta);
						}
					}
					if (next != null) {
						if (next.isDummy()) {
							assertTrue("Distance was " + (next.getX() - node.getX()), next.getX() - node.getX() >= delta);
						}
					}

					previous = node;
					if (i < layerSize-1) {
						next = layer.get(i + 1);
					}
				}
			}
		}
	}

	@Test
	public void testHashCode() {
		PositionedNode n = new PositionedNodeImpl("someId", 1,2);
		HashSet<PositionedNode> set = new HashSet<PositionedNode>();
		set.add(n);
		n.setX(n.getX()+1);
		assertTrue(set.contains(n));
	}
}
