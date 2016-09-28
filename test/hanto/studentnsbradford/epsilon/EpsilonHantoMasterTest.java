/**
 * @author Nicholas
 */
package hanto.studentnsbradford.epsilon;

import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;
import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.HORSE;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.TestHantoCoordinate;
import hanto.tournament.HantoMoveRecord;

/**
 * 
 * @author Nicholas
 *
 */
public class EpsilonHantoMasterTest {

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
	
	private void testSetup_butterflies() throws HantoException{
		game.makeMove(BUTTERFLY, null, BaseHantoGame.origin);
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
	}
	
	private void testSetup_lineUp() throws HantoException {
		testSetup_butterflies();
		game.makeMove(CRAB, null, Coord(-1, 0));
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-2, 0));
		game.makeMove(CRAB, null, Coord(3, 0));
		game.makeMove(CRAB, null, Coord(-3, 0));
		game.makeMove(CRAB, null, Coord(4, 0));
		game.makeMove(CRAB, null, Coord(-4, 0));
		game.makeMove(CRAB, null, Coord(5, 0));
		game.makeMove(CRAB, null, Coord(-5, 0));
		game.makeMove(CRAB, null, Coord(6, 0));
		game.makeMove(HORSE, null, Coord(-6, 0));
		game.makeMove(HORSE, null, Coord(7, 0));
		game.makeMove(HORSE, null, Coord(-7, 0));
		game.makeMove(HORSE, null, Coord(8, 0));
		game.makeMove(HORSE, null, Coord(-8, 0));
		game.makeMove(HORSE, null, Coord(9, 0));
	}
	
	private void testSetup_lineUpSparrow() throws HantoException{
		testSetup_lineUp();
		game.makeMove(CRAB, null, Coord(-9, 0));
		game.makeMove(CRAB, null, Coord(10, 0));
		game.makeMove(HORSE, null, Coord(-10, 0));
		game.makeMove(HORSE, null, Coord(11, 0));
		game.makeMove(SPARROW, null, Coord(-11, 0));
		game.makeMove(SPARROW, null, Coord(12, 0));
	}
	
	private void testSetup_lineUpCrab() throws HantoException{
		testSetup_lineUp();
		game.makeMove(SPARROW, null, Coord(-9, 0));
		game.makeMove(SPARROW, null, Coord(10, 0));
		game.makeMove(HORSE, null, Coord(-10, 0));
		game.makeMove(HORSE, null, Coord(11, 0));
		game.makeMove(CRAB, null, Coord(-11, 0));
		game.makeMove(CRAB, null, Coord(12, 0));
	}
	
	private void testSetup_lineUpHorse() throws HantoException{
		testSetup_lineUp();
		game.makeMove(SPARROW, null, Coord(-9, 0));
		game.makeMove(SPARROW, null, Coord(10, 0));
		game.makeMove(CRAB, null, Coord(-10, 0));
		game.makeMove(CRAB, null, Coord(11, 0));
		game.makeMove(HORSE, null, Coord(-11, 0));
		game.makeMove(HORSE, null, Coord(12, 0));
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
	// Game doesn't end in a draw after 'X' number of turns (assume < Integer.MAX_VALUE)
	
	@Test	// 
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
	// Valid moves
	
	@Test
	public void validMoves_ButterflyMustBePlaced() throws HantoException{
		game.makeMove(CRAB, null, Coord(0, 0));	// turn 1
		game.makeMove(CRAB, null, Coord(1, 0));
		game.makeMove(CRAB, null, Coord(-1, 0));	// turn 2
		game.makeMove(CRAB, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-2, 0));	// turn 3
		game.makeMove(CRAB, null, Coord(3, 0));
		Set<HantoMoveRecord> moves = ((BaseHantoGame)game).getValidMoves();
		for (HantoMoveRecord move : moves){
			assertEquals(BUTTERFLY, move.getPiece());
		}
	}
	
	//=============================================================================================
	// Resignation
	
	@Test	// 
	public void validResignation_blueHasOnlyButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));			// turn 1
		game.makeMove(BUTTERFLY, null, Coord(0, 1));
		game.makeMove(BUTTERFLY, Coord(0, 0), Coord(-1, 1));	// turn 2
		game.makeMove(CRAB, null, Coord(1, 0));
		game.makeMove(BUTTERFLY, Coord(-1, 1), Coord(0, 0));	// turn 3
		game.makeMove(CRAB, null, Coord(-1, 2));
		game.makeMove(BUTTERFLY, Coord(0, 0), Coord(1, -1));	// turn 4
		game.makeMove(CRAB, Coord(-1, 2), Coord(-1, 1));
		game.makeMove(BUTTERFLY, Coord(1, -1), Coord(0, 0));	// turn 5
		game.makeMove(CRAB, Coord(1, 0), Coord(1, -1));
		
		assertEquals(RED_WINS, game.makeMove(null, null, null));
	}	
	
	@Test	// 
	public void invalidResignation_firstTurn() throws HantoException
	{
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	@Test	// 
	public void invalidResignation_redFirstTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));			// turn 1
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	@Test	// 
	public void invalidResignation_availablePlacement() throws HantoException
	{
		testSetup_butterflies();
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	@Test	// 
	public void invalidResignation_availableWalk() throws HantoException
	{
		testSetup_lineUpCrab();
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	@Test	// 
	public void invalidResignation_availableFly() throws HantoException
	{
		testSetup_lineUpSparrow();
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	@Test	// 
	public void invalidResignation_availableJump() throws HantoException
	{
		testSetup_lineUpHorse();
		try{
			game.makeMove(null, null, null);
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoPrematureResignationException e){
			// all set
		}
	}
	
	
	//=============================================================================================
	// Make sure all pieces have the correct moves assigned
	
	@Test
	public void pieceMove_butterflyWalk() throws HantoException{
		testSetup_butterflies();
		assertEquals(OK, game.makeMove(BUTTERFLY, BaseHantoGame.origin, Coord(0, 1)));
	}
	
	@Test
	public void pieceMove_butterflyNoWalk2() throws HantoException{
		game.makeMove(CRAB, null, BaseHantoGame.origin);
		game.makeMove(CRAB, null, Coord(1, 0));
		game.makeMove(BUTTERFLY, null, Coord(-1, 0));
		game.makeMove(BUTTERFLY, null, Coord(2, 0));
		
		try{
			game.makeMove(BUTTERFLY, Coord(-1, 0), Coord(0, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void pieceMove_sparrowFly4() throws HantoException 
	{
		testSetup_butterflies();
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, null, Coord(2, 0));
		assertEquals(OK, game.makeMove(SPARROW, Coord(-1, 0), Coord(3, 0)));
	}
	
	@Test
	public void pieceMove_sparrowNoFly5() throws HantoException 
	{
		testSetup_butterflies();
		game.makeMove(SPARROW, null, Coord(-1, 0));
		game.makeMove(SPARROW, null, Coord(2, 0));
		game.makeMove(CRAB, null, Coord(-2, 0));
		try{
			game.makeMove(SPARROW, Coord(2, 0), Coord(-3, 0));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void pieceMove_crabWalk1() throws HantoException{
		testSetup_butterflies();
		game.makeMove(CRAB, null, Coord(-1, 0));
		game.makeMove(CRAB, null, Coord(2, 0));
		assertEquals(OK, game.makeMove(CRAB, Coord(-1, 0), Coord(-1, 1)));
	}
	
	@Test
	public void pieceMove_crabNoWalk2() throws HantoException{
		testSetup_butterflies();
		game.makeMove(CRAB, null, Coord(-1, 0));
		game.makeMove(CRAB, null, Coord(2, 0));
		try{
			game.makeMove(CRAB, Coord(-1, 0), Coord(0, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void pieceMove_horseJump() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(HORSE, null, Coord(-1, 0));
		game.makeMove(HORSE, null, Coord(2, 0));
		game.makeMove(HORSE, Coord(-1, 0), Coord(3, 0));
		
		final HantoPiece p = game.getPieceAt(Coord(3, 0));
		assertNull(game.getPieceAt(Coord(-1, 0)));
		assertEquals(BLUE, p.getColor());
		assertEquals(HORSE, p.getType());
	}
	
	@Test
	public void invalidJump_adjacentHex() throws HantoException {
		game.makeMove(BUTTERFLY, null, Coord(0, 0));
		game.makeMove(BUTTERFLY, null, Coord(1, 0));
		game.makeMove(HORSE, null, Coord(-1, 0));
		game.makeMove(HORSE, null, Coord(2, 0));
		try{
			game.makeMove(HORSE, Coord(-1, 0), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
}
