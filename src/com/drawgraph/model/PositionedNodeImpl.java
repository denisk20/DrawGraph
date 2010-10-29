package com.drawgraph.model;

/**
 * Date: Oct 23, 2010
 * Time: 7:40:49 PM
 *
 * @author denisk
 */
public class PositionedNodeImpl extends AbstractNode<PositionedNode> implements PositionedNode {
	private int x;
	private int y;

	public PositionedNodeImpl(String id, int x, int y) {
		this(id);
		this.x = x;
		this.y = y;
	}

	private PositionedNodeImpl(String id) {
		super(id);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}
}
