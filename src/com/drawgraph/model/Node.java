package com.drawgraph.model;

import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:38:44 AM
 *
 * @author denisk
 */
public interface Node {
	String getId();
	Set<Node> getSources();
	Set<Node> getSinks();

	Set<Node> getNeighbours();
}
