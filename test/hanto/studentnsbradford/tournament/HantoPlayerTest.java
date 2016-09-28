/**
 * @author Nicholas
 */
package hanto.studentnsbradford.tournament;

import static org.junit.Assert.*;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import org.junit.Test;

import hanto.common.HantoGameID;
import hanto.common.MoveResult;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.HantoGameFactory;
import hanto.studentnsbradford.tournament.HantoPlayer;
import hanto.tournament.HantoGamePlayer;
import hanto.tournament.HantoMoveRecord;

/**
 * Make sure the Player plays a proper game.
 * Everything was confirmed by running the Ruby test scripts.
 * @author Nicholas
 *
 */
public class HantoPlayerTest {

	private MoveResult test4Turns(HantoGameID gameType, HantoPlayerColor goesFirst) throws HantoException{
		return this.testTurns_bothSmart(gameType, goesFirst, 4);
	}
	
	private MoveResult testLong(HantoGameID gameType, HantoPlayerColor goesFirst) throws HantoException{
		return this.testTurns_bothSmart(gameType, goesFirst, 50);
	}
	
	private MoveResult testLong_vsDumb(HantoGameID gameType, HantoPlayerColor goesFirst) throws HantoException{
		return this.testTurns_vsDumb(gameType, goesFirst, 50);
	}
	
//	private MoveResult testLong_vsExternal(HantoGameID gameType, HantoPlayerColor goesFirst) 
//			throws HantoException
//	{
//		return testTurns(gameType, goesFirst, 200, new hanto.studenttnarayan.tournament.HantoPlayer());
//	}
	
	private MoveResult testTurns_vsDumb(HantoGameID gameType, HantoPlayerColor goesFirst, int turns) 
			throws HantoException
	{
		return testTurns(gameType, goesFirst, turns, new DumbHantoPlayer());
	}
	
	private MoveResult testTurns_bothSmart(HantoGameID gameType, HantoPlayerColor goesFirst, int turns) 
			throws HantoException
	{
		return testTurns(gameType, goesFirst, turns, new HantoPlayer());
	}

	
	private MoveResult testTurns(HantoGameID gameType, HantoPlayerColor goesFirst, int turns,
			HantoGamePlayer player) throws HantoException
	{
		//System.out.println("BEGIN");
		HantoGameFactory factory = HantoGameFactory.getInstance();
		HantoGame game = factory.makeHantoGame(gameType, goesFirst);
		HantoGamePlayer player1 = new HantoPlayer();
		HantoGamePlayer player2 = player;
		player1.startGame(gameType, goesFirst, true);
		player2.startGame(gameType, goesFirst == BLUE ? RED : BLUE, false);
		
		HantoMoveRecord lastMove = null;
		MoveResult result = null;
		boolean flag_victory = false;
		int i;
		for (i = 0; i < turns; i++){
			lastMove = player1.makeMove(lastMove);
			result = game.makeMove(lastMove.getPiece(), lastMove.getFrom(), lastMove.getTo());
			if (result != MoveResult.OK){
				flag_victory = true;
				break;
			}
			lastMove = player2.makeMove(lastMove);
			result = game.makeMove(lastMove.getPiece(), lastMove.getFrom(), lastMove.getTo());
			if (result != MoveResult.OK){
				flag_victory = true;
				break;
			}
		}
		if (flag_victory){
			//System.out.println("GAME OVER turn " + i);
			//System.out.println(game.getPrintableBoard());
		}
		else {
			//System.out.println("END: Nobody won after " + turns + " turns.");
		}
		return result;
	}
	
	// We're mainly just concerned that no exceptions are thrown.
	// The long tests also try to make sure the player doesn't resign.
	// Finally, the test vs. a "Dumb" player tests to make sure that the player
	// 		shouldn't lose against a randomly moving opponent.
	// Everything was confirmed by running the Ruby test scripts. 
	
	@Test
	public void test_beta() throws HantoException {
		assertEquals(MoveResult.OK, test4Turns(HantoGameID.BETA_HANTO, BLUE));
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.BETA_HANTO, RED));
	}
	
	@Test
	public void test_gamma() throws HantoException {
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.GAMMA_HANTO, BLUE));
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.GAMMA_HANTO, RED));
	}
	
	@Test
	public void test_delta() throws HantoException {
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.DELTA_HANTO, BLUE));
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.DELTA_HANTO, RED));
	}
	
	@Test
	public void test_epsilon() throws HantoException {
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.EPSILON_HANTO, BLUE));
		assertEquals(MoveResult.OK,test4Turns(HantoGameID.EPSILON_HANTO, RED));
	}
	
	@Test // takes too long
	public void test_epsilon_long() throws HantoException {
		testLong(HantoGameID.EPSILON_HANTO, RED);
	}
	
	@Test // takes up to a minute
	public void test_epsilon_longVsDumb() throws HantoException {
		assertNotSame(MoveResult.RED_WINS, testLong_vsDumb(HantoGameID.EPSILON_HANTO, BLUE));
	}
	
	// can be used to test external JAR opponents.
	
//	@Test // takes up to a minute
//	public void test_epsilon_longVsExternal() throws HantoException {
//		MoveResult result = testLong_vsExternal(HantoGameID.EPSILON_HANTO, BLUE);
//		System.out.println((result == MoveResult.BLUE_WINS ? ":D BLUE_WINS " : ":( RED_WINS "));
//	}

}
