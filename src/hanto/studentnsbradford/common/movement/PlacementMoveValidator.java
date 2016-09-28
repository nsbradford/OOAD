/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.HantoCoordinateImpl;

/**
 * @author Nicholas
 * Validator for piece Placement.
 * Validate that source is null, destination is not occupied and not next to an adjacent hex 
 * of a different color, and destination is either the origin or an adjacent hex.
 */
public class PlacementMoveValidator extends MoveValidator {

	/* (non-Javadoc)
	 * @see hanto.studentnsbradford.common.MoveValidatorStrategy#validateMove(java.util.Map, 
	 * hanto.studentnsbradford.common.HantoCoordinateImpl, 
	 * hanto.studentnsbradford.common.HantoCoordinateImpl)
	 */
	@Override
	public boolean isValidMove(HantoPieceType pieceType, HantoPlayerColor color, 
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl source, HantoCoordinateImpl destination) 
	{
		final HantoCoordinate origin = new HantoCoordinateImpl(0, 0);
		return !(	source != null ||
					board.containsKey(destination) ||
					(board.size() > 1 && isAdjacentToOpposingPiece(color, board, destination)) ||
					!(destination.equals(origin) || 
							destination.isAdjacentToOccupiedHex(board.keySet())));
	}
	
	/**
	 * Check if a destination hex is adjacent to a piece of the opposing color
	 * @param color
	 * @param board
	 * @param destination
	 * @return true if condition is met
	 */
	private static boolean isAdjacentToOpposingPiece(HantoPlayerColor color, 
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl destination)
	{
		boolean answer = false;
		for (HantoCoordinateImpl hex : board.keySet()){
			if (board.get(hex).getColor() != color && 
					HantoCoordinateImpl.isAdjacentToHex(hex, destination))
			{
				answer = true;
				break;
			}
		}
		return answer;
	}

}
