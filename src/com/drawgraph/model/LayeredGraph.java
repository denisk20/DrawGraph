package com.drawgraph.model;

import java.util.List;

/**
 * Date: Oct 30, 2010
 * Time: 3:42:16 PM
 *
 * @author denisk
 */
public interface LayeredGraph<T extends Node> extends Graph<T>{
	List<List<T>> getLayers();
}
