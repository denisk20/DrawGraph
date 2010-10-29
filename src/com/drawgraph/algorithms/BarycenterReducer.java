package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.PositionedNode;

import java.util.List;
import java.util.Map;

/**
 * Date: Oct 25, 2010
 * Time: 4:27:48 PM
 *
 * @author denisk
 */
public class BarycenterReducer extends AbstractCrossingReducer{
	@Override
	protected Map<PositionedNode, Integer> getNodeWeights(List<PositionedNode> currentLayer,
													  List<PositionedNode> bottomLayer) {
		return null;
	}
}
