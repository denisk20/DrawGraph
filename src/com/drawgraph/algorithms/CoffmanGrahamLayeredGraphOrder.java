package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
		labels = phase1(g);

		List<List<Node>> result = phase2(labels);
		return null;
	}

	protected List<List<Node>> phase2(HashMap<Node, Integer> labels) {
		List<List<Node>> result = new ArrayList<List<Node>>();
		HashMap<Node, Integer> nodesToCheck = new HashMap<Node, Integer>(labels);

		boolean nextLayer = !labels.isEmpty();
		if (layerLength > 0) {
			while (nextLayer) {
				List<Node> layer = new ArrayList<Node>();
				for (int i = 0; i < layerLength; i++) {
					Node nodeWithMaxLabel = getNodeWithMaxLabel(nodesToCheck);

					if (nodeWithMaxLabel != null) {
						layer.add(nodeWithMaxLabel);
						nodesToCheck.remove(nodeWithMaxLabel);
					} else {
						//finish this one
						if (!layer.isEmpty()) {
							result.add(layer);
						}
						//exit outer loop
						nextLayer = false;
						//exit this loop
						break;
					}
				}

				if (nextLayer) {
					result.add(layer);
				}
			}
		}
		return result;
	}

	protected List<List<Node>> phase2_1(HashMap<Node, Integer> labels, int layerLength) {
		List<List<Node>> result = new ArrayList<List<Node>>();
		//first grape
		HashSet<Node> nodesWithoutSources = new HashSet<Node>();
		for (Node n : labels.keySet()) {
			if (n.getSinks().isEmpty()) {
				nodesWithoutSources.add(n);
			}
		}

		boolean moreGrapes = true;

		ArrayList<Node> whiteGrapes = new ArrayList<Node>();
		while (moreGrapes) {
			List<Node> slots = new ArrayList<Node>(layerLength);

		}


		List<Node> bottomLayer = new ArrayList<Node>();
		final int nodesWithoutSourcesCount = nodesWithoutSources.size();
		int emptyCells = layerLength - nodesWithoutSourcesCount;
		if (emptyCells >= 0) {
			bottomLayer.addAll(nodesWithoutSources);
			nodesWithoutSources.removeAll(bottomLayer);
			//will be empty
		} else {
			Iterator<Node> nodeIterator = nodesWithoutSources.iterator();
			for (int i = 0; i < layerLength; i++) {
				Node n = nodeIterator.next();
				bottomLayer.add(n);
			}
			nodesWithoutSources.removeAll(bottomLayer);
		}
		if (!bottomLayer.isEmpty()) {
			result.add(bottomLayer);
		}
		if (!nodesWithoutSources.isEmpty()) {

		}

		return null;
	}

	private List<List<Node>> putGrapesIntoSlots(Collection<Node> allRemainingGrapes, Collection<Node> grapes, List<List<Node>> boxOfGrapes, final int slotsLength, HashMap<Node, Integer> grapesAges) {
		//this is our treasure - our box of grapes...
		if (boxOfGrapes == null) {
			boxOfGrapes = new ArrayList<List<Node>>();
		}
		//creating new slot - cosy place for our grapes
		ArrayList<Node> slot = new ArrayList<Node>(slotsLength);

		List<Node> whiteGrapes = new ArrayList<Node>();
		if (grapes.size() > slotsLength) {
			//ohhh, we've got so many grapes! Can't fit at a time!
			//just put the rest (the youngest ones) in the beginning of whiteGrapes!
			HashSet<Node> oldestGrapes = getOldestGrapes(grapes, slotsLength);
			HashSet<Node> youngestGrapes = new HashSet<Node>(grapes);
			youngestGrapes.removeAll(oldestGrapes);

			grapes.removeAll(youngestGrapes);
			//white grapes for grapes that are in the slot. Extras go first!
			whiteGrapes = new ArrayList<Node>(youngestGrapes);
		} else {
			//our slot will take all grapes! Coool!
			int extraSlots = slotsLength - grapes.size();
			//this is where we'll look for extra grapes - in remaining grapes \ white grapes
			HashSet<Node> grapesToLookForExtra = new HashSet<Node>(allRemainingGrapes);
			grapesToLookForExtra.removeAll(whiteGrapes);
			//hey, let's put extra grapes to fill the slot!
			HashSet<Node> grapesToFillTheSlot = getOldestGrapes(grapesToLookForExtra, extraSlots, grapesAges);
			//OK, let's put them as well!
			for (Node grapeToFillTheSlot : grapesToFillTheSlot) {
				slot.add(grapeToFillTheSlot);
			}
			//get white grapes for extra grapes
			ArrayList<Node> whiteGrapesForExtras = getWhiteGrapes(grapesToFillTheSlot);
			whiteGrapes.addAll(whiteGrapesForExtras);
		} 
		for (Node grape : grapes) {
			slot.add(grape);
		}

		whiteGrapes.addAll(getWhiteGrapes(slot));




		//but what if there is still some space?
//			if (slot.size() < slotsLength) {
//				int evenNowExtraSlots = slotsLength - slot.size();
//				//ok, I don't want to do this, but let's pull some grapes from white - NO!!!!!!!!! Let's create new layer instead
//			}
		//remove grapes from the sack they were in...
		allRemainingGrapes.removeAll(slot);
		//remove white grapes as well - we'd put them into a special container...
		allRemainingGrapes.removeAll(whiteGrapes);

		boxOfGrapes.add(slot);
		//OK, our slot is filled with grapes! Or not? - if so, then there are no more grapes and we shall finish and enjoy grapes!
		if (whiteGrapes.size() == 0) {
			if (allRemainingGrapes.size() > 0) {
				throw new IllegalStateException("this is very special situation, we seem to have another sack of grapes (another graph...)");
//				HashSet<Node> whiteGrapesFromAnotherSack = getOldestGrapes(allRemainingGrapes, extraSlots, grapesAges);
//				return putGrapesIntoSlots(whiteGrapesFromAnotherSack, whiteGrapesFromAnotherSack, boxOfGrapes, slotsLength, grapesAges);
			}
			return boxOfGrapes;
		} else {
			return putGrapesIntoSlots(allRemainingGrapes, whiteGrapes, boxOfGrapes, slotsLength, grapesAges);
		}

	}

	private ArrayList<Node> getWhiteGrapes(Collection<Node> grapes) {
		ArrayList<Node> whiteGrapes = new ArrayList<Node>();

		for (Node grape : grapes) {
			whiteGrapes.addAll(grape.getSources());
		}

		return whiteGrapes;
	}

	protected List<Node> getSortedNodesList(Set<Node> currentLayerSources, HashMap<Node, Integer> labels) {
		ArrayList<Node> result = new ArrayList<Node>();
		ArrayList<Integer> passedLabels = new ArrayList<Integer>();
		for (Node n : currentLayerSources) {
			passedLabels.add(labels.get(n));
		}
		Collections.sort(passedLabels);
		for (Integer label : passedLabels) {
			Node n = getNodeForLabel(labels, label);
			result.add(n);
		}
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

	private Node getNodeForLabel(HashMap<Node, Integer> labels, Integer label) {
		Node result = null;
		for (Node n : labels.keySet()) {
			if (labels.get(n).equals(label)) {
				result = n;
			}
		}
		return result;
	}

	/**
	 * This method returns a map of nodes which all nodes have labels
	 * assigned
	 *
	 * @param g graph to analyze
	 * @return map of signature <Node, Label>
	 */
	protected HashMap<Node, Integer> phase1(Graph<Node> g) {
		HashMap<Node, Integer> resultingLabels = new HashMap<Node, Integer>();
		HashSet<Node> notSourcesNodes = new HashSet<Node>();
		int label = 1;
		for (Node<Node> n : g.getNodes()) {
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
