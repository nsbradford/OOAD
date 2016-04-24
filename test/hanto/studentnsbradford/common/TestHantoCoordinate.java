/**
 * @author Nicholas
 */

package hanto.studentnsbradford.common;

import hanto.common.HantoCoordinate;

/**
 * @author Nicholas
 * Internal class for these test cases.
 */
public class TestHantoCoordinate implements HantoCoordinate {

	private final int x, y;
	
	/**
	 * Constructor.
	 * @param x
	 * @param y
	 */
	public TestHantoCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/*
	 * @see hanto.common.HantoCoordinate#getX()
	 */
	@Override
	public int getX()
	{
		return x;
	}

	/*
	 * @see hanto.common.HantoCoordinate#getY()
	 */
	@Override
	public int getY()
	{
		return y;
	}
}
