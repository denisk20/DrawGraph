package com.drawgraph.model;

import java.util.Set;

/**
 * Date: Oct 20, 2010
 * Time: 10:40:32 AM
 *
 * @author denisk
 */
public interface OrientedNode extends Node {
	Set<OrientedNode> getSources();
	Set<OrientedNode> getSinks();
}
