package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 11:48:01 PM
 *
 * @author denisk
 */
public class NodeCallback implements Callback {
	private GraphCallback parent;
	private HashSet<Node<Node>> nodes = new HashSet<Node<Node>>();

	private final static String ID = "id";

	public NodeCallback(GraphCallback parent) {
		this.parent = parent;
	}

	public void startElement(String name, Attributes atts) {
		SimpleNode node = new SimpleNode(atts.getValue(ID));
		nodes.add(node);
	}

	public void endElement(String name) {

	}

	public void characters(String chars) {
		
	}

	public Callback getChildCallback() {
		return null;
	}

	public Callback getParentCallback() {
		return parent;
	}

	public HashSet<Node<Node>> getNodes() {
		return nodes;
	}

	public void postEndElement(GraphAware graphAware) {

	}
}
