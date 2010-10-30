package com.drawgraph.model;

/**
 * Date: Oct 21, 2010
 * Time: 10:26:29 PM
 *
 * @author denisk
 */
public class GraphImpl extends AbstractGraph<Node> {
	public GraphImpl(String id) {
		super(id);
	}

	@Override
	public Graph<Node> copy() {
		Graph<Node> copy = new GraphImpl(getId());
		for (Node node : getNodes()) {
			SimpleNode copyNode = new SimpleNode(node.getId());
			copy.getNodes().add(copyNode);
		}

		addSourcesSinksLines(copy);
		return copy;
	}
}
