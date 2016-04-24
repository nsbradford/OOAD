/**
 * FlyMoveTest.java
 */

package hanto.studentnsbradford.common.movement;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
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

public class FlyMoveTest {

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
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, HantoPlayerColor.BLUE);
	}
	
	private void testSetup_normal() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, null, Coord(2, 0));
	}
	
	private void testSetup_contiguous() throws HantoException{
		game.makeMove(SPARROW, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(BUTTERFLY, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
	}

	//=============================================================================================
	// Valid flying
	
	@Test
	public void validFly_blueFlyForSecondMove() throws HantoException 
	{
		testSetup_normal();
		
		game.makeMove(SPARROW, Coord(-1, 0), Coord(3, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(3, 0));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test
	public void validFly_onlyBlueHasButterfly() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(SPARROW, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, null, Coord(2, 0));
		
		game.makeMove(SPARROW, Coord(-1, 0), Coord(3, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(3, 0));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	


	//=============================================================================================
	// Invalid walk: silly arguments
	
	@Test
	public void invalidFly_wrongColor() throws HantoException {
		testSetup_normal();
		try{
			game.makeMove(SPARROW, Coord(2, 0), Coord(-2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}
	
	@Test
	public void invalidFly_wrongPieceType() throws HantoException {
		testSetup_normal();
		try{
			game.makeMove(BUTTERFLY, Coord(-1, 0), Coord(3, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}
	
	@Test
	public void invalidFly_occupiedHex() throws HantoException {
		testSetup_normal();
		try{
			game.makeMove(SPARROW, Coord(-1, 0), Coord(0, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidDestination);
		}		
	}
	
	@Test
	public void invalidFly_butterflyNotPlaced() throws HantoException {
		game.makeMove(SPARROW, null, Coord(0, 0));
		game.makeMove(SPARROW, null, Coord(1, 0));
		try{
			game.makeMove(SPARROW, 	Coord(0, 0), Coord(2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidFly_butterflyPlacedForOtherPlayer() throws HantoException {
		game.makeMove(SPARROW, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(SPARROW, 	Coord(0, 0), Coord(2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	//=============================================================================================
	// Invalid flying: board not contiguous
	
	@Test
	public void invalidFlyContiguous_moveSelfAway() throws HantoException 
	{
		testSetup_contiguous();
		try{
			game.makeMove(SPARROW, 	Coord(2, 0), Coord(0, 2));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidFlyContiguous_isolateAnotherPiece() throws HantoException 
	{
		testSetup_contiguous();
		try{
			game.makeMove(SPARROW, Coord(0, 0), Coord(3, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidFlyContiguous_splitIntoTwoBigGroups() throws HantoException 
	{
		testSetup_contiguous();
		try{
			game.makeMove(SPARROW, Coord(0, 0), Coord(-2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	//=============================================================================================
	// Invalid flying: destination out of range TODO
	
	// TODO
}
