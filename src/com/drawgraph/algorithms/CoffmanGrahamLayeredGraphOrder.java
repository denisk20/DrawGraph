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
public class CoffmanGrahamLayeredGraphOrder implements LayeredGraphOrder {
	private int layerLength;

	public static final int EQUAL = 0;
	public static final int MORE = 1;
	public static final int LESS = -1;

	protected HashSet<Node> addedNodes = new HashSet<Node>();

	public CoffmanGrahamLayeredGraphOrder(int layerLength) {
		this.layerLength = layerLength;
	}

	@Override
	public int getLayerLength() {
		return layerLength;
	}

	@Override
	public <T extends Node<T>>LayeredGraph<T> getLayeredGraph(Graph<T> g) {
		HashMap<T, Integer> labels = phase1(g);

		List<List<T>> layers = phase2(g, labels);

		LayeredGraphImpl<T> result = new LayeredGraphImpl<T>(g.getId(), layers);
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
	protected <T extends Node<T>> HashMap<T, Integer> phase1(Graph<T> g) {
		HashMap<T, Integer> resultingLabels = new HashMap<T, Integer>();
		HashSet<T> notSourcesNodes = new HashSet<T>();
		int label = 1;
		for (T n : g.getNodes()) {
			if (n.getSources().isEmpty()) {
				resultingLabels.put(n, label);
				//label++;
			} else {
				notSourcesNodes.add(n);
			}
		}

		int uncheckedNodesCount = notSourcesNodes.size();

		for (int i = 0; i < uncheckedNodesCount; i++) {
			T n = getNodeWithMinLexMarkedSources(resultingLabels, notSourcesNodes);
			if (n == null) {
				//just get node with ANY sources (marked/unmarked)
				n = getNodeWithMinimalLexSources(notSourcesNodes, resultingLabels);
			}
			resultingLabels.put(n, ++label);
			notSourcesNodes.remove(n);
		}

		return resultingLabels;
	}

	protected <T extends Node<T>> List<List<T>> phase2(Graph<T> g, HashMap<T, Integer> labels) {
		addedNodes.clear();
		if (labels.isEmpty()) {
			throw new IllegalArgumentException("Labels are empty");
		}
		HashSet<T> nodesWithoutSinks = getNodesWithoutSinks(g.getNodes());

		HashSet<T> allNodes = new HashSet<T>(g.getNodes());
		allNodes.removeAll(nodesWithoutSinks);
		List<List<T>> result = putGrapesIntoSlots(allNodes, nodesWithoutSinks, null, layerLength, labels);
		return result;
	}

	private <T extends Node<T>> HashSet<T> getNodesWithoutSinks(Collection<T> nodes) {
		HashSet<T> nodesWithoutSinks = new HashSet<T>();
		for (T n : nodes) {
			if (n.getSinks().isEmpty()) {
				nodesWithoutSinks.add(n);
			}
		}
		return nodesWithoutSinks;
	}

	protected <T extends Node<T>> List<List<T>> putGrapesIntoSlots(Collection<T> allRemainingGrapes, Collection<T> grapes, List<List<T>> boxOfGrapes, final int slotsLength, HashMap<T, Integer> grapesAges) {
		//this is our treasure - our box of grapes...
		if (boxOfGrapes == null) {
			boxOfGrapes = new ArrayList<List<T>>();
		}
		//creating new slot - cosy place for our grapes
		ArrayList<T> slot = new ArrayList<T>(slotsLength);

		List<T> whiteGrapes = new ArrayList<T>();
		HashSet<T> hangingNodes = getHangingNodes(grapes);
		if (hangingNodes.size() > 0) {
			//put only hanging grapes into the layer
			HashSet<T> remainingGrapes = new HashSet<T>(grapes);
			remainingGrapes.removeAll(hangingNodes);

			//just put the rest in white grapes. Goes the first
			whiteGrapes.addAll(remainingGrapes);

			//add white grapes for our hanging nodes as well...
			grapes.removeAll(remainingGrapes);
			//todo do I need these lines?
			whiteGrapes.removeAll(addedNodes);
			whiteGrapes = new ArrayList<T>(new HashSet<T>(whiteGrapes));
		}
		if (grapes.size() > slotsLength) {
			//ohhh, we've got so many grapes! Can't fit at a time!
			ArrayList<T> oldestGrapes = getOldestGrapes(grapes, slotsLength, grapesAges);
			HashSet<T> youngestGrapes = new HashSet<T>(grapes);
			youngestGrapes.removeAll(oldestGrapes);

			grapes.removeAll(youngestGrapes);
			//just put the rest (the youngest ones) in the beginning of whiteGrapes!
			//white grapes for grapes that are in the slot. Extras go first!
			whiteGrapes.addAll(youngestGrapes);
			ArrayList<T> allWhiteGrapes = getWhiteGrapes(grapes);
			whiteGrapes.addAll(allWhiteGrapes);
			whiteGrapes.removeAll(addedNodes);
			//remove duplicates!
			whiteGrapes = new ArrayList<T>(new HashSet<T>(whiteGrapes));
		} else {
			//our slot will take all grapes! Coool!
			grapes = getOldestGrapes(grapes, slotsLength, grapesAges);

			
			int extraSlots = slotsLength - grapes.size();
			ArrayList<T> allWhiteGrapes = getWhiteGrapes(grapes);
			whiteGrapes.addAll(allWhiteGrapes);
			whiteGrapes.removeAll(addedNodes);
			//this is where we'll look for extra grapes - in remaining grapes \ white grapes
			HashSet<T> grapesToLookForExtra = new HashSet<T>(allRemainingGrapes);

			//this is useful when we parse the last layer
			grapes.removeAll(whiteGrapes);
			grapesToLookForExtra.removeAll(whiteGrapes);
			grapesToLookForExtra.removeAll(grapes);
			if (hangingNodes.size() == 0) {
				//hey, let's put extra grapes to fill the slot!
				ArrayList<T> grapesToFillTheSlot = getOldestGrapes(grapesToLookForExtra, extraSlots, grapesAges);
				//OK, let's put them as well!
				for (T grapeToFillTheSlot : grapesToFillTheSlot) {
					grapes.add(grapeToFillTheSlot);
				}

				//get white grapes for extra grapes
				ArrayList<T> whiteGrapesForExtras = getWhiteGrapes(grapesToFillTheSlot);
				//make sure we don't look at previously added grapes
				whiteGrapes.addAll(whiteGrapesForExtras);
				whiteGrapes.removeAll(addedNodes);
				whiteGrapes = new ArrayList<T>(new HashSet<T>(whiteGrapes));
			}
		}
		for (T grape : grapes) {
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

	private <T extends Node<T>> HashSet<T> getHangingNodes(Collection<T> grapes) {
		HashSet<T> result = new HashSet<T>();
		for (T n : grapes) {
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
	protected <T extends Node<T>> ArrayList<T> getOldestGrapes(Collection<T> grapes, int slotsLength, HashMap<T, Integer> grapesAges) {
		HashMap<T, Integer> ages = new HashMap<T, Integer>(grapesAges);
		Set<T> interestedGrapes = ages.keySet();
		interestedGrapes.retainAll(grapes);

		ArrayList<T> result = new ArrayList<T>();
		int checkedNodesCount = 0;
		while (checkedNodesCount < slotsLength) {
			T n = getNodeWithMaxLabel(ages);
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

	private<T extends Node<T>> ArrayList<T> getWhiteGrapes(Collection<T> grapes) {
		HashSet<T> whiteGrapes = new HashSet<T>();

		for (T grape : grapes) {
			whiteGrapes.addAll(grape.getSources());
		}

		return new ArrayList<T>(whiteGrapes);
	}

	private <T extends Node<T>> T getNodeWithMaxLabel(HashMap<T, Integer> nodesToCheck) {
		Map.Entry<T, Integer> result =
				new AbstractMap.SimpleImmutableEntry<T, Integer>(null, 0);

		for (Map.Entry<T, Integer> entry : nodesToCheck.entrySet()) {
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

	protected <T extends Node<T>> int lexicalComparison(Set<T> first, Set<T> second, HashMap<T, Integer> labels) {
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
			Map.Entry<T, Integer> maxFirstLabel = getLabelWithMaxValue(labels, first);
			Map.Entry<T, Integer> maxSecondLabel = getLabelWithMaxValue(labels, second);
			if (maxFirstLabel.getValue() > maxSecondLabel.getValue()) {
				result = MORE;
			} else if (maxFirstLabel.getValue() < maxSecondLabel.getValue()) {
				result = LESS;
			} else {
				HashSet<T> newFirstSet = new HashSet<T>(first);
				HashSet<T> newSecondSet = new HashSet<T>(second);

				newFirstSet.remove(maxFirstLabel.getKey());
				newSecondSet.remove(maxSecondLabel.getKey());
				result = lexicalComparison(newFirstSet, newSecondSet, labels);
			}
		}

		return result;
	}

	private <T extends Node<T>> Map.Entry<T, Integer> getLabelWithMaxValue(HashMap<T, Integer> labels, Set<T> nodes) {
		int label = 0;
		//take the very first node
		T n = nodes.iterator().next();
		for (T thisNode : nodes) {
			int thisLabel = 0;
			if (labels.containsKey(thisNode)) {
				thisLabel = labels.get(thisNode);
			}
			if (thisLabel > label) {
				label = thisLabel;
				n = thisNode;
			}
		}

		return new AbstractMap.SimpleImmutableEntry<T, java.lang.Integer>(n, label);
	}

	private <T extends Node<T>> T getNodeWithMinLexMarkedSources(HashMap<T, Integer> labels, Set<T> nodes) {
		HashSet<T> nodesWithMarkedSources = new HashSet<T>();

		for (T thisNode : nodes) {
			boolean sourcesLabeled = true;
			Set<T> sources = thisNode.getSources();
			for (T source : sources) {
				if (!labels.containsKey(source)) {
					sourcesLabeled = false;
					break;
				}
			}
			if (sourcesLabeled) {
				nodesWithMarkedSources.add(thisNode);
			}
		}
		T result = null;
		if (!nodesWithMarkedSources.isEmpty()) {
			result = getNodeWithMinimalLexSources(nodesWithMarkedSources, labels);
		}

		return result;
	}

	private <T extends Node<T>> T getNodeWithMinimalLexSources(HashSet<T> nodesWithUnmarkedSources, final HashMap<T, Integer> labels) {
		ArrayList<T> list = new ArrayList<T>(nodesWithUnmarkedSources);
		T result = Collections.max(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return lexicalComparison(o1.getSources(), o2.getSources(), labels);
			}
		});

		return result;
	}

}
