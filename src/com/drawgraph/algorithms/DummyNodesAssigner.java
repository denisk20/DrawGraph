package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.Node;

import java.util.List;

/**
 * Date: Oct 29, 2010
 * Time: 10:21:26 PM
 *
 * @author denisk
 */
public interface DummyNodesAssigner {
	LayeredGraph<Node> assignDummyNodes(LayeredGraph<Node> source);
}
