/**
 * @author Nicholas
 */
package hanto.studentnsbradford.tournament;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.HantoPieceType.BUTTERFLY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hanto.common.*;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.tournament.*;

/**
 * Description
 * @version Oct 13, 2014
 */
public class HantoPlayer implements HantoGamePlayer
{
	/**
	 * Allows a list of moves to be sorted by heuristic score.
	 * @author Nicholas
	 *
	 */
	public static class ComparableMove implements Comparator<ComparableMove>, 
			Comparable<ComparableMove>
	{
		HantoMoveRecord move;
		int score;
		
		/**
		 * A combination HantoMoveRecord and integer score that can be compared.
		 * @param move
		 * @param score
		 */
		public ComparableMove(HantoMoveRecord move, int score){
			this.move = move;
			this.score = score;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ComparableMove move) {
			return compare(this, move);
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ComparableMove m1, ComparableMove m2) {
			return Integer.compare(m1.score, m2.score);
		}
		
	}
	
	BaseHantoGame game;
	HantoPlayerColor myColor;

	/*
	 * @see hanto.tournament.HantoGamePlayer#startGame(hanto.common.HantoGameID, 
	 * hanto.common.HantoPlayerColor, boolean)
	 */
	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst)
	{
		//System.out.println("startGame");
		HantoGameFactory factory = HantoGameFactory.getInstance();
		HantoPlayerColor startingColor = null;
		if (doIMoveFirst){
			startingColor = myColor;
		}
		else {
			if (myColor == BLUE){
				startingColor = RED;
			}
			else if (myColor == RED){
				startingColor = BLUE;
			}
		}
		game = (BaseHantoGame) factory.makeHantoGame(version, startingColor);
		this.myColor = myColor;
	}

	/*
	 * @see hanto.tournament.HantoGamePlayer#makeMove(hanto.tournament.HantoMoveRecord)
	 */
	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove)
	{
		if (opponentsMove != null){
			try{
				game.makeMove(opponentsMove);
			}
			catch (HantoException e){
				System.out.println("HantoException occurred on opponent's move.");
			}
		}
		
		Set<HantoMoveRecord> validMoves = game.getValidMoves();
		HantoMoveRecord myMove = pickAMove(validMoves);
		try{
			game.makeMove(myMove);
		}
		catch (HantoException e){
			System.out.println("HantoException occurred on my move.");
		}
		return new HantoMoveRecord(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
	}
	
	
	/**
	 * Overload method allowing pickAMove() to be static.
	 * @param validMoves
	 * @return  the best available move
	 */
	protected HantoMoveRecord pickAMove(Set<HantoMoveRecord> validMoves){
		return pickAMove(validMoves, game, myColor);
	}
	
	/**
	 * Decide on the best avaiable valid move via heuristic.
	 * @param validMoves
	 * @param game
	 * @param myColor
	 * @return the best available move
	 */
	private static HantoMoveRecord pickAMove(Set<HantoMoveRecord> validMoves, BaseHantoGame game,
			HantoPlayerColor myColor)
	{
		List<HantoMoveRecord> moveList = new ArrayList<HantoMoveRecord>(validMoves);
		if (moveList.isEmpty()){
			return new HantoMoveRecord(null, null, null);
		}
		else {
			List<ComparableMove> comparableMoveList = scoreMoves(moveList, game, myColor);
			comparableMoveList.sort(null);
			return comparableMoveList.get(comparableMoveList.size() - 1).move;
		}
	}
	
	/**
	 * Assign a score to every move in the List
	 * @param moveList
	 * @param game
	 * @param myColor
	 * @return a list of (move, score) objects
	 */
	private static List<ComparableMove> scoreMoves(List<HantoMoveRecord> moveList, 
			BaseHantoGame game,
			HantoPlayerColor myColor)
	{
		List<ComparableMove> answer = new ArrayList<ComparableMove>();
		for (HantoMoveRecord move : moveList){
			answer.add(new ComparableMove(move, score(move, game, myColor)));
		}
		return answer;
	}
	
	/**
	 * We already know that the move is valid; assign it some heuristic value.
	 * @param move
	 * @param game
	 * @param myColor
	 * @return heuristic score of the move.
	 */
	private static int score(HantoMoveRecord move, BaseHantoGame game, HantoPlayerColor myColor){
		HantoPlayerColor badColor = myColor == BLUE ? RED : BLUE;
		Map<HantoCoordinateImpl, HantoPiece> board = game.getFutureBoard(move, myColor);
		HantoCoordinateImpl myButterfly = getButterflyCoord(myColor, board);
		HantoCoordinateImpl badButterfly = getButterflyCoord(badColor, board);
		
		int good = (badButterfly == null ? 0 : 
			BaseHantoGame.numSurroundingButterfly(badButterfly, badColor, board));
		int bad = (myButterfly == null ? 0 : 
			BaseHantoGame.numSurroundingButterfly(myButterfly, myColor, board));
		
		int answer = good - bad;
		return answer;
	}
	
	/**
	 * Find a player's butterfly on the board.
	 * @param color
	 * @param board
	 * @return the Butterfly hex
	 */
	private static HantoCoordinateImpl getButterflyCoord(HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board)
	{
		for (HantoCoordinateImpl hex : board.keySet()){
			if (board.get(hex).getType() == BUTTERFLY && board.get(hex).getColor() == color){
				return hex;
			}
		}
		return null;
	}

}
