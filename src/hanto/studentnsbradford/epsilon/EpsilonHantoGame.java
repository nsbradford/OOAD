/**
 * @author Nicholas
 */
package hanto.studentnsbradford.epsilon;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPieceType.HORSE;

import java.util.ArrayList;
import java.util.Arrays;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.movement.FlyMoveValidator;
import hanto.studentnsbradford.common.movement.JumpMoveValidator;
import hanto.studentnsbradford.common.movement.WalkMoveValidator;

/**
 * 
 * @author Nicholas
 *
 */
public class EpsilonHantoGame extends BaseHantoGame {

	/**
	 * The final version of Hanto, which will be used to play in the AI tournament.
	 * @param movesFirst
	 */
	public EpsilonHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		startingPieces = new HantoPieceType[] {
				HantoPieceType.BUTTERFLY,
				HantoPieceType.SPARROW,
				HantoPieceType.SPARROW,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.HORSE,
				HantoPieceType.HORSE,
				HantoPieceType.HORSE,
				HantoPieceType.HORSE
		};
		playerInactivePieces.put(HantoPlayerColor.BLUE, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces))); 
		playerInactivePieces.put(HantoPlayerColor.RED, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces)));
		
		moveValidators.get(BUTTERFLY).add(new WalkMoveValidator(1));
		moveValidators.get(SPARROW).add(new FlyMoveValidator(4));
		moveValidators.get(CRAB).add(new WalkMoveValidator(1));
		moveValidators.get(HORSE).add(new JumpMoveValidator());
	}

	
	/* (non-Javadoc)
	 * @see hanto.studentnsbradford.common.BaseHantoGame#isResignation(hanto.common.HantoPieceType,
	 * hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	protected boolean isResignation(HantoPieceType pieceType, HantoCoordinate from, 
			HantoCoordinate to) throws HantoException 
	{
		boolean answer = false;
		if (super.isResignation(pieceType, from, to)){
			if (!getValidMoves().isEmpty()){
				throw new HantoPrematureResignationException();
			}
			else {
				answer = true;
			}
		}
		return answer;
	}
	
}
