package com.drawgraph.parser.callbacks;

import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

/**
 * Date: Oct 22, 2010
 * Time: 12:04:19 AM
 *
 * @author denisk
 */
public class RootCallback implements Callback {
	private GraphMLCallback graphMLCallback;

	public RootCallback() {
		graphMLCallback = new GraphMLCallback(this);
	}

	public void startElement(String name, Attributes atts) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void endElement(String name) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void characters(String chars) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Callback getChildCallback() {
		return graphMLCallback;
	}

	public Callback getParentCallback() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
	public void postEndElement(GraphAware graphAware) {

	}
}
