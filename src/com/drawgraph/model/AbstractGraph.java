package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 23, 2010
 * Time: 7:48:22 PM
 *
 * @author denisk
 */
public abstract class AbstractGraph<T extends Node> implements Graph<T>{
	private String id;
	private HashSet<T> nodes = new HashSet<T>();
	private HashSet<Line<T>> lines = new HashSet<Line<T>>();

	public AbstractGraph(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public HashSet<T> getNodes() {
		return nodes;
	}

	public HashSet<Line<T>> getLines() {
		return lines;
	}
}
