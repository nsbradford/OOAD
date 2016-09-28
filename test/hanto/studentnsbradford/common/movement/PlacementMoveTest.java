/**
 * @author Nicholas
 */

package hanto.studentnsbradford.common.movement;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
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

public class PlacementMoveTest {

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
	// Invalid placement (next to an opposing piece)
	
	@Test
	public void invalidPlacement_blueSparrowSecondMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		try{
			game.makeMove(SPARROW, null, Coord(2, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidPlacement_redButterflyMovesNextToTwoBluePieces() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		try{
			game.makeMove(SPARROW, null, Coord(0, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidPlacement_redButterflyMovesNextToMixedPieces() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(SPARROW, null, Coord(1, 0));
		game.makeMove(SPARROW, null, Coord(-1, 0));
		try{
			game.makeMove(BUTTERFLY, null, Coord(0, -1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}


}
