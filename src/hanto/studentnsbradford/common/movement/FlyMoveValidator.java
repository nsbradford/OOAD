/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import java.util.Map;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.HantoCoordinateImpl;

/**
 * 
 * @author Nicholas
 * Validate that a piece is flying properly; simply has to keep the board contiguous.
 */
public class FlyMoveValidator extends MoveValidator {

	int maxDistance;
	
	/**
	 * Constructor for FlyMoveValidator.
	 * Sets maxDistance to Integer.MAX_VALUE
	 */
	public FlyMoveValidator(){
		this(Integer.MAX_VALUE);
	}
	
	/**
	 * Constructor for FlyMoveValidator
	 * @param maxDistance
	 */
	public FlyMoveValidator(int maxDistance){
		this.maxDistance = maxDistance;
	}
	
	/* (non-Javadoc)
	 * @see hanto.studentnsbradford.common.movement.MoveValidatorStrategy#isValidMove(hanto.common.HantoPieceType, 
	 * hanto.common.HantoPlayerColor, java.util.Map, hanto.studentnsbradford.common.HantoCoordinateImpl, 
	 * hanto.studentnsbradford.common.HantoCoordinateImpl)
	 */
	@Override
	public boolean isValidMove(HantoPieceType pieceType, HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl source, HantoCoordinateImpl destination) 
	{
		return (isValidStandardMove(pieceType, color, board, source, destination) &&
				maxDistance >= source.hexDistanceTo(destination));
	}
}
