package com.drawgraph.parser;

import com.drawgraph.model.Graph;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Date: Oct 21, 2010
 * Time: 11:03:13 PM
 *
 * @author denisk
 */
public class GraphMLParser implements DocumentParser {
	public Graph buildGraph(String inputFile) throws IOException, SAXException, ParserConfigurationException {

		// Create a JAXP "parser factory" for creating SAX parsers
		SAXParserFactory spf = SAXParserFactory.newInstance();

		// Configure the parser factory for the type of parsers we require
		spf.setValidating(false); // No validation required

		// Now use the parser factory to create a SAXParser object
		// Note that SAXParser is a JAXP class, not a SAX class
		SAXParser sp = spf.newSAXParser();

		// Create a SAX input source for the file argument
		InputSource input = new InputSource(getClass().getClassLoader().getResourceAsStream(inputFile));

		// Give the InputSource an absolute URL for the file, so that
		// it can resolve relative URLs in a <!DOCTYPE> declaration, e.g.
		input.setSystemId("file://" + new File(inputFile).getAbsolutePath());

		GraphMLHandler handler = new GraphMLHandler();
		sp.parse(input, handler);

		return handler.getGraph();
	}
}
