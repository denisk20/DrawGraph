package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;

import java.util.List;

/**
 * Date: Oct 29, 2010
 * Time: 10:24:44 PM
 *
 * @author denisk
 */
public class NoDummyNodesAssigner implements DummyNodesAssigner {
	@Override
	public void assignDummyNodes(List<List<Node>> layers, Graph<Node> g) {
		//do nothing
	}
}
