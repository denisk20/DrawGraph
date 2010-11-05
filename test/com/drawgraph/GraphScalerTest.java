package com.drawgraph;

import com.drawgraph.algorithms.DummyNodesAssigner;
import com.drawgraph.algorithms.SimpleDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.graphics.GraphScaler;
import com.drawgraph.graphics.GraphScalerImpl;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Line;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Date: Oct 23, 2010
 * Time: 9:30:51 PM
 *
 * @author denisk
 */
public class GraphScalerTest {
	private GraphScaler scaler = new GraphScalerImpl();
	private static final int LAYER_LENGTH = 2;
	private static final int DIST = 25;
	private static final int TOP_OFF = 40;
	private static final int LEFT_OFF = 30;
	private static final int LAYER_OFFSET = 10;
	private static final int LAYER_SHIFT= 10;
	private static final int RADIUS = 5;

	@Test
	public void scale() throws IOException, SAXException, ParserConfigurationException {
		Graph<SimpleNode> g =GraphMLTestUtils.parseGraph(GraphMLTestUtils.DIGRAPH_FILE_NAME);

		DummyNodesAssigner assigner = new SimpleDummyNodesAssigner() ;
		scaler.setLayerOffset(LAYER_OFFSET);
		scaler.setLeftOffset(LEFT_OFF);
		scaler.setTopOffset(TOP_OFF);
		scaler.setMinDistance(DIST);
		scaler.setShift(LAYER_SHIFT);

		SimpleLayeredGraphOrder layeredGraphOrder = new SimpleLayeredGraphOrder(LAYER_LENGTH);
		LayeredGraph<SimpleNode> layeredGraph = layeredGraphOrder.getLayeredGraph(g);
		LayeredGraph<SimpleNode> layeredWithDummies = assigner.assignDummyNodes(layeredGraph);
		LayeredPositionedGraph positionedGraph = scaler.scale(layeredWithDummies);
		positionedGraph.setRadius(RADIUS);

		HashSet<PositionedNode> positionedNodes = positionedGraph.getNodes();
		HashSet<Line> lines = positionedGraph.getLines();

		HashSet<SimpleNode> initialNodes = layeredWithDummies.getNodes();
		int nodesCount = initialNodes.size();
		assertEquals(nodesCount, positionedNodes.size());
		for (PositionedNode n : positionedNodes) {
			Set<PositionedNode> sources = n.getSources();
			Set<PositionedNode> sinks = n.getSinks();
			assertTrue(sources.size() > 0 || sinks.size() > 0);
			assertTrue(initialNodes.containsAll(sources));
			assertTrue(initialNodes.containsAll(sinks));
		}
		assertEquals(layeredWithDummies.getLines().size(), lines.size());

		for (Line line : lines) {
			assertTrue(positionedNodes.contains(line.getSource()));
			assertTrue(positionedNodes.contains(line.getSink()));
		}

//		int expectedWidth = scaler.getMinDistance() * (layeredGraphOrder.getLayerLength() - 1) + scaler
//				.getLeftOffset() + positionedGraph.getRadius() / 2;
//		assertEquals(expectedWidth, positionedGraph.getWidth());
//
//		int expectedHeight = scaler.getTopOffset() + scaler.getLayerOffset() * (layeredGraphOrder
//				.getLayersCount() - 1) + positionedGraph.getRadius() / 2;
//		assertEquals(expectedHeight, positionedGraph.getHeight());
//
//		List<List<PositionedNode>> layers = positionedGraph.getLayers();
//		int expectedLayersCount = (int)(nodesCount / (double)LAYER_LENGTH);
//		assertEquals(expectedLayersCount, layers.size());
//
//		for (List<PositionedNode> layer : layers) {
//			assertEquals(LAYER_LENGTH, layer.size());
//		}
	}
}
