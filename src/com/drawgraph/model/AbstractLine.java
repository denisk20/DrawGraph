package com.drawgraph.model;

/**
 * Date: Oct 23, 2010
 * Time: 8:02:57 PM
 *
 * @author denisk
 */
public abstract class AbstractLine implements Line {
		private String id;
	private Node source;
	private Node sink;

	public AbstractLine (Node source, Node sink, String id) {
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
		if (!(o instanceof Line)) {
			return false;
		}

		final Line line = (Line) o;

		if (sink != null ? !sink.equals(line.getSink()) : line.getSink() != null) {
			return false;
		}
		if (source != null ? !source.equals(line.getSource()) : line.getSource() != null) {
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
		return "LineImpl{id=" +id + "source=" + source.getId() + ", sink=" + sink.getId() + '}';
	}

}
