package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredGraphImpl;
import com.drawgraph.model.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
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
public class CoffmanGrahamLayeredGraphOrder implements LayeredGraphOrder<Node> {
	private int layerLength;

	public static final int EQUAL = 0;
	public static final int MORE = 1;
	public static final int LESS = -1;

	protected HashMap<Node, Integer> labels = new HashMap<Node, Integer>();

	protected HashSet<Node> addedNodes = new HashSet<Node>();

	public CoffmanGrahamLayeredGraphOrder(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public LayeredGraph<Node> getLayeredGraph(Graph<? extends Node> g) {
		addedNodes.clear();
		labels = phase1(g);

		List<List<Node>> layers = phase2(g);

		LayeredGraphImpl result = new LayeredGraphImpl(g.getId(), layers);
		result.getLines().addAll(g.getLines());
		result.getNodes().addAll(g.getNodes());
		return result;
	}

	/**
	 * This method returns a map of nodes which all nodes have labels
	 * assigned
	 *
	 * @param g graph to analyze
	 * @return map of signature <Node, Label>
	 */
	protected HashMap<Node, Integer> phase1(Graph<? extends Node> g) {
		HashMap<Node, Integer> resultingLabels = new HashMap<Node, Integer>();
		HashSet<Node> notSourcesNodes = new HashSet<Node>();
		int label = 1;
		for (Node n : g.getNodes()) {
			if (n.getSources().isEmpty()) {
				resultingLabels.put(n, label);
				//label++;
			} else {
				notSourcesNodes.add(n);
			}
		}

		int uncheckedNodesCount = notSourcesNodes.size();

		for (int i = 0; i < uncheckedNodesCount; i++) {
			Node<Node> n = getNodeWithMinLexMarkedSources(resultingLabels, notSourcesNodes);
			if (n == null) {
				//just get node with ANY sources (marked/unmarked)
				n = getNodeWithMinimalLexSources(notSourcesNodes);
			}
			resultingLabels.put(n, ++label);
			notSourcesNodes.remove(n);
		}

		return resultingLabels;
	}

	protected List<List<Node>> phase2(Graph<? extends Node> g) {
		if (labels.isEmpty()) {
			throw new IllegalArgumentException("Labels are empty");
		}
		HashSet<Node> nodesWithoutSinks = getNodesWithoutSinks(g.getNodes());

		HashSet<Node> allNodes = new HashSet<Node>(g.getNodes());
		allNodes.removeAll(nodesWithoutSinks);
		List<List<Node>> result = putGrapesIntoSlots(allNodes, nodesWithoutSinks, null, layerLength, labels);
		return result;
	}

	private HashSet<Node> getNodesWithoutSinks(Collection<? extends Node> nodes) {
		HashSet<Node> nodesWithoutSinks = new HashSet<Node>();
		for (Node n : nodes) {
			if (n.getSinks().isEmpty()) {
				nodesWithoutSinks.add(n);
			}
		}
		return nodesWithoutSinks;
	}

	protected List<List<Node>> putGrapesIntoSlots(Collection<Node> allRemainingGrapes, Collection<Node> grapes, List<List<Node>> boxOfGrapes, final int slotsLength, HashMap<Node, Integer> grapesAges) {
		//this is our treasure - our box of grapes...
		if (boxOfGrapes == null) {
			boxOfGrapes = new ArrayList<List<Node>>();
		}
		//creating new slot - cosy place for our grapes
		ArrayList<Node> slot = new ArrayList<Node>(slotsLength);

		List<Node> whiteGrapes = new ArrayList<Node>();
		HashSet<Node> hangingNodes = getHangingNodes(grapes);
		if (hangingNodes.size() > 0) {
			//put only hanging grapes into the layer
			HashSet<Node> remainingGrapes = new HashSet<Node>(grapes);
			remainingGrapes.removeAll(hangingNodes);

			//just put the rest in white grapes. Goes the first
			whiteGrapes.addAll(remainingGrapes);

			//add white grapes for our hanging nodes as well...
			grapes.removeAll(remainingGrapes);
			//todo do I need these lines?
			whiteGrapes.removeAll(addedNodes);
			whiteGrapes = new ArrayList<Node>(new HashSet<Node>(whiteGrapes));
		}
		if (grapes.size() > slotsLength) {
			//ohhh, we've got so many grapes! Can't fit at a time!
			ArrayList<Node> oldestGrapes = getOldestGrapes(grapes, slotsLength, grapesAges);
			HashSet<Node> youngestGrapes = new HashSet<Node>(grapes);
			youngestGrapes.removeAll(oldestGrapes);

			grapes.removeAll(youngestGrapes);
			//just put the rest (the youngest ones) in the beginning of whiteGrapes!
			//white grapes for grapes that are in the slot. Extras go first!
			whiteGrapes = new ArrayList<Node>(youngestGrapes);
			ArrayList<Node> allWhiteGrapes = getWhiteGrapes(grapes);
			whiteGrapes.addAll(allWhiteGrapes);
			whiteGrapes.removeAll(addedNodes);
			//remove duplicates!
			whiteGrapes = new ArrayList<Node>(new HashSet<Node>(whiteGrapes));
		} else {
			//our slot will take all grapes! Coool!
			grapes = getOldestGrapes(grapes, slotsLength, grapesAges);

			
			int extraSlots = slotsLength - grapes.size();
			ArrayList<Node> allWhiteGrapes = getWhiteGrapes(grapes);
			whiteGrapes.addAll(allWhiteGrapes);
			whiteGrapes.removeAll(addedNodes);
			//this is where we'll look for extra grapes - in remaining grapes \ white grapes
			HashSet<Node> grapesToLookForExtra = new HashSet<Node>(allRemainingGrapes);

			//this is useful when we parse the last layer
			grapes.removeAll(whiteGrapes);
			grapesToLookForExtra.removeAll(whiteGrapes);
			grapesToLookForExtra.removeAll(grapes);
			if (hangingNodes.size() == 0) {
				//hey, let's put extra grapes to fill the slot!
				ArrayList<Node> grapesToFillTheSlot = getOldestGrapes(grapesToLookForExtra, extraSlots, grapesAges);
				//OK, let's put them as well!
				for (Node grapeToFillTheSlot : grapesToFillTheSlot) {
					grapes.add(grapeToFillTheSlot);
				}

				//get white grapes for extra grapes
				ArrayList<Node> whiteGrapesForExtras = getWhiteGrapes(grapesToFillTheSlot);
				//make sure we don't look at previously added grapes
				whiteGrapes.addAll(whiteGrapesForExtras);
				whiteGrapes.removeAll(addedNodes);
				whiteGrapes = new ArrayList<Node>(new HashSet<Node>(whiteGrapes));
			}
		}
		for (Node grape : grapes) {
			slot.add(grape);
		}

		whiteGrapes.removeAll(grapes);
		addedNodes.addAll(grapes);

		//remove grapes from the sack they were in...
		allRemainingGrapes.removeAll(slot);
		//remove white grapes as well - we'd put them into a special container...
		allRemainingGrapes.removeAll(whiteGrapes);

		boxOfGrapes.add(slot);
		//OK, our slot is filled with grapes! Or not? - if so, then there are no more grapes and we shall finish and enjoy grapes!
		if (whiteGrapes.size() == 0) {
			if (allRemainingGrapes.size() > 0) {
				throw new UnexpectedCycledGraphException("this is very special situation, we seem to have another sack of grapes (another graph...)");
			}
			return boxOfGrapes;
		} else {
			return putGrapesIntoSlots(allRemainingGrapes, whiteGrapes, boxOfGrapes, slotsLength, grapesAges);
		}

	}

	private HashSet<Node> getHangingNodes(Collection<Node> grapes) {
		HashSet<Node> result = new HashSet<Node>();
		for (Node n : grapes) {
			if (n.getSinks().size() == 0) {
				result.add(n);
			}
		}

		return result;
	}

//	protected List<List<Node>> testPhase2(Set<Node> nodes) {
//		HashSet<Node> remainingNodes = new HashSet<Node>(nodes);
//		HashSet<Node> checkedNodes = new HashSet<Node>();
//		List<List<Node>> layers = new ArrayList<List<Node>>();
//		Node mainSink = getNodeWithMaxLabel(labels);
//		checkedNodes.add(mainSink);
//		List<Node> bottomLayer
//		while (checkedNodes.size() <= nodes.size()) {
//			//Node node =
//		}
//	}
	/**
	 * This method returns MAXIMUM slotsLength oldest grapes (which has biggest label)
	 * from grapes
	 */
	protected ArrayList<Node> getOldestGrapes(Collection<Node> grapes, int slotsLength, HashMap<Node, Integer> grapesAges) {
		HashMap<Node, Integer> ages = new HashMap<Node, Integer>(grapesAges);
		Set<Node> interestedGrapes = ages.keySet();
		interestedGrapes.retainAll(grapes);

		ArrayList<Node> result = new ArrayList<Node>();
		int checkedNodesCount = 0;
		while (checkedNodesCount < slotsLength) {
			Node n = getNodeWithMaxLabel(ages);
			if (n != null) {
				if (n.getSinks().size()==0  || addedNodes.containsAll(n.getSinks())) {
					result.add(n);
				}
				checkedNodesCount++;
			} else {
				break;
			}
			ages.remove(n);
		}

		return result;
	}

	private ArrayList<Node> getWhiteGrapes(Collection<Node> grapes) {
		HashSet<Node> whiteGrapes = new HashSet<Node>();

		for (Node grape : grapes) {
			whiteGrapes.addAll(grape.getSources());
		}

		return new ArrayList<Node>(whiteGrapes);
	}

	private Node getNodeWithMaxLabel(HashMap<Node, Integer> nodesToCheck) {
		Map.Entry<Node, Integer> result =
				new AbstractMap.SimpleImmutableEntry<Node, Integer>(null, 0);

		for (Map.Entry<Node, Integer> entry : nodesToCheck.entrySet()) {
			if (entry.getValue() > result.getValue()) {
				result = entry;
			}
		}

		return result.getKey();
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
			Map.Entry<Node, Integer> maxFirstLabel = getLabelWithMaxValue(labels, first);
			Map.Entry<Node, Integer> maxSecondLabel = getLabelWithMaxValue(labels, second);
			if (maxFirstLabel.getValue() > maxSecondLabel.getValue()) {
				result = MORE;
			} else if (maxFirstLabel.getValue() < maxSecondLabel.getValue()) {
				result = LESS;
			} else {
				HashSet<Node> newFirstSet = new HashSet<Node>(first);
				HashSet<Node> newSecondSet = new HashSet<Node>(second);

				newFirstSet.remove(maxFirstLabel.getKey());
				newSecondSet.remove(maxSecondLabel.getKey());
				result = lexicalComparison(newFirstSet, newSecondSet);
			}
		}

		return result;
	}

	private Map.Entry<Node, Integer> getLabelWithMaxValue(HashMap<Node, Integer> labels, Set<Node> nodes) {
		int label = 0;
		//take the very first node
		Node<Node> n = nodes.iterator().next();
		for (Node<Node> thisNode : nodes) {
			int thisLabel = 0;
			if (labels.containsKey(thisNode)) {
				thisLabel = labels.get(thisNode);
			}
			if (thisLabel > label) {
				label = thisLabel;
				n = thisNode;
			}
		}

		return new AbstractMap.SimpleImmutableEntry<Node, java.lang.Integer>(n, label);
	}

	private Node<Node> getNodeWithMinLexMarkedSources(HashMap<Node, Integer> labels, Set<Node> nodes) {
		HashSet<Node> nodesWithMarkedSources = new HashSet<Node>();

		for (Node<Node> thisNode : nodes) {
			boolean sourcesLabeled = true;
			Set<Node> sources = thisNode.getSources();
			for (Node source : sources) {
				if (!labels.containsKey(source)) {
					sourcesLabeled = false;
					break;
				}
			}
			if (sourcesLabeled) {
				nodesWithMarkedSources.add(thisNode);
			}
		}
		Node<Node> result = null;
		if (!nodesWithMarkedSources.isEmpty()) {
			result = getNodeWithMinimalLexSources(nodesWithMarkedSources);
		}

		return result;
	}

	private Node<Node> getNodeWithMinimalLexSources(HashSet<Node> nodesWithUnmarkesSources) {
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
