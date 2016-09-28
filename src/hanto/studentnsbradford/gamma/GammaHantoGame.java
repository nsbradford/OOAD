/**
 * @author Nicholas
 */
package hanto.studentnsbradford.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;

import java.util.ArrayList;
import java.util.Arrays;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.movement.WalkMoveValidator;

/**
 * @author Nicholas
 *
 */
public class GammaHantoGame extends BaseHantoGame {
		
	
	/**
	 * Constructor for GammaHantoGame with the ability to specify the player that moves first.
	 * @param movesFirst the HantoPlayerColor that moves first
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst){
		super(movesFirst);
		isResignationAllowed = false;
		maxMovesInGame = 40;
		startingPieces = new HantoPieceType[] {
				BUTTERFLY,
				SPARROW,
				SPARROW,
				SPARROW,
				SPARROW,
				SPARROW
		};
		
		playerInactivePieces.put(HantoPlayerColor.BLUE, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces))); 
		playerInactivePieces.put(HantoPlayerColor.RED, 
				new ArrayList<HantoPieceType>(Arrays.asList(startingPieces)));
		
		moveValidators.get(BUTTERFLY).add(new WalkMoveValidator());
		moveValidators.get(SPARROW).add(new WalkMoveValidator());
	}

}
