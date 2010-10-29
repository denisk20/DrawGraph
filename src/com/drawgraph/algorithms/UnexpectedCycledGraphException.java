package com.drawgraph.algorithms;

/**
 * Date: Oct 29, 2010
 * Time: 10:49:31 AM
 *
 * @author denisk
 */
public class UnexpectedCycledGraphException extends IllegalArgumentException {
	public UnexpectedCycledGraphException(String s) {
		super(s);
	}
}
