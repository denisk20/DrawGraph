package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Graph;
import com.drawgraph.model.GraphImpl;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

/**
 * Date: Oct 21, 2010
 * Time: 11:42:53 PM
 *
 * @author denisk
 */
public class GraphMLCallback implements Callback<SimpleNode> {
	private GraphCallback graphCallback;
	private RootCallback parent;

	private Graph<SimpleNode> graph;

	public GraphMLCallback(RootCallback parent) {
		graphCallback = new GraphCallback(this);
		this.parent = parent;
	}

	public void startElement(String name, Attributes atts) {
		graph = new GraphImpl(atts.getValue("id"));
	}

	public void endElement(String name) {

	}

	public void characters(String chars) {

	}

	public Callback<SimpleNode> getChildCallback() {
		return graphCallback;
	}

	public Callback<SimpleNode> getParentCallback() {
		return parent;
	}

	public Graph<SimpleNode> getGraph() {
		return graph;
	}

	public void postEndElement(GraphAware<SimpleNode> graphAware) {
		graphAware.setGraph(graph);
	}
}
