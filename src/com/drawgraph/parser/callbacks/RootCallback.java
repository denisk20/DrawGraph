package com.drawgraph.parser.callbacks;

import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

/**
 * Date: Oct 22, 2010
 * Time: 12:04:19 AM
 *
 * @author denisk
 */
public class RootCallback implements Callback<SimpleNode> {
	private GraphMLCallback graphMLCallback;

	public RootCallback() {
		graphMLCallback = new GraphMLCallback(this);
	}

	public void startElement(String name, Attributes atts) {

	}

	public void endElement(String name) {

	}

	public void characters(String chars) {

	}

	public Callback<SimpleNode> getChildCallback() {
		return graphMLCallback;
	}

	public Callback<SimpleNode> getParentCallback() {
		return null;
	}

	public void postEndElement(GraphAware<SimpleNode> graphAware) {

	}
}
