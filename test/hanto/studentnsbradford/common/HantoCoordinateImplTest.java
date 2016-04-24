/**
 * 
 */
package hanto.studentnsbradford.common;

import static org.junit.Assert.*;

import org.junit.Test;

import hanto.common.HantoCoordinate;

/**
 * @author Nicholas
 *
 */
public class HantoCoordinateImplTest {

	// Helper methods
	private HantoCoordinate Coord(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	@Test // 14
	public void unitCheck_isAdjacentToOccupiedHex()
	{
		final HantoCoordinate loc = Coord(0, 0);
		
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(0, 1)));
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(1, 0)));
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(1, -1)));
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(0, -1)));
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(-1, 0)));
		assertTrue(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(-1, 1)));
		
		assertFalse(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(0, 0)));
		assertFalse(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(1, 1)));
		assertFalse(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(2, 0)));
		assertFalse(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(1, 2)));
		assertFalse(HantoCoordinateImpl.isAdjacentToHex(loc, Coord(-1, -1)));
	}

}
