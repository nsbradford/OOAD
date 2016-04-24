package hanto.studentnsbradford.delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.MoveResult.BLUE_WINS;
import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.TestHantoCoordinate;

public class DeltaHantoMasterTest {

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
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, HantoPlayerColor.BLUE);
	}
	
	private void testSetup_butterflies() throws HantoException{
		game.makeMove(BUTTERFLY, null, BaseHantoGame.origin);
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
	}
	
	private void testSetup_sparrows() throws HantoException{
		testSetup_butterflies();
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, null, Coord(2, 0));
	}
	
	private void testSetup_crabs() throws HantoException{
		testSetup_sparrows();
		game.makeMove(CRAB, null, Coord(0, -1));
		game.makeMove(CRAB, null, Coord(2, -1));
	}
	
	//=============================================================================================
	// Moving a piece to itself throws exception
	
	@Test	// 
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
	
	@Test	// 
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
	// Resignation
	
	@Test	// 
	public void resignation_firstTurn() throws HantoException
	{
		assertEquals(RED_WINS, game.makeMove(null, null, null));
	}
	
	@Test	// 
	public void resignation_secondTurn() throws HantoException
	{
		assertEquals(OK, game.makeMove(BUTTERFLY, null, BaseHantoGame.origin));
		assertEquals(BLUE_WINS, game.makeMove(null, null, null));
	}
	
	@Test
	public void resignation_gameAlreadyOver() throws HantoException
	{
		assertEquals(RED_WINS, game.makeMove(null, null, null));
		try{
			game.makeMove(BUTTERFLY, null, BaseHantoGame.origin);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageGameAlreadyOver);
		}
	}
	
	//=============================================================================================
	// Game doesn't end in a draw after 'X' number of turns (assume < Integer.MAX_VALUE)
	
	@Test
	public void gameLastsVeryLong() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // turn 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // turn 2
		game.makeMove(SPARROW, null, Coord(2, 0));
		
		// move back and forth for many many turns; any more than this takes too long
		for (int i = 0; i < 1000; i++){
			assertEquals(OK, game.makeMove(SPARROW, Coord(-1, 0), Coord(-1, 1))); // turn 
			assertEquals(OK, game.makeMove(SPARROW, Coord(2, 0), Coord(2, -1)));
			assertEquals(OK, game.makeMove(SPARROW, Coord(-1, 1), Coord(-1, 0))); // turn 
			assertEquals(OK, game.makeMove(SPARROW, Coord(2, -1), Coord(2, 0)));
		}
	}
	
	//=============================================================================================
	// Pieces have the correct moves
	
	@Test
	public void pieceMove_butterflyWalk() throws HantoException{
		testSetup_butterflies();
		assertEquals(OK, game.makeMove(BUTTERFLY, BaseHantoGame.origin, Coord(0, 1)));
	}
	
	@Test
	public void pieceMove_sparrowFly() throws HantoException{
		testSetup_sparrows();
		assertEquals(OK, game.makeMove(SPARROW, Coord(-1, 0), Coord(3, 0)));
	}
	
	@Test
	public void pieceMove_crabWalk3() throws HantoException{
		testSetup_crabs();
		assertEquals(OK, game.makeMove(CRAB, Coord(0, -1), Coord(-2, 1)));
	}
	
	@Test
	public void pieceMove_crabWalkNo4() throws HantoException{
		testSetup_crabs();
		try{
			game.makeMove(CRAB, Coord(0, -1), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
}
