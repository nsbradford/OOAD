/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hanto.studentnsbradford.beta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;

import java.util.ArrayList;
import java.util.Arrays;

import hanto.common.*;
import hanto.studentnsbradford.common.BaseHantoGame;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.studentnsbradford.common.HantoPieceImpl;

/**
 * Basic Beta version of Hanto where there is no movement,
 * and each player gets 1 Butterfly and 5 Sparrows.
 * @version Mar 16, 2016
 */
public class BetaHantoGame extends BaseHantoGame
{
	
	/**
	 * Constructor for BetaHantoGame with the ability to specify the player that moves first.
	 * @param movesFirst the HantoPlayerColor that moves first
	 */
	public BetaHantoGame(HantoPlayerColor movesFirst){
		super(movesFirst);
		isResignationAllowed = false;
		maxMovesInGame = 12;
		numMovesBeforeButterfly = 6;
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
	}
	
	@Override
	protected void validateMove(HantoPieceImpl piece, HantoCoordinateImpl source,
			HantoCoordinateImpl destination) throws HantoException
	{
		super.standardValidation(piece, source, destination);
		if (!(source == null)){
			throw new HantoException(messageInvalidSource);
		}
		else if (numMoves != 1 && 
				!destination.isAdjacentToOccupiedHex(board.keySet()))
		{
			throw new HantoException(messageInvalidDestination);
		}
	}
}
