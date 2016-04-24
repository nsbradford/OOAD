/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentnsbradford.beta;

import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.TestHantoCoordinate;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
{

	// Helper methods
	private HantoCoordinate Coord(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	private static HantoGameFactory factory;
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, HantoPlayerColor.BLUE);
	}
	
	//=============================================================================================
	// Basic moves
	
	@Test	// 1
	public void blueMakesValidFirstMove() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		final HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
	}
	
	@Test	// 2
	public void redMakesValidFirstMove() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(1, 0));
		final HantoPiece p = game.getPieceAt(Coord(1, 0));
		assertEquals(HantoPlayerColor.RED, p.getColor());
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class)	// 3
	public void redMovesOnTopOfBlue() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
	}
	
	@Test(expected = HantoException.class)	// 4
	public void redMovesNonAdjacent() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(2, 2));
	}
	
	
	//=============================================================================================
	// Invalid moves
	
	@Test(expected = HantoException.class)	// 5
	public void blueMakesInvalidFirstMove() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1));
	}
	
	@Test(expected = HantoException.class)	// 6
	public void blueTriesToPlaceNonButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.CRANE, null, new TestHantoCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class)	// 7
	public void redTriesToPlaceCrab() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.CRAB, null, new TestHantoCoordinate(1, 0));
	}
	
	@Test	// 8
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		assertEquals(MoveResult.OK, mr);
		final HantoPiece p = game.getPieceAt(Coord(0, 0));
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class)	//9
	public void placeTooManyButterflies() throws HantoException 
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, Coord(1, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(2, 0)); // second butterfly
	}
	
	@Test(expected = HantoException.class)	// 10
	public void neverPlaceButterfly() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(1, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(2, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(3, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(4, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(5, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(6, 0)));
		game.makeMove(HantoPieceType.SPARROW, null, Coord(7, 0));
	}

	@Test(expected = HantoException.class)	// 11
	public void moveOtherPlayerPiece() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, Coord(0, 0), new TestHantoCoordinate(1, 0));
	}
	
	//=============================================================================================
	// Game ending
	
	@Test	// 12
	public void boringDraw() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(1, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(2, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(3, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(4, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(5, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(6, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(7, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(8, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(9, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(10, 0)));
		assertEquals(MoveResult.DRAW, game.makeMove(HantoPieceType.SPARROW, null, Coord(11, 0)));
	}
	

	
	//=============================================================================================
	// Unit tests
	
	@Test // 16
	public void checkGetPrintableBoard() throws HantoException{
		assertNotNull(game.getPrintableBoard());
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertNotNull(game.getPrintableBoard());
	}
	
		
	//=============================================================================================
	// Tests that are wrong because they misintreprt the meaning of "Butterfly surrounded"
	
	//@Test(expected = HantoException.class)	// 17
	public void moveAfterGameOver() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(1, -1)));
		assertEquals(MoveResult.RED_WINS, game.makeMove(HantoPieceType.SPARROW, null, Coord(-1, 0)));	
		game.makeMove(HantoPieceType.SPARROW, null, Coord(-1, 1));
	}
	
	//@Test	// 13
	public void blueWins() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(0, 2)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(1, 0)));
		assertEquals(MoveResult.BLUE_WINS, game.makeMove(HantoPieceType.SPARROW, null, Coord(-1, 1)));	
	}

	//@Test	// 14
	public void redWins() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(1, -1)));
		assertEquals(MoveResult.RED_WINS, game.makeMove(HantoPieceType.SPARROW, null, Coord(-1, 0)));	
	}
	
	//@Test	// 15
	public void simultaneousVictory() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 0)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.BUTTERFLY, null, Coord(0, 1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(0, -1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(-1, 1)));
		assertEquals(MoveResult.OK, game.makeMove(HantoPieceType.SPARROW, null, Coord(0, 2)));
		assertEquals(MoveResult.DRAW, game.makeMove(HantoPieceType.SPARROW, null, Coord(1, 0)));
	}
	
	@Test(expected = HantoException.class)
	public void resignation_doesNotWork() throws HantoException
	{
		assertEquals(MoveResult.OK, game.makeMove(null, null, null));
	}
}
