package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Line;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 11:46:58 PM
 *
 * @author denisk
 */
public class GraphCallback implements Callback<SimpleNode> {
	private final static String NODE = "node";
	private final static String LINE = "edge";

	private GraphMLCallback parent;
	private NodeCallback nodeCallback;
	private LineCallback lineCallback;

	private Callback<SimpleNode> currentCallback;

	public GraphCallback(GraphMLCallback parent) {
		nodeCallback = new NodeCallback(this);
		lineCallback = new LineCallback(this);

		this.parent = parent;
	}

	public void startElement(String name, Attributes atts) {
		if (name.equals(NODE)) {
			currentCallback = nodeCallback;
		} else if (name.equals(LINE)) {
			currentCallback = lineCallback;
		} else {
			throw new IllegalArgumentException("Unsupported tag inside <graph>: " + name);
		}

		currentCallback.startElement(name, atts);
	}

	public void endElement(String name) {
		HashSet<SimpleNode> nodes = nodeCallback.getNodes();
		parent.getGraph().getNodes().addAll(nodes);
		HashMap<String, SimpleNode> nodesMap = getMapForNodes(nodes);
		HashSet<Line> lines = lineCallback.getLines(nodesMap);
		parent.getGraph().getLines().addAll(lines);
	}

	private HashMap<String, SimpleNode> getMapForNodes(HashSet<SimpleNode> nodes) {
		HashMap<String, SimpleNode> result = new HashMap<String, SimpleNode>();
		for (SimpleNode node : nodes) {
			result.put(node.getId(), node);
		}

		return result;
	}

	public void characters(String chars) {

	}

	public Callback<SimpleNode> getChildCallback() {
		return currentCallback;
	}

	public Callback<SimpleNode> getParentCallback() {
		return parent;
	}

	public void postEndElement(GraphAware graphAware) {

	}
}
