/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.HORSE;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.TestHantoCoordinate;

/**
 * 
 * @author Nicholas
 *
 */
public class JumpMoveTest {

	// Helper methods
	private HantoCoordinate Coord(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	//=============================================================================================
	// Setup
	
	private static HantoGameFactory factory;
	private HantoGame game;
	private static final String messageNoException = "Missing expected exception.";
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE);
	}
	
	private void testSetup_x() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(HORSE, null, Coord(-1, 0));
		game.makeMove(HORSE, null, Coord(2, 0));
	}
	
	private void testSetup_y() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(0, 1));
		game.makeMove(HORSE, null, Coord(0, -1));
		game.makeMove(HORSE, null, Coord(0, 2));
	}
	
	private void testSetup_z() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, -1));
		game.makeMove(HORSE, null, Coord(-1, 1));
		game.makeMove(HORSE, null, Coord(2, -2));
	}
	
	private void testSetup_complex() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));	// turn 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(HORSE, null, Coord(-1, 0));		// turn 2
		game.makeMove(CRAB, null, Coord(1, 1));
		game.makeMove(CRAB, null, Coord(0, -1));	// turn 3
		game.makeMove(CRAB, null, Coord(2, 1));
		game.makeMove(CRAB, null, Coord(0, -2));	// turn 4
		game.makeMove(CRAB, null, Coord(3, 0));
	}
	
	//=============================================================================================
	// Valid jumping
	
	@Test
	public void validJump_NE() throws HantoException {
		testSetup_x();
		game.makeMove(HORSE, Coord(-1, 0), Coord(3, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(3, 0));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void validJump_SW() throws HantoException {
		testSetup_x();
		game.makeMove(CRAB, null, Coord(-1, 1));
		game.makeMove(HORSE, Coord(2, 0), Coord(-2, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(-2, 0));
		assertNull(game.getPieceAt(Coord(2, 0)));
		assertEquals(RED, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void validJump_N() throws HantoException {
		testSetup_y();
		game.makeMove(HORSE, Coord(0, -1), Coord(0, 3));
		
		final HantoPiece p = game.getPieceAt(Coord(0, 3));
		assertNull(game.getPieceAt(Coord(0, -1)));
		assertEquals(BLUE, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void validJump_S() throws HantoException {
		testSetup_y();
		game.makeMove(CRAB, null, Coord(-1, 0));
		game.makeMove(HORSE, Coord(0, 2), Coord(0, -2));
		
		final HantoPiece p = game.getPieceAt(Coord(0, -2));
		assertNull(game.getPieceAt(Coord(0, 2)));
		assertEquals(RED, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
		@Test
	public void validJump_SE() throws HantoException {
		testSetup_z();
		game.makeMove(HORSE, Coord(-1, 1), Coord(3, -3));
		
		final HantoPiece p = game.getPieceAt(Coord(3, -3));
		assertNull(game.getPieceAt(Coord(-1, 1)));
		assertEquals(BLUE, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void validJump_NW() throws HantoException {
		testSetup_z();
		game.makeMove(CRAB, null, Coord(0, 1));
		game.makeMove(HORSE, Coord(2, -2), Coord(-2, 2));
		
		final HantoPiece p = game.getPieceAt(Coord(-2, 2));
		assertNull(game.getPieceAt(Coord(2, -2)));
		assertEquals(RED, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void validJump_complex() throws HantoException {
		testSetup_complex();
		game.makeMove(HORSE, Coord(-1, 0), Coord(2, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(2, 0));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	//=============================================================================================
	// Invalid jumping

	@Test
	public void invalidJump_adjacentHex() throws HantoException {
		testSetup_x();
		try{
			game.makeMove(HORSE, Coord(-1, 0), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidJump_notStraightLine() throws HantoException {
		testSetup_x();
		try{
			game.makeMove(HORSE, Coord(-1, 0), Coord(1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidJump_unoccupiedHexInPath() throws HantoException {
		testSetup_complex();
		
		try{
			game.makeMove(HORSE, Coord(-1, 0), Coord(4, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
}
