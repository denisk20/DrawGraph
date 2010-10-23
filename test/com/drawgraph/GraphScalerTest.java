package com.drawgraph;

import com.drawgraph.graphics.GraphScaler;
import com.drawgraph.graphics.GraphScalerImpl;
import com.drawgraph.graphics.SimpleLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.Line;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedGraph;
import com.drawgraph.model.PositionedNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;
/**
 * Date: Oct 23, 2010
 * Time: 9:30:51 PM
 *
 * @author denisk
 */
public class GraphScalerTest {
	private GraphScaler scaler = new GraphScalerImpl();
	private static final int LAYER_LENGTH = 2;

	@Test
	public void scale() throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g =GraphMLTestUtils.parseGraph();

		scaler.setLayerOffset(10);
		scaler.setLeftOffset(30);
		scaler.setTopOffset(40);
		scaler.setMinDistance(5);

		PositionedGraph positionedGraph = scaler.scale(g, new SimpleLayeredGraphOrder(LAYER_LENGTH));
		HashSet<PositionedNode> positionedNodes = positionedGraph.getNodes();
		assertEquals(g.getNodes().size(), positionedNodes.size());
		HashSet<Line> lines = positionedGraph.getLines();
		assertEquals(g.getLines().size(), lines.size());

		for (Line line : lines) {
			assertTrue(positionedNodes.contains(line.getSource()));
			assertTrue(positionedNodes.contains(line.getSink()));
		}
	}
}
