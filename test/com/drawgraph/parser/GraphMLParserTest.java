package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import com.drawgraph.parser.callbacks.LineCallback;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
/**
 * Date: Oct 22, 2010
 * Time: 11:55:56 AM
 *
 * @author denisk
 */
public class GraphMLParserTest {
	private final static String FILE_NAME = "test.graphml";
	private final static String N_0 = "n0";
	private final static String N_1 = "n1";
	private final static String N_2 = "n2";
	private final static String N_3 = "n3";

	private final static String E_0 = "e0";
	private final static String E_1 = "e1";
	private final static String E_2 = "e2";
	private final static String E_3 = "e3";
	private final static String E_4 = "e4";
	private final static String E_5 = "e5";


	private final static String[] NODES = {"n0", "n1", "n2", "n3", "n4"};
	private final static String[][] LINES = {
			{"e0", "n3", "n0"},
			{"e1", "n1", "n2"},
			{"e2", "n3", "n1"},
			{"e3", "n0", "n1"},
			{"e4", "n1", "n0"},
			{"e5", "n2", "n3"},
	};

	HashSet<String> NODES_SET = new HashSet<String>(Arrays.asList(NODES));
	HashSet<LineCallback.LineSkeleton> LINES_SET = new HashSet<LineCallback.LineSkeleton>();
	private GraphMLParser testable = new GraphMLParser();


	public GraphMLParserTest() {
		for (String[] line : LINES) {
			LineCallback.LineSkeleton skeleton = new LineCallback.LineSkeleton(line[0], line[1], line[2]);
			LINES_SET.add(skeleton);
		}
	}

	@Test
	public void parseGraphMLDocument() throws IOException, SAXException, ParserConfigurationException {
		final Graph graph = testable.buildGraph(FILE_NAME);


		final HashSet<Node> nodes = graph.getNodes();
		for (Node n : nodes) {
			assertTrue(NODES_SET.contains(n.getId()));
		}
	}
}
