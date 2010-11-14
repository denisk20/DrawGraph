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

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public PositionedNode newInstance(String id) {
		return new PositionedNodeImpl(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PositionedNodeImpl)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		final PositionedNodeImpl that = (PositionedNodeImpl) o;

		if (x != that.x) {
			return false;
		}
		if (y != that.y) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + x;
		result = 31 * result + y;
		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "PositionedNodeImpl{" + "x=" + x + ", y=" + y + '}';
	}
}
