package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 23, 2010
 * Time: 7:48:22 PM
 *
 * @author denisk
 */
public abstract class AbstractGraph<T extends Node> implements Graph<T> {
	private String id;
	private HashSet<T> nodes = new HashSet<T>();
	private HashSet<Line> lines = new HashSet<Line>();

	public AbstractGraph(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public HashSet<T> getNodes() {
		return nodes;
	}

	public HashSet<Line> getLines() {
		return lines;
	}

	@Override
	public T getNodeById(String id) {
		for (T node : nodes) {
			if (node.getId().equals(id)) {
				return node;
			}
		}
		throw new IllegalArgumentException("No node with id " + id + " in graph " + this.id);
	}

	protected void addSourcesSinksLines(Graph<T> copy) {
		for (Node<Node> node : getNodes()) {
			Node copyNode = copy.getNodeById(node.getId());
			for (Node source : node.getSources()) {
				Node copySource = copy.getNodeById(source.getId());
				copyNode.getSources().add(copySource);
			}
			for (Node sink : node.getSinks()) {
				Node copySink = copy.getNodeById(sink.getId());
				copyNode.getSinks().add(copySink);
			}
		}

		for (Line l : getLines()) {
			LineImpl line = new LineImpl(copy.getNodeById(l.getSource().getId()), copy.getNodeById(l
					.getSink().getId()), l.getId());
			copy.getLines().add(line);
		}
	}
}
