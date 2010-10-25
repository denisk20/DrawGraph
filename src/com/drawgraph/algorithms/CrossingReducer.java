package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;

/**
 * Date: Oct 25, 2010
 * Time: 4:23:58 PM
 *
 * @author denisk
 */
public interface CrossingReducer {
	LayeredPositionedGraph reduce(LayeredPositionedGraph source);
}
