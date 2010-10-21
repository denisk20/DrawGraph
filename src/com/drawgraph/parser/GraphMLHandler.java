package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import org.xml.sax.Attributes;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Date: Oct 21, 2010
 * Time: 11:13:02 PM
 *
 * @author denisk
 */
public class GraphMLHandler extends DefaultHandler {
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();	//To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();	//To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);	//To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);	//To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);	//To change body of overridden methods use File | Settings | File Templates.
	}

	public Graph getGraph() {
		return null;
	}
}
