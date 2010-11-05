package com.drawgraph.algorithms;

import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredGraphImpl;
import com.drawgraph.model.Line;
import com.drawgraph.model.LineImpl;
import com.drawgraph.model.Node;

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
	public <T extends Node<T>> LayeredGraph<T> assignDummyNodes(LayeredGraph<T> source) {
		int dummyCount = 0;
		
		LayeredGraphImpl<T> layeredGraph = new LayeredGraphImpl<T>(source.getId(), source.getLayers());
		layeredGraph.getLines().addAll(source.getLines());
		layeredGraph.getNodes().addAll(source.getNodes());

		GraphUtils gu = new GraphUtils();

		//collect information on routes that go from top to bottom
		for (int i = 2; i < layeredGraph.getLayers().size(); i++) {
			List<T> layer =layeredGraph.getLayers().get(i);
			for (T nodeFromLayer : layer) {
				Set<T> sinksCopyFromLayer = new HashSet<T>(nodeFromLayer.getSinks());

				for (T sink : sinksCopyFromLayer) {
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

						T previous = sink;
						for (int j = indexOfSink + 1; j < i; j++) {
							T dummy = previous.newInstance("dummy_" + dummyCount);

							dummy.setDummy(true);
							dummy.addSink(previous);
							previous.addSource(dummy);

							layeredGraph.getLines().add(new LineImpl(dummy, previous, dummy.getId() + "->" + previous.getId()));
							layeredGraph.getNodes().add(dummy);
							dummyCount++;
							List<T> layerToAddDummyTo = layeredGraph.getLayers().get(j);
							if (right) {
								layerToAddDummyTo.add(layerToAddDummyTo.size(), dummy);
							} else {
								layerToAddDummyTo.add(0, dummy);
							}
							previous = dummy;
						}

						layeredGraph.getLines().add(new LineImpl(nodeFromLayer, previous, nodeFromLayer.getId() + "->" + previous.getId()));
						
						nodeFromLayer.getSinks().add(previous);
						previous.getSources().add(nodeFromLayer);
					}
				}
			}
		}

		//collect information on routes that go from bottom to top
		for (int i = layeredGraph.getLayers().size() -3 ; i >=0; i--) {
			List<T> layer =layeredGraph.getLayers().get(i);
			for (T nodeFromLayer : layer) {
				Set<T> sinksCopyFromLayer = new HashSet<T>(nodeFromLayer.getSinks());

				for (T sink : sinksCopyFromLayer) {
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

						T previous = nodeFromLayer;
						for (int j = i + 1; j < indexOfSink; j++) {
							T dummy = previous.newInstance("dummy_" + dummyCount);
							dummy.setDummy(true);
							dummy.addSource(previous);
							previous.addSink(dummy);

							layeredGraph.getLines().add(new LineImpl(previous, dummy, previous.getId() + "->" + dummy.getId()));
							layeredGraph.getNodes().add(dummy);
							dummyCount++;
							List<T> layerToAddDummyTo = layeredGraph.getLayers().get(j);
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

	private <T extends Node<T>> void removeLine(HashSet<Line> lines, T nodeFromLayer, T sink) {
		LineImpl lineToRemove = new LineImpl(nodeFromLayer, sink, "to remove");
		boolean removed = lines.remove(lineToRemove);

		if (!removed) {
			throw new IllegalStateException("Cannot remove line " + lineToRemove);
		}
	}
}
