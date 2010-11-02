package com.drawgraph.algorithms;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredGraphImpl;
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

	@Override
	public LayeredGraph<Node> assignDummyNodes(LayeredGraph<Node> source) {
		int dummyCount = 0;
		
		LayeredGraphImpl layeredGraph = new LayeredGraphImpl(source.getId(), source.getLayers());
		layeredGraph.getLines().addAll(source.getLines());
		layeredGraph.getNodes().addAll(source.getNodes());

		GraphUtils gu = new GraphUtils();

		for (int i = 2; i < layeredGraph.getLayers().size(); i++) {
			List<Node> layer =layeredGraph.getLayers().get(i);
			for (Node<Node> nodeFromLayer : layer) {
				Set<Node> sinksCopyFromLayer = new HashSet<Node>(nodeFromLayer.getSinks());

				for (Node<Node> sink : sinksCopyFromLayer) {
					int indexOfSink = gu.getLayerIndexForNode(sink, layeredGraph.getLayers());
					if (indexOfSink == -1) {
						throw new IllegalStateException("No index for sink: " + sink);
					}
					int distance = i - indexOfSink;
					if (distance > 1) {
						int currentLayerSize = layer.size();
						boolean right = layer.indexOf(nodeFromLayer) >= currentLayerSize /2;
						sink.getSources().remove(nodeFromLayer);
						nodeFromLayer.getSinks().remove(sink);
						removeLine(layeredGraph.getLines(), nodeFromLayer, sink);

						Node previous = sink;
						for (int j = indexOfSink + 1; j < i; j++) {
							SimpleNode dummy = new SimpleNode("dummy_" + dummyCount);
							dummy.setDummy(true);
							dummy.addSink(previous);
							previous.addSource(dummy);

							layeredGraph.getLines().add(new LineImpl(dummy, previous, dummy.getId() + "->" + previous.getId()));
							layeredGraph.getNodes().add(dummy);
							dummyCount++;
							List<Node> layerToAddDummyTo = layeredGraph.getLayers().get(j);
							if (right) {
								layerToAddDummyTo.add(layerToAddDummyTo.size(), dummy);
							} else {
								layerToAddDummyTo.add(0, dummy);
							}
							previous=dummy;
						}

						layeredGraph.getLines().add(new LineImpl(nodeFromLayer, previous, nodeFromLayer.getId() + "->" + previous.getId()));
						
						nodeFromLayer.getSinks().add(previous);
						previous.getSources().add(nodeFromLayer);
					}
				}
			}
		}

		for (int i = layeredGraph.getLayers().size() -3 ; i >=0; i--) {
			List<Node> layer =layeredGraph.getLayers().get(i);
			for (Node<Node> nodeFromLayer : layer) {
				Set<Node> sinksCopyFromLayer = new HashSet<Node>(nodeFromLayer.getSinks());

				for (Node<Node> sink : sinksCopyFromLayer) {
					int indexOfSink = gu.getLayerIndexForNode(sink, layeredGraph.getLayers());
					if (indexOfSink == -1) {
						throw new IllegalStateException("No index for sink: " + sink);
					}
					int distance = indexOfSink - i;
					if (distance > 1) {
						int currentLayerSize = layer.size();
						boolean right = layer.indexOf(nodeFromLayer) >= currentLayerSize /2;
						sink.getSources().remove(nodeFromLayer);
						nodeFromLayer.getSinks().remove(sink);
						removeLine(layeredGraph.getLines(), nodeFromLayer, sink);

						Node previous = nodeFromLayer;
						for (int j = i + 1; j < indexOfSink; j++) {
							SimpleNode dummy = new SimpleNode("dummy_" + dummyCount);
							dummy.setDummy(true);
							dummy.addSource(previous);
							previous.addSink(dummy);

							layeredGraph.getLines().add(new LineImpl(previous, dummy, previous.getId() + "->" + dummy.getId()));
							layeredGraph.getNodes().add(dummy);
							dummyCount++;
							List<Node> layerToAddDummyTo = layeredGraph.getLayers().get(j);
							if (right) {
								layerToAddDummyTo.add(layerToAddDummyTo.size(), dummy);
							} else {
								layerToAddDummyTo.add(0, dummy);
							}
							previous=dummy;
						}

						layeredGraph.getLines().add(new LineImpl(previous, sink, previous.getId() + "->" + sink.getId()));

						sink.getSources().add(previous);
						previous.getSinks().add(sink);
					}
				}
			}
		}

		return layeredGraph;
	}

	private void removeLine(HashSet<Line> lines, Node<Node> nodeFromLayer, Node<Node> sink) {
		LineImpl lineToRemove = new LineImpl(nodeFromLayer, sink, "to remove");
		boolean removed = lines.remove(lineToRemove);

		if (!removed) {
			throw new IllegalStateException("Cannot remove line " + lineToRemove);
		}
	}
}
