/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.HantoPieceType.SPARROW;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import hanto.common.HantoPiece;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.studentnsbradford.common.HantoPieceImpl;

/**
 * @author Nicholas
 *
 */
public class MoveValidatorTest {

	private Map<HantoCoordinateImpl,HantoPiece> board;
	private final HantoPiece blueSparrow = new HantoPieceImpl(BLUE, SPARROW);
	private final HantoPiece redSparrow = new HantoPieceImpl(RED, SPARROW);
	
	@Before
	public void setup()
	{
		board = new HashMap<HantoCoordinateImpl, HantoPiece>();
	}

	//=============================================================================================
	// isContinuousConfiguration()
	
	@Test
	public void isContinuousConfiguration_invalidForEmptyBoard(){
		final Map<HantoCoordinateImpl,HantoPiece> board = new HashMap<HantoCoordinateImpl, HantoPiece>();
		assertFalse(MoveValidator.isContinuousConfiguration(board.keySet()));
	}
	
	@Test
	public void isContinuousConfiguration_validWithNullPieces(){
		board.put(new HantoCoordinateImpl(0, 0), null);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(1, 0), null);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(2, 0), null);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(3, 0), null);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
	}
	
	@Test
	public void isContinuousConfiguration_validIgnoringPieceColor(){
		board.put(new HantoCoordinateImpl(0, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(1, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(2, 0), redSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(3, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
	}
	
	@Test
	public void isContinuousConfiguration_invalidOnePieceSeparated(){
		board.put(new HantoCoordinateImpl(0, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(1, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(-2, 0), redSparrow);
		assertFalse(MoveValidator.isContinuousConfiguration(board.keySet()));
	}
	
	@Test
	public void isContinuousConfiguration_invalidTwoSeparateBigGroups(){
		board.put(new HantoCoordinateImpl(0, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(1, 0), blueSparrow);
		assertTrue(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(-2, 0), redSparrow);
		assertFalse(MoveValidator.isContinuousConfiguration(board.keySet()));
		board.put(new HantoCoordinateImpl(-3, 0), redSparrow);
		assertFalse(MoveValidator.isContinuousConfiguration(board.keySet()));
	}
	
	

}
