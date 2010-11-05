package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.callbacks.Callback;
import com.drawgraph.parser.callbacks.RootCallback;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Date: Oct 21, 2010
 * Time: 11:13:02 PM
 *
 * @author denisk
 */
public class GraphMLHandler extends DefaultHandler implements GraphAware<SimpleNode> {
	private Callback<SimpleNode> currentCallback;
	private Graph<SimpleNode> graph;

	public GraphMLHandler() {
		currentCallback = new RootCallback();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		currentCallback.startElement(qName, attributes);
		currentCallback = currentCallback.getChildCallback();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentCallback.endElement(qName);
		currentCallback.postEndElement(this);

		currentCallback = currentCallback.getParentCallback();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
	}

	public Graph<SimpleNode> getGraph() {
		return graph;
	}

	public void setGraph(Graph<SimpleNode> graph) {
		this.graph = graph;
	}
}
