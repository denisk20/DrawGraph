package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 23, 2010
 * Time: 7:37:24 PM
 *
 * @author denisk
 */
public class PositionedGraphImpl extends AbstractGraph<PositionedNode> implements PositionedGraph {
	private int nodeRadius;
	public PositionedGraphImpl(String id) {
		super(id);
	}

	@Override
	public void setRadius(int r) {
		this.nodeRadius  = r;
	}

	@Override
	//this is not effective, but it works
	public int getWidth() {
		int width = 0;
		for (PositionedNode n : getNodes()) {
			if (n.getX() > width) {
				width = n.getX();
			}
		}

		return width + nodeRadius/2;
	}

	@Override
	//this is not effective, but it works
	public int getHeight() {
		int height = 0;
		for (PositionedNode n : getNodes()) {
			if (n.getY() > height) {
				height = n.getY();
			}
		}

		return height + nodeRadius/2;
	}

	@Override
	public int getRadius() {
		return nodeRadius;
	}
}
