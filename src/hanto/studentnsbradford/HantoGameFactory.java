/**
 * @author Nicholas
 */
package hanto.studentnsbradford;

import hanto.common.*;
import hanto.studentnsbradford.alpha.AlphaHantoGame;
import hanto.studentnsbradford.beta.BetaHantoGame;
import hanto.studentnsbradford.delta.DeltaHantoGame;
import hanto.studentnsbradford.epsilon.EpsilonHantoGame;
import hanto.studentnsbradford.gamma.GammaHantoGame;

/**
 * This is a singleton class that provides a factory to create an instance of any version
 * of a Hanto game.
 * 
 * @author gpollice
 * @version Feb 5, 2013
 */
public class HantoGameFactory
{
	private static final HantoGameFactory instance = new HantoGameFactory();
	
	/**
	 * Default private descriptor.
	 */
	private HantoGameFactory()
	{
		// Empty, but the private constructor is necessary for the singleton.
	}

	/**
	 * @return the instance
	 */
	public static HantoGameFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * Create the specified Hanto game version with the Blue player moving
	 * first.
	 * @param gameId the version desired.
	 * @return the game instance
	 */
	public HantoGame makeHantoGame(HantoGameID gameId)
	{
		return makeHantoGame(gameId, HantoPlayerColor.BLUE);
	}
	
	/**
	 * Factory method that returns the appropriately configured Hanto game.
	 * @param gameId the version desired.
	 * @param movesFirst the player color that moves first
	 * @return the game instance
	 */
	public HantoGame makeHantoGame(HantoGameID gameId, HantoPlayerColor movesFirst) {
		HantoGame game = null;
		switch (gameId) {
			case ALPHA_HANTO:
				game = new AlphaHantoGame();
				break;
			case BETA_HANTO:
				game = new BetaHantoGame(movesFirst);
				break;
			case GAMMA_HANTO:
				game = new GammaHantoGame(movesFirst);
				break;
			case DELTA_HANTO:
				game = new DeltaHantoGame(movesFirst);
				break;
			case EPSILON_HANTO:
				game = new EpsilonHantoGame(movesFirst);
			default:
				break;
		}
		return game;
	}
}
