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
 * @author Nicholas
 *
 */
public class JumpMoveValidator extends MoveValidator {



	/* (non-Javadoc)
	 * @see hanto.studentnsbradford.common.movement.MoveValidator#isValidMove(hanto.common.HantoPieceType, hanto.common.HantoPlayerColor, java.util.Map, hanto.studentnsbradford.common.HantoCoordinateImpl, hanto.studentnsbradford.common.HantoCoordinateImpl)
	 */
	@Override
	public boolean isValidMove(HantoPieceType pieceType, HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl source, HantoCoordinateImpl destination)
	{
		return (isValidStandardMove(pieceType, color, board, source, destination) &&
				!(source.hexDistanceTo(destination) < 2) &&
				isStraightLine(pieceType, color, board, source, destination));
	}
	
	/**+
	 * 
	 * @param pieceType
	 * @param color
	 * @param board
	 * @param source
	 * @param destination
	 * @return
	 */
	private static boolean isStraightLine(HantoPieceType pieceType, HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl source, HantoCoordinateImpl destination)
	{
		int x = source.getX();
		int y = source.getY();		
		int xInc = 0;
		int yInc = 0;
		if (source.getDirectionTo(destination) == null){
			return false;
		}
		switch (source.getDirectionTo(destination)){
			case N:
				yInc = 1;
				break;
			case NE:
				xInc = 1;
				break;
			case SE:
				xInc = 1;
				yInc = -1;
				break;
			case S:
				yInc = -1;
				break;
			case SW:
				xInc = -1;
				break;
			case NW:
				xInc = -1;
				yInc = 1;
				break;
			default:
				break;
		}
		while(board.get(new HantoCoordinateImpl(x, y)) != null){
			x += xInc;
			y += yInc;
		}
		return destination.equals(new HantoCoordinateImpl(x, y));
	}

}
