package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import com.drawgraph.model.SimpleNode;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Date: Oct 21, 2010
 * Time: 11:03:13 PM
 *
 * @author denisk
 */
public class GraphMLParser implements DocumentParser {
	public Graph<SimpleNode> buildGraph(String inputFile) throws IOException, SAXException, ParserConfigurationException {

		// Create a JAXP "parser factory" for creating SAX parsers
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		// Configure the parser factory for the type of parsers we require
		spf.setValidating(false); // No validation required

		// Now use the parser factory to create a SAXParser object
		// Note that SAXParser is a JAXP class, not a SAX class
		SAXParser sp = spf.newSAXParser();

		// Create a SAX input source for the file argument
		InputSource input = new InputSource(getClass().getClassLoader().getResourceAsStream(inputFile));

		// Give the InputSource an absolute URL for the file, so that
		// it can resolve relative URLs in a <!DOCTYPE> declaration, e.g.
		File file = new File(inputFile);
		String fileName = file.getAbsolutePath();
		input.setSystemId("file://" + fileName);

		GraphMLHandler handler = new GraphMLHandler();
		System.out.println("Parsing file: " + fileName);
		sp.parse(file, handler);

		return handler.getGraph();
	}

	@Override
	public Graph<SimpleNode> buildGraph(File inputFile) throws IOException, SAXException, ParserConfigurationException {
		return buildGraph(inputFile.getAbsolutePath());
	}
}
