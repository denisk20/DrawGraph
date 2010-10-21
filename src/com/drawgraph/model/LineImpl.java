package com.drawgraph.model;

/**
 * Date: Oct 21, 2010
 * Time: 10:28:28 PM
 *
 * @author denisk
 */
public class LineImpl implements Line {
	private Node source;
	private Node sink;

	public LineImpl (Node source, Node sink) {
		this.source = source;
		this.sink = sink;
	}

	public Node getSource() {
		return source;
	}

	public Node getSink() {
		return sink;
	}
}
