/**
 * @author Nicholas
 */
package hanto.studentnsbradford.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.TestHantoCoordinate;

/**
 * 
 * @author Nicholas
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GammaHantoMasterTest {

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
	// Game ending
	
	@Test
	public void gameEnd_blueWinsOnLastTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // turn 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1)); // turn 2
		game.makeMove(SPARROW, null, Coord(1, 1));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // turn 3
		game.makeMove(SPARROW, null, Coord(2, -1));
		game.makeMove(SPARROW, null, Coord(-1, 1)); // turn 4
		game.makeMove(SPARROW, null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-2, 1)); // turn 5
		game.makeMove(SPARROW, null, Coord(3, 0));
		
		// blue gets ready while red loiters
		game.makeMove(SPARROW, Coord(0, -1), Coord(1, -1)); // turn 6; get ready
		game.makeMove(SPARROW, null, Coord(2, 1));
		
		// waste a turn
		game.makeMove(SPARROW, Coord(-2, 1), Coord(-2, 0)); // turn 7; move back and forth
		game.makeMove(SPARROW, Coord(3, 0), Coord(3, -1));
		
		// turn 8-9, 10-11, 12-13, 14-15, 16-17, 18-19
		for (int i = 0; i < 6; i++){
			game.makeMove(SPARROW, Coord(-2, 0), Coord(-2, 1)); // turn 6; move back and forth
			game.makeMove(SPARROW, Coord(3, -1), Coord(3, 0));
			game.makeMove(SPARROW, Coord(-2, 1), Coord(-2, 0)); // turn 7; move back and forth
			game.makeMove(SPARROW, Coord(3, 0), Coord(3, -1));
		}

		// turn 20
		assertEquals(MoveResult.BLUE_WINS, 
				game.makeMove(SPARROW, Coord(-1, 1), Coord(0, 1)));
	}
	
	@Test
	public void gameEnd_redWinsBeforeLastTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1)); // move 2
		game.makeMove(SPARROW, null, Coord(1, 1));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // move 3
		game.makeMove(SPARROW, null, Coord(2, -1));
		game.makeMove(SPARROW, null, Coord(-1, 1)); // move 4
		game.makeMove(SPARROW, Coord(1, 1), Coord(0, 1));
		game.makeMove(SPARROW, null, Coord(-1, -1)); // move 5
		assertEquals(MoveResult.RED_WINS, 
				game.makeMove(SPARROW, Coord(2, -1), Coord(1, -1)));
	}
	
	@Test
	public void gameEnd_blueSelfLosesByPlacement() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1)); // move 2
		game.makeMove(SPARROW, null, Coord(1, 1));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // move 3
		game.makeMove(SPARROW, null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-1, 1)); // move 4
		game.makeMove(SPARROW, null, Coord(2, -1));
		game.makeMove(SPARROW, Coord(-1, 1), Coord(0, 1)); // move 5
		game.makeMove(SPARROW, Coord(2, -1), Coord(1, -1));
		assertEquals(MoveResult.RED_WINS, game.makeMove(SPARROW, null, Coord(-1, 1)));
	}

	
	@Test
	public void gameEnd_moveAfterGameIsOver() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1)); // move 2
		game.makeMove(SPARROW, null, Coord(1, 1));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // move 3
		game.makeMove(SPARROW, null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-1, 1)); // move 4
		game.makeMove(SPARROW, null, Coord(2, -1));
		game.makeMove(SPARROW, Coord(-1, 1), Coord(0, 1)); // move 5
		game.makeMove(SPARROW, Coord(2, -1), Coord(1, -1));
		assertEquals(MoveResult.RED_WINS, game.makeMove(SPARROW, null, Coord(-1, 1)));
		try{
			game.makeMove(BUTTERFLY, null, Coord(-1, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageGameAlreadyOver);
		}
	}
	
	@Test
	public void gameEnd_boringDraw() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // turn 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // turn 2
		game.makeMove(SPARROW, null, Coord(2, 0));
		
		// move back and forth for 16 turns
		for (int i = 0; i < 8; i++){
			game.makeMove(SPARROW, Coord(-1, 0), Coord(-1, 1)); // turn 
			game.makeMove(SPARROW, Coord(2, 0), Coord(2, -1));
			game.makeMove(SPARROW, Coord(-1, 1), Coord(-1, 0)); // turn 
			game.makeMove(SPARROW, Coord(2, -1), Coord(2, 0));
		}
		game.makeMove(SPARROW, Coord(-1, 0), Coord(-1, 1)); // turn 19
		game.makeMove(SPARROW, Coord(2, 0), Coord(2, -1));
		game.makeMove(SPARROW, Coord(-1, 1), Coord(-1, 0)); // turn 20
		assertEquals(MoveResult.DRAW, 
				game.makeMove(SPARROW, Coord(2, -1), Coord(2, 0)));
	}
	
	@Test
	public void gameEnd_simultaneousVictory() throws HantoException 
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0)); // move 1
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(0, -1)); // move 2
		game.makeMove(SPARROW, null, Coord(1, 1));
		game.makeMove(SPARROW, null, Coord(-1, 0)); // move 3
		game.makeMove(SPARROW, null, Coord(2, 0));
		game.makeMove(SPARROW, null, Coord(-1, 1)); // move 4
		game.makeMove(SPARROW, null, Coord(2, -1));
		game.makeMove(SPARROW, null, Coord(-1, 2)); // move 5
		game.makeMove(SPARROW, null, Coord(2, -2));
		game.makeMove(SPARROW, Coord(-1, 2), Coord(0, 1)); // move 6
		assertEquals(MoveResult.DRAW, 
				game.makeMove(SPARROW, Coord(2, -2), Coord(1, -1)));
	}
	
	@Test(expected = HantoException.class)
	public void resignation_doesNotWork() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(null, null, null));
	}
}
