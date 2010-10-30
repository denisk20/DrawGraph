package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.Line;
import com.drawgraph.model.LineImpl;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: Oct 29, 2010
 * Time: 10:25:50 PM
 *
 * @author denisk
 */
public class SimpleDummyNodesAssigner implements DummyNodesAssigner {
	private int dummyCount = 0;

	@Override
	public void assignDummyNodes(List<List<Node>> layers, Graph<Node> g) {
		GraphUtils gu = new GraphUtils();

		for (int i = 2; i < layers.size(); i++) {
			List<Node> layer =layers.get(i);
			for (Node<Node> nodeFromLayer : layer) {
				Set<Node> sinksCopyFromLayer = new HashSet<Node>(nodeFromLayer.getSinks());

				for (Node<Node> sink : sinksCopyFromLayer) {
					int indexOfSink = gu.getLayerIndexForNode(sink, layers);
					if (indexOfSink == -1) {
						throw new IllegalStateException("No index for sink: " + sink);
					}
					int distance = i - indexOfSink;
					if (distance > 1) {
						int currentLayerSize = layer.size();
						boolean right = layer.indexOf(nodeFromLayer) >= currentLayerSize /2;
						sink.getSources().remove(nodeFromLayer);
						nodeFromLayer.getSinks().remove(sink);
						removeLine(g.getLines(), nodeFromLayer, sink);

						Node previous = sink;
						for (int j = indexOfSink + 1; j < i; j++) {
							SimpleNode dummy = new SimpleNode("dummy_" + dummyCount);
							dummy.setDummy(true);
							dummy.addSink(previous);
							previous.addSource(dummy);

							dummyCount++;
							List<Node> layerToAddDummyTo = layers.get(j);
							if (right) {
								layerToAddDummyTo.add(layerToAddDummyTo.size(), dummy);
							} else {
								layerToAddDummyTo.add(0, dummy);
							}
							previous=dummy;
						}

						nodeFromLayer.getSinks().add(previous);
						previous.getSources().add(nodeFromLayer);
					}
				}
			}
		}
	}

	private void removeLine(HashSet<Line> lines, Node<Node> nodeFromLayer, Node<Node> sink) {
		LineImpl lineToRemove = new LineImpl(nodeFromLayer, sink, "to remove");
		boolean removed = lines.remove(lineToRemove);

		if (!removed) {
			throw new IllegalStateException("Cannot remove line " + lineToRemove);
		}
	}
}
