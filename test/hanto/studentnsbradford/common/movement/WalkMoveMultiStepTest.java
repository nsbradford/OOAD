/**
 * WalkMoveMultiStepTest.java
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.MoveResult.OK;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.studentnsbradford.common.movement.WalkMoveValidator.Node;

/**
 * @author Nicholas
 *
 */
public class WalkMoveMultiStepTest {

	// Helper methods
	private HantoCoordinateImpl Coord(int x, int y)
	{
		return new HantoCoordinateImpl(x, y);
	}
	
	//=============================================================================================
	// Setup
	
	//private Map<HantoCoordinateImpl, HantoPiece> board;
	private Map<HantoCoordinateImpl, Boolean> graph;
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
	
	private void testSetup_unit(){
		graph = new HashMap<HantoCoordinateImpl, Boolean>();
		graph.put(Coord(0,0), null);
		graph.put(Coord(1,0), null);
	}
	
	private void testSetup_bigWalk() throws HantoException{
		game.makeMove(BUTTERFLY, null, Coord(0, 0));	// turn 1
		game.makeMove(BUTTERFLY, null, Coord(-1, 1));
		game.makeMove(SPARROW, null, Coord(1, -1));		// turn 2
		game.makeMove(SPARROW, null, Coord(-1, 2));
		game.makeMove(SPARROW, null, Coord(2, -1));		// turn 3
		game.makeMove(SPARROW, null, Coord(-1, 3));
		game.makeMove(SPARROW, null, Coord(3, -1));		// turn 4
		game.makeMove(SPARROW, null, Coord(-1, 4));
		
	}
	
	//=============================================================================================
	// Unit tests
	
	@Test
	public void unit_getAllUnvisitedChildNodes() {
		testSetup_unit();
		Node parent = new Node(Coord(1, 0), 0);
		Set<Node> children = WalkMoveValidator.getAllUnvisitedChildNodes(parent, graph.keySet());
		assertTrue(children.contains(new Node(Coord(0, 1), 1)));
		assertTrue(children.contains(new Node(Coord(1, -1), 1)));
		assertTrue(children.size() == 2);
	}
	
	@Test
	public void unit_bfs() throws HantoException {
		testSetup_crabs();
		
	}
	
	//=============================================================================================
	// Valid walking
	
	@Test
	public void validMultiStepWalk_blueWalk3_delta() throws HantoException 
	{
		testSetup_crabs();
		assertEquals(OK, game.makeMove(CRAB, Coord(0, -1), Coord(-2, 1)));
	}
	
	@Test
	public void validMultiStepWalk_blueWalk2_delta() throws HantoException 
	{
		testSetup_crabs();
		assertEquals(OK, game.makeMove(CRAB, Coord(0, -1), Coord(-2, 0)));
	}

	//=============================================================================================
	// Invalid walking
	
	@Test
	public void invalidMultiStepWalk_tooFar() throws HantoException 
	{
		testSetup_crabs();
		try{
			game.makeMove(CRAB, Coord(0, -1), Coord(-1, 1));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidMultiStepWalk_veryNotContiguous() throws HantoException{
		testSetup_bigWalk();
		game.makeMove(CRAB, null, Coord(3, 0));			// turn 5
		game.makeMove(SPARROW, null, Coord(-1, 5));
		
		try{
			game.makeMove(CRAB, Coord(3, 0), Coord(0, 3));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
	
	@Test
	public void invalidMultiStepWalk_notContiguous() throws HantoException{
		testSetup_bigWalk();
		game.makeMove(CRAB, null, Coord(3, 0));			// turn 5
		game.makeMove(SPARROW, null, Coord(0, 2));
		
		try{
			game.makeMove(CRAB, Coord(3, 0), Coord(0, 3));
			assertTrue(messageNoException, false); // execution shouldn't reach here
		}
		catch (HantoException e){
			assertEquals(e.getMessage(), BaseHantoGame.messageInvalidMove);
		}
	}
}
