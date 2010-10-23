package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Node;
import com.drawgraph.parser.GraphAware;
import org.xml.sax.Attributes;

/**
 * Date: Oct 21, 2010
 * Time: 11:43:14 PM
 *
 * @author denisk
 */
public interface Callback {
	void startElement(String name, Attributes atts);

	void endElement(String name);

	void characters(String chars);

	Callback getChildCallback();

	Callback getParentCallback();

	void postEndElement(GraphAware<Node> graphAware);
}
