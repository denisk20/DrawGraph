package com.drawgraph;

import com.drawgraph.algorithms.CoffmanGrahamLayeredGraphOrder;
import com.drawgraph.algorithms.GraphUtils;
import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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


	private MockCoffmanGrahamLayeredGraphOrder testable = new MockCoffmanGrahamLayeredGraphOrder(0);
	private static final int MAX_LAYER_LENGTH = 25;
	private static final int MIN_LAYER_LENGTH = 1;

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
		performAndAssertPhase1(GraphMLTestUtils.parseGraph(GraphMLTestUtils.FILE_NAME));
	}

	@Test
	public void phase1_pureSourceSink() throws IOException, SAXException, ParserConfigurationException {
		Graph<SimpleNode> g = GraphMLTestUtils.parseGraph(GraphMLTestUtils.PURE_SOURCE_SINK_FILE_NAME);
		HashMap<SimpleNode, Integer> map = testable.phase1(g);

		Node nodeWithoutSink = testable.getOldestGrapes(g.getNodes(), 1, map).get(0);
		assertTrue(nodeWithoutSink.getSinks().isEmpty());
	}

	@Test
	public void phase1_pureSource() throws IOException, SAXException, ParserConfigurationException {
		performAndAssertPhase1(GraphMLTestUtils.parseGraph(GraphMLTestUtils.PURE_SOURCE_FILE_NAME));
	}

	@Test
	/**
	 * This is the main test
	 */
	public void parseAllDags() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<File> files = GraphMLTestUtils.getFilesInDirectories(GraphMLTestUtils.DAGS_DIRECTORY);
		for (File file : files) {
			Graph<SimpleNode> g = GraphMLTestUtils.parseGraph(file);

			for (int i = MIN_LAYER_LENGTH; i < MAX_LAYER_LENGTH; i++) {
				performCoffmanGrahamLayering(MIN_LAYER_LENGTH, g);
			}
		}
	}

	private void performCoffmanGrahamLayering(int layerLength,
											  Graph<SimpleNode> g) throws IOException, SAXException, ParserConfigurationException {
		HashMap<SimpleNode, Integer> map = performAndAssertPhase1(g);

		performAndAssertPhase2(layerLength, map, g);
	}

	private HashMap<SimpleNode, Integer> performAndAssertPhase1(Graph<SimpleNode> g) throws IOException, SAXException, ParserConfigurationException {
		HashMap<SimpleNode, Integer> map = testable.phase1(g);
		Set<SimpleNode> nodes = map.keySet();
		for (SimpleNode n : nodes) {
			if (n.getSources().isEmpty()) {
				assertEquals(new Integer(1), map.get(n));
			} else {
				assertFalse(new Integer(1).equals(map.get(n)));
			}
		}
		return map;
	}

	private void performAndAssertPhase2(int layerLength,
										HashMap<SimpleNode, Integer> labels,
										Graph<SimpleNode> g) throws IOException, SAXException, ParserConfigurationException {
		final List<List<SimpleNode>> layers = performPhase2(layerLength, labels, g);
		List<SimpleNode> upperLayer = layers.get(layers.size() - 1);
		List<SimpleNode> bottomLayer = layers.get(0);

		assertLayersFitLimit(layers, layerLength);
		assertNoDuplicates(layers);
		assertDirection(layers);
		assertNodesOnSameLayerHasNoPointerToEachOther(layers);

		for (Node n : upperLayer) {
			assertTrue(n.getSources().isEmpty());
		}
		for (Node n : bottomLayer) {
			assertTrue(n.getSinks().isEmpty());
		}
	}

	private void assertNodesOnSameLayerHasNoPointerToEachOther(List<List<SimpleNode>> layers) {
		for (List<SimpleNode> layer : layers) {
			//inside layer
			ArrayList<SimpleNode> allLayerSourcesTargets = new ArrayList<SimpleNode>();
			for (SimpleNode n : layer) {
				//inside node
				allLayerSourcesTargets.addAll(n.getSources());
				allLayerSourcesTargets.addAll(n.getSinks());
			}

			allLayerSourcesTargets.addAll(layer);
			int initialSize = allLayerSourcesTargets.size();
			int actualSize = new HashSet<SimpleNode>(allLayerSourcesTargets).size();

			assertEquals(actualSize, initialSize);
		}
	}

	private void assertDirection(List<List<SimpleNode>> layers) {
		GraphUtils graphUtils = new GraphUtils();
		//starting from bottom to top
		for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
			List<SimpleNode> layer = layers.get(layerIndex);
			for (SimpleNode n : layer) {
				int nodeLayerIndex = graphUtils.getLayerIndexForNode(n, layers);
				for (Node source : n.getSources()) {
					int sourceIndex = graphUtils.getLayerIndexForNode(source, layers);
					assertTrue(nodeLayerIndex < sourceIndex);
				}
			}
		}
	}

	private void assertNoDuplicates(List<List<SimpleNode>> layers) {
		List<SimpleNode> allNodes = new ArrayList<SimpleNode>();
		for (List<SimpleNode> layer : layers) {
			allNodes.addAll(layer);
		}
		int initialSize = allNodes.size();
		int uniqueSize = new HashSet<SimpleNode>(allNodes).size();

		assertEquals(uniqueSize, initialSize);
	}

	private void assertLayersFitLimit(List<List<SimpleNode>> layers, int layerLength) {
		for (List<SimpleNode> layer : layers) {
			assertTrue(layer.size() <= layerLength);
		}
	}

	private List<List<SimpleNode>> performPhase2(int layerLength,
										   HashMap<SimpleNode, Integer> labels,
										   Graph<SimpleNode> g) throws IOException, SAXException, ParserConfigurationException {
		testable.setLayerLength(layerLength);

		final List<List<SimpleNode>> layers = testable.phase2(g, labels);

		return layers;
	}

	private void assertLexicalComparison(Integer[] firstLabels, Integer[] secondLabels, int value) {
		HashMap<SimpleNode, Integer> values1 = getLabelMap("one", firstLabels);
		HashMap<SimpleNode, Integer> values2 = getLabelMap("two", secondLabels);

		HashMap<SimpleNode, Integer> allValues = new HashMap<SimpleNode, Integer>(values1);
		allValues.putAll(values2);

		int result = testable.lexicalComparison(values1.keySet(), values2.keySet(), allValues);
		assertEquals(value, result);
	}

	private HashMap<SimpleNode, Integer> getLabelMap(String prefix, Integer... values) {
		HashMap<SimpleNode, Integer> result = new HashMap<SimpleNode, Integer>();
		for (int i = 0; i < values.length; i++) {
			SimpleNode n = new SimpleNode(prefix + "_id_" + i);
			result.put(n, values[i]);
		}
		return result;
	}

	private class MockCoffmanGrahamLayeredGraphOrder extends CoffmanGrahamLayeredGraphOrder {
		public MockCoffmanGrahamLayeredGraphOrder(int layerLength) {
			super(layerLength);
		}

		@Override
		public <T extends Node<T>> int lexicalComparison(Set<T> first, Set<T> second, HashMap<T, Integer> labels) {
			return super.lexicalComparison(first, second, labels);
		}

		@Override
		public <T extends Node<T>> HashMap<T, Integer> phase1(Graph<T> g) {
			return super.phase1(g);
		}

		@Override
		public <T extends Node<T>> List<List<T>> phase2(Graph<T> g, HashMap<T, Integer> labels) {
			return super.phase2(g, labels);
		}

		@Override
		public <T extends Node<T>> ArrayList<T> getOldestGrapes(Collection<T> grapes, int slotsLength, HashMap<T, Integer> grapesAges) {
			return super.getOldestGrapes(grapes, slotsLength, grapesAges);
		}
	}
}
