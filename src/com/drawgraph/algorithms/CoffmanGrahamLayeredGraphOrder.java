package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

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

	private static final int EQUAL = 0;
	private static final int MORE = 1;
	private static final int LESS = -1;

	HashMap<Node, Integer> labels = new HashMap<Node, Integer>();
	public CoffmanGrahamLayeredGraphOrder(int value) {
		layerLength = value;
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

	private int lexicalComparison(Set<Node> first, Set<Node> second) {
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
			Map.Entry<Node, Integer> maxFirstLabel = getLabelWithMaxLabel(first);
			Map.Entry<Node, Integer> maxSecondLabel = getLabelWithMaxLabel(second);
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

	private Map.Entry<Node, Integer> getLabelWithMaxLabel(Set<Node> nodes) {
		//todo
		return null;
	}
}
