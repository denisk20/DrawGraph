package com.drawgraph;

import com.drawgraph.algorithms.CoffmanGrahamLayeredGraphOrder;
import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: Oct 25, 2010
 * Time: 10:43:32 PM
 *
 * @author denisk
 */
public class CoffmanGrahamLayeredGraphOrderTest {

	private final static String DAGS_DIRECTORY = "data/dags";

	private MockCoffmanGrahamLayeredGraphOrder testable = new MockCoffmanGrahamLayeredGraphOrder(0);

	@Test
	public void testLexicalComparison_less() {
		Integer[] firstLabels = new Integer[]{1, 2, 4};
		Integer[] secondLabels = new Integer[]{1, 2, 5};
		int value = CoffmanGrahamLayeredGraphOrder.LESS;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_less1() {
		Integer[] firstLabels = new Integer[]{1, 2, 4};
		Integer[] secondLabels = new Integer[]{1, 3, 4};
		int value = CoffmanGrahamLayeredGraphOrder.LESS;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_less2() {
		Integer[] firstLabels = new Integer[]{1, 4};
		Integer[] secondLabels = new Integer[]{1, 3, 4};
		int value = CoffmanGrahamLayeredGraphOrder.LESS;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_less3() {
		Integer[] firstLabels = new Integer[]{1, 6};
		Integer[] secondLabels = new Integer[]{1, 2, 5, 4, 4, 5, 5, 6};
		int value = CoffmanGrahamLayeredGraphOrder.LESS;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_more() {
		Integer[] firstLabels = new Integer[]{1, 3, 5};
		Integer[] secondLabels = new Integer[]{1, 2, 5};
		int value = CoffmanGrahamLayeredGraphOrder.MORE;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_more3() {
		Integer[] firstLabels = new Integer[]{1, 3, 6};
		Integer[] secondLabels = new Integer[]{1, 3, 5};
		int value = CoffmanGrahamLayeredGraphOrder.MORE;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_more1() {
		Integer[] firstLabels = new Integer[]{1, 2, 5, 1};
		Integer[] secondLabels = new Integer[]{1, 2, 5};
		int value = CoffmanGrahamLayeredGraphOrder.MORE;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_more2() {
		Integer[] firstLabels = new Integer[]{1, 6};
		Integer[] secondLabels = new Integer[]{1, 2, 5, 4, 4, 5, 5};
		int value = CoffmanGrahamLayeredGraphOrder.MORE;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_equal() {
		Integer[] firstLabels = new Integer[]{};
		Integer[] secondLabels = new Integer[]{};
		int value = CoffmanGrahamLayeredGraphOrder.EQUAL;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void testLexicalComparison_equal2() {
		Integer[] firstLabels = new Integer[]{1, 3, 4, 6};
		Integer[] secondLabels = new Integer[]{1, 3, 4, 6};
		int value = CoffmanGrahamLayeredGraphOrder.EQUAL;
		assertLexicalComparison(firstLabels, secondLabels, value);
	}

	@Test
	public void phase1() throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g = GraphMLTestUtils.parseGraph();
		HashMap<Node, Integer> map = testable.phase1(g);
		Set<Node> nodes = map.keySet();
		for (Node n : nodes) {
			if (n.getSources().isEmpty()) {
				assertEquals(new Integer(1), map.get(n));
			} else {
				assertFalse(new Integer(1).equals(map.get(n)));
			}
		}
	}

	@Test
	public void phase1_pureSourceSink() throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g = GraphMLTestUtils.parseGraph(GraphMLTestUtils.PURE_SOURCE_SINK_FILE_NAME);
		HashMap<Node, Integer> map = testable.phase1(g);

		Node nodeWithoutSink = testable.getOldestGrapes(g.getNodes(), 1, map).get(0);
		assertTrue(nodeWithoutSink.getSinks().isEmpty());
	}

	@Test
	public void phase1_pureSource() throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g = GraphMLTestUtils.parseGraph(GraphMLTestUtils.PURE_SOURCE_FILE_NAME);
		HashMap<Node, Integer> map = testable.phase1(g);
		Set<Node> nodes = map.keySet();
		for (Node n : nodes) {
			if (n.getSources().isEmpty()) {
				assertEquals(new Integer(1), map.get(n));
			} else {
				assertFalse(new Integer(1).equals(map.get(n)));
			}
		}
	}

	@Test
	public void phase2_pureSource_pureSink() throws IOException, SAXException, ParserConfigurationException {
		applyCoffmanGrahamToDagAndAssert(GraphMLTestUtils.DAG_FILE_NAME);
	}

	private void applyCoffmanGrahamToDagAndAssert(String dagFileName) throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g = GraphMLTestUtils.parseGraph(dagFileName);
		final int layerLength = 2;
		final List<List<Node>> layers = performPhase2(dagFileName, layerLength);
		List<Node> upperLayer = layers.get(layers.size()-1);
		List<Node> bottomLayer = layers.get(0);

		assertLayersFitLimit(layers, layerLength);
		assertNoDuplicates(layers);
		assertDirection(layers);

		for (Node n : upperLayer) {
			assertTrue(n.getSources().isEmpty());
		}
		for (Node n : bottomLayer) {
			assertTrue(n.getSinks().isEmpty());
		}
	}

	private void assertDirection(List<List<Node>> layers) {
		//starting from bottom to top
		for (int layerIndex = 0; layerIndex< layers.size(); layerIndex++) {
			List<Node> layer = layers.get(layerIndex);
			for (Node<Node> n : layer) {
				int nodeLayerIndex = getIndexForNode(n, layers);
				for (Node source : n.getSources()) {
					int sourceIndex = getIndexForNode(source, layers);
					assertTrue(nodeLayerIndex < sourceIndex);

				}
			}
		}

	}

	private void assertIndexIsHigher(int nodeLayerIndex, Node source) {
	}

	private int getIndexForNode(Node<Node> inputNode, List<List<Node>> layers) {
		for (int layerIndex = 0; layerIndex< layers.size(); layerIndex++) {
			List<Node> layer = layers.get(layerIndex);
			for (Node<Node> n : layer) {
				if (n.equals(inputNode)) {
					return layerIndex;
				}
			}
		}
		throw new IllegalArgumentException("No node in layers: " + inputNode);
	}

	private void assertNoDuplicates(List<List<Node>> layers) {
		List<Node> allNodes = new ArrayList<Node>();
		for (List<Node> layer : layers) {
			allNodes.addAll(layer);
		}
		int initialSize = allNodes.size();
		int uniqueSize = new HashSet<Node>(allNodes).size();

		assertEquals(initialSize, uniqueSize);
	}

	private void assertLayersFitLimit(List<List<Node>> layers, int layerLength) {
		for (List<Node> layer : layers) {
			assertTrue(layer.size() <= layerLength);
		}
	}

	@Test
	public void dummy() {
		ArrayList list = new ArrayList();
		list.add(3);
		list.add(1);
		list.add(5);

		Collections.sort(list);
		int i = 0;
	}
	
	private List<List<Node>> performPhase2(String pureSourceFileName, int layerLength) throws IOException, SAXException, ParserConfigurationException {
		Graph<Node> g = GraphMLTestUtils.parseGraph(pureSourceFileName);
		HashSet<Node> nodes = g.getNodes();
		testable.setLayerLength(layerLength);

		HashMap<Node, Integer> labels = testable.phase1(g);
		testable.setLabels(labels);
		final List<List<Node>> layers = testable.phase2_2(g);

		return layers;
	}

	private void assertLexicalComparison(Integer[] firstLabels, Integer[] secondLabels, int value) {
		HashMap<Node, Integer> values1 = getLabelMap("one", firstLabels);
		HashMap<Node, Integer> values2 = getLabelMap("two", secondLabels);

		testable.getLabels().putAll(values1);
		testable.getLabels().putAll(values2);
		int result = testable.lexicalComparison(values1.keySet(), values2.keySet());
		assertEquals(value, result);
	}

	private HashMap<Node, Integer> getLabelMap(String prefix, Integer... values) {
		HashMap<Node, Integer> result = new HashMap<Node, Integer>();
		for (int i = 0; i < values.length; i++) {
			Node n = new SimpleNode(prefix + "_id_" + i);
			result.put(n, values[i]);
		}
		return result;
	}

	private class MockCoffmanGrahamLayeredGraphOrder extends CoffmanGrahamLayeredGraphOrder {
		public MockCoffmanGrahamLayeredGraphOrder(int layerLength) {
			super(layerLength);
		}

		public HashMap<Node, Integer> getLabels() {
			return labels;
		}

		@Override
		public int lexicalComparison(Set<Node> first, Set<Node> second) {
			return super.lexicalComparison(first, second);
		}

		@Override
		public HashMap<Node, Integer> phase1(Graph<Node> g) {
			return super.phase1(g);
		}

		@Override
		protected List<List<Node>> phase2_2(Graph<Node> g) {
			return super.phase2_2(g);
		}

		@Override
		protected ArrayList<Node> getOldestGrapes(Collection<Node> grapes, int slotsLength, HashMap<Node, Integer> grapesAges) {
			return super.getOldestGrapes(grapes, slotsLength, grapesAges);
		}

		public void setLabels(HashMap<Node, Integer> labels) {
			this.labels = labels;
		}
	}
}
