package com.drawgraph.graphics;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Node;
import com.drawgraph.model.PositionedGraph;
import com.drawgraph.model.PositionedLine;
import com.drawgraph.model.PositionedNode;
import com.drawgraph.model.PositionedNodeImpl;

import java.util.HashSet;
import java.util.List;

/**
 * Date: Oct 23, 2010
 * Time: 12:46:53 PM
 *
 * @author denisk
 */
public class GraphScalerImpl implements GraphScaler{
	private int minDistance;
	private int nodeRadius;

	private int topOffset;
	private int bottomOffset;
	private int leftOffset;
	private int rightOffset;

	@Override
	public void setMinDistance(int dist) {
		minDistance = dist;
	}

	@Override
	public void setNodeRadius(int r) {
		nodeRadius = r;
	}

	@Override
	public void setTopOffset(int off) {
		topOffset = off;
	}

	@Override
	public void setBottomOffset(int off) {
		bottomOffset = off;
	}

	@Override
	public void setLeftOffset(int off) {
		leftOffset = off;
	}

	@Override
	public void setRightOffset(int off) {
		rightOffset = off;
	}

	@Override
	public PositionedGraph scale(Graph<Node> g, LayeredGraphOrder<Node> order) {
		final List<List<Node>> layers = order.getLayers(g);

		HashSet<PositionedNode> positionedNodes = new HashSet<PositionedNode>();
		HashSet<PositionedLine> positionedLines = new HashSet<PositionedLine>();

		int curX = leftOffset;
		int curY = topOffset;
		for (List<Node> layer : layers) {
//			PositionedNode positionedNode = new PositionedNodeImpl();
		}
		return null;
	}
}
