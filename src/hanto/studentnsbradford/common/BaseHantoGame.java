/**
 * 
 */
package hanto.studentnsbradford.common;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentnsbradford.common.movement.MoveValidator;

/**
 * @author Nicholas
 *
 */
public abstract class BaseHantoGame implements HantoGame {

	// Exception messages
	public static final String messageGameAlreadyOver = 
			"Cannot make any more moves once the game is over.";
	public static final String messageInvalidSource = 
			"Source HantoCoordinate should be null.";
	public static final String messageInvalidPieceType = 
			"Invalid piece type.";
	public static final String messageInvalidFirstMove = 
			"First move must be to (0,0).";
	public static final String messageWrongPlayerTurn = 
			"Incorrect player attempted a turn.";
	public static final String messageInvalidDestination = 
			"Must be placed on an open hex, adjacent to an occupied hex.";
	public static final String messageNoPiecesRemaining = 
			"No pieces of that kind remaining to place.";
	
	public static final String messageForgotButterfly = 
			"Each side must place their Butterfly on or before their 4th turn.";
	public static final String messageInvalidMove = "Invalid move.";
	public static final String messageNullDestination = "Cannot have a null move destination.";
	
	// constants
	public static final HantoCoordinate origin = new HantoCoordinateImpl(0, 0);
	protected HantoPieceType[] startingPieces;
	protected Map<HantoPieceType, Set <MoveValidator>> moveValidators;
	
	// private member variables
	protected boolean isResignationAllowed;
	protected boolean isGameOver;
	protected int numMoves;
	protected HantoPlayerColor activePlayerColor;
	protected Map<HantoCoordinateImpl, HantoPiece> board;
	protected Map<HantoPlayerColor, List<HantoPieceType>> playerInactivePieces;
	protected Map<HantoPlayerColor, HantoCoordinateImpl> playerButterflyLocations;
	
	protected int maxMovesInGame;
	protected int numMovesBeforeButterfly;
	
	/**
	 * Constructor for BaseHantoGame
	 * @param movesFirst
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst) {
		isResignationAllowed = true;
		isGameOver = false;
		numMoves = 0;
		activePlayerColor = movesFirst;
		board = new HashMap<HantoCoordinateImpl, HantoPiece>();
		playerButterflyLocations = new HashMap<HantoPlayerColor, HantoCoordinateImpl>();
		playerButterflyLocations.put(BLUE, null);
		playerButterflyLocations.put(RED, null);
		playerInactivePieces = new HashMap<HantoPlayerColor, List<HantoPieceType>>();
		moveValidators = new HashMap<HantoPieceType, Set<MoveValidator>>();
		for (HantoPieceType type : HantoPieceType.values()){
			moveValidators.put(type, new HashSet<MoveValidator>());
		}
	}

	//=============================================================================================
	// API

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, 
			HantoCoordinate to) throws HantoException
	{
		numMoves++;
		
		if (isResignationAllowed && !isGameOver && pieceType == null && from == null && to == null){
			isGameOver = true;
			return activePlayerColor == BLUE ? MoveResult.RED_WINS : MoveResult.BLUE_WINS;
		}
				
		HantoCoordinateImpl source = null;
		HantoCoordinateImpl destination = null;
		HantoPieceImpl piece = null;
		if (from != null){
			source = new HantoCoordinateImpl(from);
		}
		if (to == null){
			throw new HantoException(messageNullDestination);
		}
		else {
			destination = new HantoCoordinateImpl(to);
		}
		if (pieceType != null){
			piece = new HantoPieceImpl(activePlayerColor, pieceType, moveValidators.get(pieceType));
		}
		else {
			throw new HantoException(messageInvalidMove);
		}
		
		standardValidation(piece, source, destination);
		validateMove(piece, source, destination);
		updateBoard(piece, source, destination);
		updateActivePlayerColor();
		return getVictoryStatus();
	}
	
	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where)
	{
		final HantoCoordinateImpl location = new HantoCoordinateImpl(where);
		return board.get(location);
	}

	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard()
	{
		final StringBuilder builder = new StringBuilder("Board: \n");
		for (HantoCoordinateImpl hex : board.keySet()){
			builder.append(hex.toString());
			builder.append('\n');
		}
		return builder.toString();
	}
	
	//=============================================================================================
	// Non-static helper functions
	
	/**
	 * alter the board and the set of remaining pieces to place
	 * @param piece
	 * @param source
	 * @param destination
	 */
	protected void updateBoard(HantoPieceImpl piece, HantoCoordinateImpl source,
			HantoCoordinateImpl destination)
	{
		if (source == null){
			playerInactivePieces.get(activePlayerColor).remove(piece.getType());
		}
		else{
			board.remove(source);
		}
		board.put(destination, piece);
		
		if (piece.getType() == HantoPieceType.BUTTERFLY){
			playerButterflyLocations.put(activePlayerColor, destination);
		}
	}
	

	
	//=============================================================================================
	// Validation
	
	/**
	 * 
	 * @param piece
	 * @param source
	 * @param destination
	 * @throws HantoException
	 */
	protected void validateMove(HantoPieceImpl piece, HantoCoordinateImpl source,
			HantoCoordinateImpl destination) throws HantoException 
	{
		piece.validateMove(board, source, destination);
	}
	
	/**
	 * 
	 * @param piece
	 * @param source
	 * @param destination
	 * @throws HantoException
	 */
	protected void standardValidation(HantoPieceImpl piece, HantoCoordinateImpl source,
			HantoCoordinateImpl destination) throws HantoException 
	{
		if (isGameOver){
			throw new HantoException(messageGameAlreadyOver);
		}
		else if (!isValidPieceType(piece.getType())){
			throw new HantoException(messageInvalidPieceType);
		}
		else if (source == null && 
				!playerInactivePieces.get(activePlayerColor).contains(piece.getType()))
		{
			throw new HantoException(messageNoPiecesRemaining);
		}
		else if (numMoves == 1 && !destination.equals(origin)){
			throw new HantoException(messageInvalidFirstMove);
		}
		else if (
			numMoves > numMovesBeforeButterfly && 
			piece.getType() != HantoPieceType.BUTTERFLY &&
			playerInactivePieces.get(activePlayerColor).contains(HantoPieceType.BUTTERFLY))
		{
			throw new HantoException(messageForgotButterfly);
		}
		else if (board.containsKey(destination)){
			throw new HantoException(messageInvalidDestination);
		}
	}
	
	/**
	 * Check if piece is a BUTTERFLY or SPARROW.
	 * @param pieceType
	 * @return true if piece type is valid.
	 */
	protected boolean isValidPieceType(HantoPieceType pieceType){
		return Arrays.asList(startingPieces).contains(pieceType);
	}
	
	//=============================================================================================
	// Non-static helper functions
	
	/**
	 * Switch the active Player.
	 */
	protected void updateActivePlayerColor(){
		if (activePlayerColor == BLUE){
			activePlayerColor = RED;
		}
		else if (activePlayerColor == RED){
			activePlayerColor = BLUE;
		}
	}
	
	
	/**
	 * Called after every move to test the win conditions.
	 * @return the result of the most recent move
	 */
	protected MoveResult getVictoryStatus()
	{
		MoveResult answer = MoveResult.OK;
		final boolean blueVictory = isButterflySurrounded(RED);
		final boolean redVictory = isButterflySurrounded(BLUE);
		if (blueVictory && redVictory){
			answer = MoveResult.DRAW;
			isGameOver = true;
		}
		else if (blueVictory){
			answer = MoveResult.BLUE_WINS;
			isGameOver = true;
		}
		else if (redVictory){
			answer = MoveResult.RED_WINS;
			isGameOver = true;
		}
		else if (numMoves >= maxMovesInGame){
			answer = MoveResult.DRAW;
			isGameOver = true;
		}
		return answer;
	}
	
	/**
	 * Check if a given player's butterfly is surrounded.
	 * If so, they lose the game.
	 * @param playerColor the player to check.
	 * @return true if a player's butterfly is surrounded.
	 */
	protected boolean isButterflySurrounded(HantoPlayerColor playerColor){
		final HantoCoordinateImpl butterflyLoc = playerButterflyLocations.get(playerColor);
		if (butterflyLoc == null){
			return false;
		}
		final Set<HantoCoordinateImpl> surroundingHexes = butterflyLoc.getAdjacentHexes();
		
		// reduce list to only unoccupied hexes
		surroundingHexes.removeAll(board.keySet());
		
		return surroundingHexes.size() > 0 ? false : true;
	}
}
