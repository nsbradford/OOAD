/**
 * @author Nicholas
 */

package hanto.studentnsbradford.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hanto.tournament.HantoMoveRecord;

/**
 * A dumb player for testing against.
 * @author Nicholas
 *
 */
public class DumbHantoPlayer extends HantoPlayer {

	
	/**
	 * Decide on the best avaiable valid move via heuristic.
	 * @param validMoves
	 * @param game
	 * @param myColor
	 * @return the best available move
	 */
	@Override
	public HantoMoveRecord pickAMove(Set<HantoMoveRecord> validMoves)
	{
		List<HantoMoveRecord> moveList = new ArrayList<HantoMoveRecord>(validMoves);
		if (moveList.isEmpty()){
			return new HantoMoveRecord(null, null, null);
		}
		return moveList.get(0);
	}
}
