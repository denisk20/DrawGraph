package com.drawgraph.graphics;

import com.drawgraph.model.Graph;
import com.drawgraph.model.PositionedGraph;

/**
 * Date: Oct 22, 2010
 * Time: 5:55:36 PM
 *
 * @author denisk
 */
public interface GraphScaler {
	void setMinDistance(int dist);

	void setNodeRadius(int r);


	void setTopOffset(int off);

	void setBottomOffset(int off);

	void setLeftOffset(int off);

	void setRightOffset(int off);


	PositionedGraph scale(Graph g, LayeredGraphOrder order);
}
