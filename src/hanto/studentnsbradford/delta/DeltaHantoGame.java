/**
 * 
 */
package hanto.studentnsbradford.delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPieceType.CRAB;

import java.util.ArrayList;
import java.util.Arrays;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.movement.FlyMoveValidator;
import hanto.studentnsbradford.common.movement.WalkMoveValidator;

/**
 * @author Nicholas
 *
 */
public class DeltaHantoGame extends BaseHantoGame {

	/**
	 * @param movesFirst
	 */
	public DeltaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		maxMovesInGame = Integer.MAX_VALUE; // "infinite"
		numMovesBeforeButterfly = 6;
		startingPieces = new HantoPieceType[] {
				HantoPieceType.BUTTERFLY,
				HantoPieceType.SPARROW,
				HantoPieceType.SPARROW,
				HantoPieceType.SPARROW,
				HantoPieceType.SPARROW,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB,
				HantoPieceType.CRAB
		};
		playerInactivePieces.put(HantoPlayerColor.BLUE, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces))); 
		playerInactivePieces.put(HantoPlayerColor.RED, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces)));
		
		moveValidators.get(BUTTERFLY).add(new WalkMoveValidator());
		moveValidators.get(SPARROW).add(new FlyMoveValidator());
		moveValidators.get(CRAB).add(new WalkMoveValidator(3));
	}

}
