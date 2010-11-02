package com.drawgraph.experimental;

import java.util.Set;

/**
 * Date: Nov 1, 2010
 * Time: 11:21:41 AM
 *
 * @author denisk
 */
public interface Node <T extends Node<? super T>> {
	Set<T> getSources();
}
