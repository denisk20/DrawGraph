package com.drawgraph;
import com.drawgraph.algorithms.CoffmanGrahamLayeredGraphOrder;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Date: Oct 25, 2010
 * Time: 10:43:32 PM
 *
 * @author denisk
 */
public class CoffmanGrahamLayeredGraphOrderTest {

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
		for (int i=0; i < values.length; i++) {
			Node n = new SimpleNode(prefix +"_id_" + i);
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
	}
}
