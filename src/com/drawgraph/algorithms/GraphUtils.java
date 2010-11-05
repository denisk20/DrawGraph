package com.drawgraph.algorithms;

import com.drawgraph.model.Node;

import java.util.List;

/**
 * Date: Oct 29, 2010
 * Time: 10:54:01 PM
 *
 * @author denisk
 */
public class GraphUtils {
	public int getLayerIndexForNode(Node n, List<? extends List<? extends Node>> layers) {
		int index = -1;
		for (int i = 0; i < layers.size(); i++) {
			List<? extends Node> layer = layers.get(i);
			if (layer.contains(n)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public int getDummiesCount(List<? extends Node> layer) {
		int count = 0;
		for (Node n : layer) {
			if (n.isDummy()) {
				count++;
			}
		}
		return count;
	}
}
