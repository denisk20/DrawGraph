package com.drawgraph.algorithms;

import com.drawgraph.model.PositionedNode;

import java.util.List;
import java.util.Map;

/**
 * Date: Oct 29, 2010
 * Time: 2:19:45 PM
 *
 * @author denisk
 */
public class MedianReducer extends AbstractCrossingReducer{
	@Override
	protected Map<PositionedNode, Integer> getNodeWeights(List<PositionedNode> currentLayer,
													  List<PositionedNode> bottomLayer) {
		return null;
	}
}
