/**
 * @author Nicholas
 */
package hanto.studentnsbradford.epsilon;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.OK;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.studentnsbradford.common.TestHantoCoordinate;
import hanto.studentnsbradford.delta.DeltaHantoGame;

public class EpsilonHantoRegressionTest {


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
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = HantoGameFactory.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		game = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE);
	}
	
	//=============================================================================================
	// (taken from Beta) Basic moves
	
	@Test	// 1
	public void basicMove_bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, Coord(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}


	@Test	// 2
	public void basicMove_bluePlacesInitialSparrowAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(SPARROW, null, Coord(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test	// 3
	public void basicMove_redPlacesInitialSparrowAtOrigin() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);	// RedFirst
		final MoveResult mr = game.makeMove(SPARROW, null, Coord(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test	// 4
	public void basicMove_validFirstAndSecondMove() throws HantoException
	{
		// Blue Butterfly -> (0, 0)
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
		final MoveResult mr = game.makeMove(SPARROW, null, Coord(1, 0));
		assertEquals(OK, mr);
		p = game.getPieceAt(Coord(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	

	//=============================================================================================
	// (taken from Beta with slight alterations) Invalid moves
	
	@Test	// 5
	public void invalidPiece_blueTriesToPlaceNonButterfly() throws HantoException
	{
		try{
			game.makeMove(HantoPieceType.CRANE, null, new TestHantoCoordinate(0, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidPieceType);
		}
		
	}
	
	@Test	// 6
	public void invalidPiece_redTriesToPlaceNonButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		try{
			game.makeMove(HantoPieceType.DOVE, null, new TestHantoCoordinate(1, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidPieceType);
		}
	}
	
	@Test	// 7
	public void invalidMove_validFirstMoveNonAdjacentSecondMove() throws HantoException
	{
		game.makeMove(SPARROW,  null, Coord(0, 0));
		try{
			game.makeMove(BUTTERFLY, null, Coord(1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidMove);
		}
	}
	
	@Test	// 8
	public void invalidMove_firstMoveIsNotAtOrigin() throws HantoException
	{
		try{
			game.makeMove(BUTTERFLY, null, Coord(-1, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidFirstMove);
		}
	}
	
	@Test	// 9
	public void invalidMove_blueAttemptsToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, null, Coord(1, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageNoPiecesRemaining);
		}
	}
	
	@Test	// 10
	public void invalidMove_redAttemptsToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-2, 0));
		
		try{
			game.makeMove(BUTTERFLY, null, Coord(3, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageNoPiecesRemaining);
		}
		
	}
	
	@Test	// 11
	public void invalidMove_blueTriesToPlacePieceOnOccupiedHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		try{
			game.makeMove(SPARROW, 	null, Coord(-1, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidDestination);
		}
	}
	
	@Test	// 12
	public void invalidMove_redTriesToPlacePieceOnOccupiedHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, 	null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-2, 0));
		try{
			game.makeMove(SPARROW, 	null, Coord(2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageInvalidDestination);
		}
	}
	
	@Test	// 13
	public void invalidMove_blueDoesNotPlaceButterflyByFourthMove() throws HantoException
	{
		game.makeMove(CRAB, null, Coord(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(-1, 0));	// Move 2
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-2, 0));	// Move 3
		game.makeMove(CRAB, null, Coord(3, 0));	
		try {
			game.makeMove(CRAB, null, Coord(-3, 0)); // Move 4
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageForgotButterfly);
		}
	}
	
	@Test	// 14
	public void invalidMove_redDoesNotPlaceButterflyByFourthTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));	// Move 1
		game.makeMove(CRAB, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(-1, 0));	// Move 2
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-2, 0));	// Move 3
		game.makeMove(CRAB, null, Coord(3, 0));
		game.makeMove(CRAB, null, Coord(-3, 0));	// Move 4
		try {
			game.makeMove(CRAB, null, Coord(0, -4));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), DeltaHantoGame.messageForgotButterfly);
		}
	}
	
	//=============================================================================================
	// Game ending

	@Test	// 15
	public void gameEnd_redWinsBeforeLastTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(0, -1)); // move 2
		game.makeMove(CRAB, null, Coord(1, 1));
		game.makeMove(CRAB, null, Coord(-1, 0)); // move 3
		game.makeMove(CRAB, null, Coord(2, -1));
		game.makeMove(CRAB, null, Coord(-1, 1)); // move 4
		game.makeMove(CRAB, Coord(1, 1), Coord(0, 1));
		game.makeMove(CRAB, null, Coord(-1, -1)); // move 5
		assertEquals(MoveResult.RED_WINS, 
				game.makeMove(CRAB, Coord(2, -1), Coord(1, -1)));
	}
	
	@Test	// 16
	public void gameEnd_blueSelfLosesByPlacement() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(0, -1)); // move 2
		game.makeMove(CRAB, null, Coord(1, 1));
		game.makeMove(CRAB, null, Coord(-1, 0)); // move 3
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-1, 1)); // move 4
		game.makeMove(CRAB, null, Coord(2, -1));
		game.makeMove(CRAB, Coord(-1, 1), Coord(0, 1)); // move 5
		game.makeMove(CRAB, Coord(2, -1), Coord(1, -1));
		assertEquals(MoveResult.RED_WINS, game.makeMove(SPARROW, null, Coord(-1, 1)));
	}

	
	@Test	// 17
	public void gameEnd_moveAfterGameIsOver() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(0, -1)); // move 2
		game.makeMove(CRAB, null, Coord(1, 1));
		game.makeMove(CRAB, null, Coord(-1, 0)); // move 3
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-1, 1)); // move 4
		game.makeMove(CRAB, null, Coord(2, -1));
		game.makeMove(CRAB, Coord(-1, 1), Coord(0, 1)); // move 5
		game.makeMove(CRAB, Coord(2, -1), Coord(1, -1));
		assertEquals(MoveResult.RED_WINS, game.makeMove(CRAB, null, Coord(-1, 1)));
		try{
			game.makeMove(BUTTERFLY, null, Coord(-1, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageGameAlreadyOver);
		}
	}
	
	@Test	// 18
	public void gameEnd_simultaneousVictory() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(0, -1)); // move 2
		game.makeMove(CRAB, null, Coord(1, 1));
		game.makeMove(CRAB, null, Coord(-1, 0)); // move 3
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-1, 1)); // move 4
		game.makeMove(CRAB, null, Coord(2, -1));
		game.makeMove(CRAB, null, Coord(-1, 2)); // move 5
		game.makeMove(CRAB, null, Coord(2, -2));
		game.makeMove(CRAB, Coord(-1, 2), Coord(0, 1)); // move 6
		assertEquals(MoveResult.DRAW, 
				game.makeMove(CRAB, Coord(2, -2), Coord(1, -1)));
	}
	

	//=============================================================================================
	// Moving a piece to itself throws exception
	
	@Test	// 19
	public void invalidMove_sourceEqualsDestination() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, BaseHantoGame.origin);
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, BaseHantoGame.origin, BaseHantoGame.origin);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(BaseHantoGame.messageInvalidDestination, e.getMessage());
		}
	}
		
	//=============================================================================================
	// Null destination move throws exception
	
	@Test	// 20
	public void invalidMove_nullDestination() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, BaseHantoGame.origin);
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(BUTTERFLY, BaseHantoGame.origin, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(BaseHantoGame.messageNullDestination, e.getMessage());
		}
		
		try{
			game.makeMove(BUTTERFLY, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(BaseHantoGame.messageNullDestination, e.getMessage());
		}
	}
	
	//=============================================================================================
	// Unit tests
	
	@Test	// 21
	public void unitCheck_getPrintableBoard() throws HantoException{
		assertNotNull(game.getPrintableBoard());
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertNotNull(game.getPrintableBoard());
	}
	
	@Test	// 22
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
