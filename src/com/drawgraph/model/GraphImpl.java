package com.drawgraph.model;

/**
 * Date: Oct 21, 2010
 * Time: 10:26:29 PM
 *
 * @author denisk
 */
public class GraphImpl extends AbstractGraph<SimpleNode> {
	public GraphImpl(String id) {
		super(id);
	}

	@Override
	public Graph<SimpleNode> copy() {
		Graph<SimpleNode> copy = new GraphImpl(getId());
		for (Node node : getNodes()) {
			SimpleNode copyNode = new SimpleNode(node.getId());
			copy.getNodes().add(copyNode);
		}

		addSourcesSinksLines(copy);
		return copy;
	}
}
