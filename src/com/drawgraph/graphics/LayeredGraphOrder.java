package com.drawgraph.graphics;

import com.drawgraph.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Oct 22, 2010
 * Time: 5:50:52 PM
 *
 * @author denisk
 */
public interface LayeredGraphOrder extends GraphOrder {
	int getLayerLength();

	int getLayersCount();

	List<List<Node>> getLayers();
}
