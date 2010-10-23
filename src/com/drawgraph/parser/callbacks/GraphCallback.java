package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Line;
import com.drawgraph.model.Node;
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
public class GraphCallback implements Callback{
	private final static String NODE = "node";
	private final static String LINE = "edge";

	private GraphMLCallback parent;
	private NodeCallback nodeCallback;
	private LineCallback lineCallback;

	private Callback currentCallback;

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
		HashSet<Node> nodes = nodeCallback.getNodes();
		parent.getGraph().getNodes().addAll(nodes);
		HashMap<String, Node> nodesMap = getMapForNodes(nodes);
		HashSet<Line<Node>> lines = lineCallback.getLines(nodesMap);
		parent.getGraph().getLines().addAll(lines);
	}

	private HashMap<String, Node> getMapForNodes(HashSet<Node> nodes) {
		HashMap<String, Node> result = new HashMap<String, Node>();
		for (Node node : nodes) {
			result.put(node.getId(), node);
		}

		return result;
	}

	public void characters(String chars) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Callback getChildCallback() {
		return currentCallback;
	}

	public Callback getParentCallback() {
		return parent;
	}

	public void postEndElement(GraphAware graphAware) {

	}
}
