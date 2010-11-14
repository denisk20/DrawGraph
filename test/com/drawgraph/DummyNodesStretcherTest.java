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
import com.drawgraph.model.SimpleNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	public void testStretchCopies() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<File> files = GraphMLTestUtils
				.getFilesInDirectories(GraphMLTestUtils.DAGS_DIRECTORY, GraphMLTestUtils.DIGRAPHS_DIRECTORY);

		for (File file : files) {
			assertStretcher(file);
		}
	}

	@Test
	public void testSimpleFile() throws IOException, SAXException, ParserConfigurationException {
		assertStretcher(new File("/media/Windows_data/work/swisslabs/DrawGraph/data/digraphs/g.10.3.graphml"));
	}
	private void assertStretcher(File file) throws IOException, SAXException, ParserConfigurationException {
		Graph<SimpleNode> graph = GraphMLTestUtils.parseGraph(file);
		LayeredGraph<SimpleNode> layeredGraph = order.getLayeredGraph(graph);
		LayeredGraph<SimpleNode> layeredWithDummies = dummyNodesAssigner.assignDummyNodes(layeredGraph);
		LayeredPositionedGraph layeredPositionedGraph = scaler.scale(layeredWithDummies);
		LayeredPositionedGraphImpl copy = layeredPositionedGraph.copy();

		LayeredPositionedGraph stretched = stretcher.transform(layeredPositionedGraph);
		LayeredPositionedGraph copyStretched = stretcher.transform(copy);

//		assertEquals(stretched.getNodes(), copyStretched.getNodes());
		final String id = "dummy_1";
		PositionedNode origDummy1 = null;
		PositionedNode copyDummy1 = null;
		for (PositionedNode n : stretched.getNodes()) {
			if (n.getId().equals(id)) {
				origDummy1 = n;
			}
			if (!copyStretched.getNodes().contains(n)) {
				System.out.println("Node is not in set: " + n);
			}
		}

		for (PositionedNode n : copy.getNodes()) {
			if (n.getId().equals(id)) {
				copyDummy1 = n;
			}
		}
		System.out.println("Dummy1 VS OrigDummy1: " + origDummy1.equals(copyDummy1));
		ArrayList<PositionedNode> orig = new ArrayList<PositionedNode>(stretched.getNodes());
		ArrayList<PositionedNode> copied = new ArrayList<PositionedNode>(copy.getNodes());
//		assertEquals(orig, copied);
		assertEquals(stretched.getLayers(), copyStretched.getLayers());
	}
}
