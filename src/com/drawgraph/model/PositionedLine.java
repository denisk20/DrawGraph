package com.drawgraph.model;

/**
 * Date: Oct 23, 2010
 * Time: 8:40:37 AM
 *
 * @author denisk
 */
public interface PositionedLine extends Line {
	@Override
	PositionedNode getSource();

	@Override
	PositionedNode getSink();
}
