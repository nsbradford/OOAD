/**
 * WalkMoveTest.java
 * @author Nicholas
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

public class WalkMoveTest {

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
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, HantoPlayerColor.BLUE);
	}
	
	//=============================================================================================
	// Valid walking
	
	@Test
	public void validWalk_blueWalkForThirdMove() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		game.makeMove(SPARROW, 	Coord(-1, 0), Coord(-1, 1));
		final HantoPiece p = game.getPieceAt(Coord(-1, 1));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test
	public void validWalk_butterflyHasBeenPlacedOnlyOnePlayer() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(SPARROW, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		game.makeMove(SPARROW, 	Coord(-1, 0), Coord(-1, 1));
		final HantoPiece p = game.getPieceAt(Coord(-1, 1));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}


	//=============================================================================================
	// Invalid walk: silly arguments
	
	@Test
	public void invalidWalk_wrongColor() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(1, 0), Coord(0, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}
	
	@Test
	public void invalidWalk_wrongPieceType() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(SPARROW, Coord(0, 0), Coord(0, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}
	
	@Test
	public void invalidWalk_occupiedHex() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(0, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidDestination);
		}		
	}
	
	@Test
	public void invalidWalk_nonAdjacentHex() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}

	@Test
	public void invalidWalk_notAdjacentToOccupiedHex() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(-1, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}		
	}
	
	@Test
	public void invalidWalk_butterflyNotPlaced() throws HantoException {
		game.makeMove(SPARROW, null, Coord(0, 0));
		game.makeMove(SPARROW, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(SPARROW, 	Coord(-1, 0), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidWalk_butterflyPlacedForOtherPlayer() throws HantoException {
		game.makeMove(SPARROW, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(SPARROW, 	Coord(-1, 0), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}

	//=============================================================================================
	// Invalid walk: sliding details, and keeping the board contiguous
	
	@Test
	public void invalidWalkSlide_notEnoughSlidingRoomChosenSide() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1));
		game.makeMove(SPARROW, null, Coord(2, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(1, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidWalkContiguous_moveSelfAway() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(SPARROW, 	Coord(-1, 0), Coord(-1, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidWalkContiguous_isolateAnotherPiece() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(0, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidWalkContiguous_splitIntoTwoBigGroups() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(BUTTERFLY, Coord(0, 0), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
}
