package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Date: Oct 21, 2010
 * Time: 10:32:45 PM
 *
 * @author denisk
 */
public interface DocumentParser {
	Graph buildGraph(String inputFile) throws IOException, SAXException, ParserConfigurationException;
}
