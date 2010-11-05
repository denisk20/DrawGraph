package com.drawgraph.model;

/**
 * Date: Oct 20, 2010
 * Time: 10:32:10 AM
 *
 * @author denisk
 */
public class SimpleNode extends AbstractNode<SimpleNode> {
	public SimpleNode(String id) {
		super(id);
	}

	@Override
	public SimpleNode newInstance(String s) {
		SimpleNode node = new SimpleNode(s);

		return node;
	}
}
