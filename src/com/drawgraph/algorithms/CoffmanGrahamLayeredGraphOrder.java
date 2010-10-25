package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class CoffmanGrahamLayeredGraphOrder implements LayeredGraphOrder<Node<Node>> {
	private int layerLength;

	public static final int EQUAL = 0;
	public static final int MORE = 1;
	public static final int LESS = -1;

	protected HashMap<Node<Node>, Integer> labels = new HashMap<Node<Node>, Integer>();
	public CoffmanGrahamLayeredGraphOrder(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public List<List<Node<Node>>> getLayers(Graph<Node<Node>> g) {
		HashSet<Node<Node>> notSourcesNodes = new HashSet<Node<Node>>();
		int label = 1;
		for(Node<Node> n:	g.getNodes()) {
			if (n.getSources().isEmpty()) {
				labels.put(n, label);
				label++;
			} else {
				notSourcesNodes.add(n);
			}
		}

		int uncheckedNodesCount = notSourcesNodes.size();

		for (int i = 0; i < uncheckedNodesCount; i++) {
			Node n = getNodeWithMinLexMarkedSources(labels, notSourcesNodes);
			labels.put(n, label);
			label++;
			notSourcesNodes.remove(n);
		}

		
		return null;
	}

	@Override
	public void setLayerLength(int layerLength) {
		this.layerLength = layerLength;
	}

	protected int lexicalComparison(Set<Node<Node>> first, Set<Node<Node>> second) {
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
			Map.Entry<Node<Node>, Integer> maxFirstLabel = getLabelWithMaxValue(labels, first );
			Map.Entry<Node<Node>, Integer> maxSecondLabel = getLabelWithMaxValue(labels, second);
			if (maxFirstLabel.getValue() > maxSecondLabel.getValue()) {
				result = MORE;
			} else if (maxFirstLabel.getValue() < maxSecondLabel.getValue()) {
				result = LESS;
			} else  {
				HashSet<Node<Node>> newFirstSet = new HashSet<Node<Node>>(first);
				HashSet<Node<Node>> newSecondSet = new HashSet<Node<Node>>(second);

				newFirstSet.remove(maxFirstLabel.getKey());
				newSecondSet.remove(maxSecondLabel.getKey());
				result = lexicalComparison(newFirstSet, newSecondSet);
			}
		}

		return result;
	}

	private Map.Entry<Node<Node>, Integer> getLabelWithMaxValue(HashMap<Node<Node>, Integer> labels, Set<Node<Node>> nodes) {
		int label = 0;
		Node<Node> n = null;
		for (Node<Node> thisNode : nodes) {
			int thisLabel = labels.get(thisNode);
			if (thisLabel > label) {
				label = thisLabel;
				n = thisNode;
			}
		}

		return new AbstractMap.SimpleImmutableEntry<com.drawgraph.model.Node<Node>,java.lang.Integer>(n, label);
	}

	private Node<Node> getNodeWithMinLexMarkedSources(HashMap<Node<Node>, Integer> labels, Set<Node<Node>> nodes) {
		HashSet<Node<Node>> nodesWithMarkedSources = new HashSet<Node<Node>>();

		for (Node<Node> thisNode : nodes) {
			boolean sourcesLabeled = true;
			Set<Node> sources = thisNode.getSources();
			for (Node source : sources) {
				if (! labels.containsKey(source)) {
					sourcesLabeled = false;
					break;
				}
			}
			if (sourcesLabeled) {
				nodesWithMarkedSources.add(thisNode);
			}
		}
		Node<Node> result = getNodeWithMinimalLexSources(nodesWithMarkedSources);

		return result;
	}

	private Node<Node> getNodeWithMinimalLexSources(HashSet<Node<Node>> nodesWithUnmarkesSources) {
		ArrayList<Node> list = new ArrayList<Node>(nodesWithUnmarkesSources);
		Node<Node> result = Collections.max(list, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return lexicalComparison(o1.getSources(), o2.getSources());
			}
		});

		return result;
	}
}
