package com.drawgraph.model;

import java.util.HashSet;

/**
 * Date: Oct 21, 2010
 * Time: 10:22:06 PM
 *
 * @author denisk
 */
public interface Graph<T extends Node> {
	String getId();

	HashSet<T> getNodes();

	HashSet<Line> getLines();
}
