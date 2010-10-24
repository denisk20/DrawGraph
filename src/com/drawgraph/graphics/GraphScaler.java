package com.drawgraph.graphics;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;

/**
 * Date: Oct 22, 2010
 * Time: 5:55:36 PM
 *
 * @author denisk
 */
public interface GraphScaler {
	void setMinDistance(int dist);

	void setTopOffset(int off);

	void setLeftOffset(int off);

	void setLayerOffset(int layerOffset);


	LayeredPositionedGraph scale(Graph<Node> g, LayeredGraphOrder<Node> order);

	int getMinDistance();

	int getLayerOffset();

	int getTopOffset();

	int getLeftOffset();
}
