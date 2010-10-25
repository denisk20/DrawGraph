package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;

/**
 * Date: Oct 25, 2010
 * Time: 4:27:48 PM
 *
 * @author denisk
 */
public class BarycenterReducer implements CrossingReducer{
	@Override
	public LayeredPositionedGraph reduce(LayeredPositionedGraph source) {
		return source;
	}
}
