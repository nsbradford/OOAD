/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common;

import static org.junit.Assert.*;

import org.junit.Test;

import hanto.studentnsbradford.common.HantoDirection;
import hanto.common.HantoCoordinate;

/**
 * @author Nicholas
 *
 */
public class HantoCoordinateImplTest {

	// Helper methods
	private HantoCoordinateImpl Coord(int x, int y)
	{
		return new HantoCoordinateImpl(x, y);
	}
	
	@Test
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

	@Test
	public void unitCheck_hexDistanceTo()
	{
		assertEquals(1, Coord(0, 0).hexDistanceTo(Coord(1, 0))); 	// simple
		assertEquals(3, Coord(0, 0).hexDistanceTo(Coord(3, 0))); 	// x axis
		assertEquals(3, Coord(0, 0).hexDistanceTo(Coord(-3, 0)));
		assertEquals(6, Coord(0, 0).hexDistanceTo(Coord(0, 6))); 	// y axis
		assertEquals(6, Coord(0, 0).hexDistanceTo(Coord(0, -6)));
		assertEquals(4, Coord(0, 0).hexDistanceTo(Coord(-4, 4))); 	// z axis
		assertEquals(4, Coord(0, 0).hexDistanceTo(Coord(4, -4)));
		
		assertEquals(3, Coord(0, 0).hexDistanceTo(Coord(2, 1)));	// ++ quad
		assertEquals(2, Coord(0, 0).hexDistanceTo(Coord(-1, 2)));	// -+ quad
		assertEquals(5, Coord(0, 0).hexDistanceTo(Coord(-1, -4)));	// -- quad
		assertEquals(3, Coord(0, 0).hexDistanceTo(Coord(3, -2)));	// +- quad
	}
	
	@Test
	public void unitCheck_getDirectionTo()
	{
		final HantoCoordinateImpl origin = Coord(0, 0);
		assertEquals(HantoDirection.N, origin.getDirectionTo(Coord(0, 1)));
		assertEquals(HantoDirection.NE, origin.getDirectionTo(Coord(1, 0)));
		assertEquals(HantoDirection.SE, origin.getDirectionTo(Coord(1, -1)));
		assertEquals(HantoDirection.S, origin.getDirectionTo(Coord(0, -1)));
		assertEquals(HantoDirection.SW, origin.getDirectionTo(Coord(-1, 0)));
		assertEquals(HantoDirection.NW, origin.getDirectionTo(Coord(-1, 1)));
		
		assertEquals(HantoDirection.N, origin.getDirectionTo(Coord(0, 4)));
		assertEquals(HantoDirection.NE, origin.getDirectionTo(Coord(5, 0)));
		assertEquals(HantoDirection.SE, origin.getDirectionTo(Coord(3, -3)));
		assertEquals(HantoDirection.S, origin.getDirectionTo(Coord(0, -4)));
		assertEquals(HantoDirection.SW, origin.getDirectionTo(Coord(-2, 0)));
		assertEquals(HantoDirection.NW, origin.getDirectionTo(Coord(-8, 8)));
		
		assertNull(origin.getDirectionTo(Coord(1, 4)));
		assertNull(origin.getDirectionTo(Coord(5, 2)));
		assertNull(origin.getDirectionTo(Coord(4, -3)));
		assertNull(origin.getDirectionTo(Coord(2, -4)));
		assertNull(origin.getDirectionTo(Coord(-2, 1)));
		assertNull(origin.getDirectionTo(Coord(-8, 10)));
	}
}
