package com.drawgraph.experimental;

import java.util.Set;

/**
 * Date: Nov 1, 2010
 * Time: 11:22:46 AM
 *
 * @author denisk
 */
public interface BaseNode extends Node<BaseNode> {
	@Override
	Set<BaseNode> getSources();
}
