package com.drawgraph.graphics;

/**
 * Date: Oct 22, 2010
 * Time: 5:55:36 PM
 *
 * @author denisk
 */
public interface GraphScaler {
	void setCanvasWidth(int width);

	void setCanvasHeight(int height);


	void setTopOffset(int off);

	void setBottomOffset(int off);

	void setLeftOffset(int off);

	void setRightOffset(int off);


	void setNodeRadius(int r);


	PositionedGraph scale();
}
