package com.drawgraph.model;

/**
 * Date: Oct 20, 2010
 * Time: 10:32:42 AM
 *
 * @author denisk
 */
public interface Line {
	String getId();
	Node getSource();

	Node getSink();
}
