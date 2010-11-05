package com.drawgraph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Oct 30, 2010
 * Time: 3:49:08 PM
 *
 * @author denisk
 */
public class LayeredGraphImpl<T extends Node<T>> extends AbstractGraph<T> implements LayeredGraph<T>{
	private List<List<T>> layers = new ArrayList<List<T>>();

	public LayeredGraphImpl(String id, List<List<T>> layers) {
		super(id);
		this.layers.addAll(layers);
	}

	@Override
	public List<List<T>> getLayers() {
		return layers;
	}

	@Override
	public Graph<T> copy() {
		LayeredGraphImpl<T> copy = new LayeredGraphImpl<T>(getId(), new ArrayList<List<T>>(layers));
		for (T node : getNodes()) {
			copy.getNodes().add(node.newInstance(node.getId()));
		}

		addSourcesSinksLines(copy);
		return copy;
	}
}
