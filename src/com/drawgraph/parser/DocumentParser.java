package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.SimpleNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Date: Oct 21, 2010
 * Time: 10:32:45 PM
 *
 * @author denisk
 */
public interface DocumentParser {
	Graph<SimpleNode> buildGraph(String inputFile) throws IOException, SAXException, ParserConfigurationException;

	Graph<SimpleNode> buildGraph(File inputFile) throws IOException, SAXException, ParserConfigurationException;
}
