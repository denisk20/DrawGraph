package com.drawgraph.model;

/**
 * Date: Oct 23, 2010
 * Time: 8:38:46 AM
 *
 * @author denisk
 */
public interface PositionedNode extends Node<PositionedNode> {
	int getX();

	int getY();
}
