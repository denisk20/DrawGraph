package com.drawgraph;

import com.drawgraph.graphics.DrawGraphUI;
import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphMLParser;
import com.drawgraph.parser.callbacks.LineCallback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;

/**
 * Date: Oct 23, 2010
 * Time: 1:17:59 PM
 *
 * @author denisk
 */
public class GraphMLTestUtils {
	public final static String FILE_NAME = "test/test.graphml";
	public final static String PURE_SOURCE_FILE_NAME = "test/test-pureSource.graphml";
	public final static String PURE_SOURCE_SINK_FILE_NAME = "test/test-pureSourcePureSink.graphml";
	public final static String DAG_FILE_NAME = "test/test-DAG.graphml";
	public final static String DIGRAPH_FILE_NAME = "test/test-digraph.graphml";

	public final static String DAGS_DIRECTORY = "data/dags";
	public final static String DIGRAPHS_DIRECTORY = "data/digraphs";

	public final static String GRAPH_NAME = "Test Graph";

	private static GraphMLParser parser = new GraphMLParser();

	private final static String[] NODES = {"n0", "n1", "n2", "n3", "n4"};
	private final static String[][] LINES = {
			{"e0", "n3", "n0"},
			{"e1", "n1", "n2"},
			{"e2", "n3", "n1"},
			{"e3", "n0", "n2"},
			{"e4", "n1", "n0"},
			{"e5", "n2", "n3"},
	};

	public static HashSet<String> NODES_SET = new HashSet<String>(Arrays.asList(NODES));
	public static HashSet<LineCallback.LineSkeleton> LINES_SET = new HashSet<LineCallback.LineSkeleton>();

	static {
		for (String[] line : LINES) {
			LineCallback.LineSkeleton skeleton = new LineCallback.LineSkeleton(line[0], line[1], line[2]);
			LINES_SET.add(skeleton);
		}
	}
	public static HashSet<Node> getSourcesForNode(Node n) {
		String id = n.getId();
		HashSet<Node> result = new HashSet<Node>();
		for (LineCallback.LineSkeleton skeleton : LINES_SET) {
			if (skeleton.getTarget().equals(id)) {
				result.add(new SimpleNode(skeleton.getSource()));
			}
		}

		return result;
	}

	public static HashSet<Node> getSinksForNode(Node n) {
		String id = n.getId();
		HashSet<Node> result = new HashSet<Node>();
		for (LineCallback.LineSkeleton skeleton : LINES_SET) {
			if (skeleton.getSource().equals(id)) {
				result.add(new SimpleNode(skeleton.getTarget()));
			}
		}

		return result;
	}

	public static Graph<SimpleNode> parseGraph() throws IOException, SAXException, ParserConfigurationException {
		return parser.buildGraph(FILE_NAME);
	}
	public static Graph<SimpleNode> parseGraph(String filename) throws IOException, SAXException, ParserConfigurationException {
		return parser.buildGraph(filename);
	}

	public static Graph<SimpleNode> parseGraph(File file) throws IOException, SAXException, ParserConfigurationException {
		return parser.buildGraph(file);
	}

	public static ArrayList<File> getFilesInDirectories(String... relativeDirNames) {
		ArrayList<File> result = new ArrayList<File>();
		for (String dirName : relativeDirNames) {
			File directory = new File(dirName);
			assertTrue(directory.exists());
			assertTrue(directory.isDirectory());

			result.addAll(Arrays.asList(directory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(DrawGraphUI.GRAPHML_EXT);
				}
			})));
		}
		
		return result;
	}

}
