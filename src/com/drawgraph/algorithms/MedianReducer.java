package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;

/**
 * Date: Oct 25, 2010
 * Time: 4:26:56 PM
 *
 * @author denisk
 */
public class MedianReducer implements CrossingReducer{
	@Override
	public LayeredPositionedGraph reduce(LayeredPositionedGraph source) {
		return source;
	}
}
