package com.drawgraph.model;

/**
 * Date: Oct 21, 2010
 * Time: 10:28:28 PM
 *
 * @author denisk
 */
public class LineImpl implements Line {
	private String id;
	private Node source;
	private Node sink;

	public LineImpl (Node source, Node sink, String id) {
		this.source = source;
		this.sink = sink;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Node getSource() {
		return source;
	}

	public Node getSink() {
		return sink;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LineImpl)) {
			return false;
		}

		final LineImpl line = (LineImpl) o;

		if (sink != null ? !sink.equals(line.sink) : line.sink != null) {
			return false;
		}
		if (source != null ? !source.equals(line.source) : line.source != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = source != null ? source.hashCode() : 0;
		result = 31 * result + (sink != null ? sink.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "LineImpl{" + "source=" + source + ", sink=" + sink + '}';
	}
}
