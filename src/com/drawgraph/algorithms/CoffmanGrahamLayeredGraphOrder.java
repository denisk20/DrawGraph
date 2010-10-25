package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: Oct 25, 2010
 * Time: 4:11:01 PM
 *
 * @author denisk
 */
public class CoffmanGrahamLayeredGraphOrder implements LayeredGraphOrder<Node> {
	private int layerLength;

	public static final int EQUAL = 0;
	public static final int MORE = 1;
	public static final int LESS = -1;

	protected HashMap<Node, Integer> labels = new HashMap<Node, Integer>();
	public CoffmanGrahamLayeredGraphOrder(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public List<List<Node>> getLayers(Graph<Node> g) {
		HashSet<Node> notSourcesNodes = new HashSet<Node>();
		int label = 1;
		for(Node n:	g.getNodes()) {
			if (n.getSources().isEmpty()) {
				labels.put(n, label);
				label++;
			} else {
				notSourcesNodes.add(n);
			}
		}

		//todo
		return null;
	}

	@Override
	public void setLayerLength(int layerLength) {
		this.layerLength = layerLength;
	}

	protected int lexicalComparison(Set<Node> first, Set<Node> second) {
		final int firstSize = first.size();
		final int secondSize = second.size();

		int result;
		if (firstSize > 0 && secondSize == 0) {
			result = MORE;
		} else if (firstSize == 0 && secondSize > 0) {
			result = LESS;
		} else if (firstSize == 0 && secondSize == 0) {
			result = EQUAL;
		} else {
			Map.Entry<Node, Integer> maxFirstLabel = getLabelWithMaxLabel(labels, first );
			Map.Entry<Node, Integer> maxSecondLabel = getLabelWithMaxLabel(labels, second);
			if (maxFirstLabel.getValue() > maxSecondLabel.getValue()) {
				result = MORE;
			} else if (maxFirstLabel.getValue() < maxSecondLabel.getValue()) {
				result = LESS;
			} else  {
				HashSet<Node> newFirstSet = new HashSet<Node>(first);
				HashSet<Node> newSecondSet = new HashSet<Node>(second);

				newFirstSet.remove(maxFirstLabel.getKey());
				newSecondSet.remove(maxSecondLabel.getKey());
				result = lexicalComparison(newFirstSet, newSecondSet);
			}
		}

		return result;
	}

	private Map.Entry<Node, Integer> getLabelWithMaxLabel(HashMap<Node, Integer> labels, Set<Node> nodes) {
		int label = 0;
		Node n = null;
		for (Node thisNode : nodes) {
			int thisLabel = labels.get(thisNode);
			if (thisLabel > label) {
				label = thisLabel;
				n = thisNode;
			}
		}

		return new AbstractMap.SimpleImmutableEntry<com.drawgraph.model.Node,java.lang.Integer>(n, label);
	}
}
