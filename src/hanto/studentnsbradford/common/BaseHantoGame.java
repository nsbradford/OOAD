/**
 * @author Nicholas
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
import hanto.tournament.HantoMoveRecord;

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
	public static final HantoCoordinateImpl origin = new HantoCoordinateImpl(0, 0);
		

	// constant once initialized and confirmed by the subclass
	protected HantoPieceType[] startingPieces;
	protected Map<HantoPieceType, Set <MoveValidator>> moveValidators;
	protected int maxMovesInGame;
	protected int numMovesBeforeButterfly;
	protected boolean isResignationAllowed;
	
	// private member variables
	protected boolean isGameOver;
	protected int numMoves;
	protected HantoPlayerColor activePlayerColor;
	protected Map<HantoCoordinateImpl, HantoPiece> board;
	protected Map<HantoPlayerColor, List<HantoPieceType>> playerInactivePieces;
	protected Map<HantoPlayerColor, HantoCoordinateImpl> playerButterflyLocations;
	
	/**
	 * Constructor for BaseHantoGame
	 * @param movesFirst
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst) {
		maxMovesInGame = Integer.MAX_VALUE; // "infinite"
		numMovesBeforeButterfly = 6;
		isResignationAllowed = true;
		isGameOver = false;
		numMoves = 1;
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

	/**
	 * Use the makeMove() from HantoGame
	 * @param move
	 * @return the result of the move
	 * @throws HantoException
	 */
	public MoveResult makeMove(HantoMoveRecord move) throws HantoException{
		return makeMove(move.getPiece(), move.getFrom(), move.getTo());
	}
	
	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, 
	 * hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, 
			HantoCoordinate to) throws HantoException
	{
		
		if (isResignation(pieceType, from, to)){
			isGameOver = true;
			return activePlayerColor == BLUE ? MoveResult.RED_WINS : MoveResult.BLUE_WINS;
		}
		checkNullParams(pieceType, to);
		
		HantoCoordinateImpl source = null;
		HantoCoordinateImpl destination = null;
		HantoPieceImpl piece = null;
		if (from != null){
			source = new HantoCoordinateImpl(from);
		}
		destination = new HantoCoordinateImpl(to);
		piece = new HantoPieceImpl(activePlayerColor, pieceType, moveValidators.get(pieceType));

		standardValidation(piece, source, destination);
		validateMoveForPiece(piece, source, destination);
		updateBoard(piece, source, destination);
		updateActivePlayerColor();
		numMoves++;
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
			builder.append(board.get(hex).toString());
			builder.append('\n');
		}
		return builder.toString();
	}
	
	//=============================================================================================
	// Validation
	
	/**
	 * Check if a move is valid.
	 * @param pieceType
	 * @param from
	 * @param to
	 * @return whether the given move is valid
	 */
	private boolean isValidMove(HantoPieceType pieceType, HantoCoordinate from, 
			HantoCoordinate to)
	{
		boolean answer = false;
		try {
			checkNullParams(pieceType, to);
			HantoCoordinateImpl source = null;
			HantoCoordinateImpl destination = null;
			HantoPieceImpl piece = null;
			if (from != null){
				source = new HantoCoordinateImpl(from);
			}
			destination = new HantoCoordinateImpl(to);
			piece = new HantoPieceImpl(activePlayerColor, pieceType, moveValidators.get(pieceType));

			standardValidation(piece, source, destination);
			validateMoveForPiece(piece, source, destination);
			answer = true;
		}
		catch (HantoException e){
			answer = false;
		}
		return answer;
	}
	
	/**
	 * 
	 * @param pieceType
	 * @param from
	 * @param to
	 * @return whether all params are null
	 * @throws HantoException
	 */
	protected boolean isResignation(HantoPieceType pieceType, HantoCoordinate from, 
			HantoCoordinate to) throws HantoException
	{
		boolean answer = false;
		if (isResignationAllowed && !isGameOver && pieceType == null && from == null && to == null)
		{
			answer = true;
		}
		return answer;
	}
	
	/**
	 * Check for null params
	 * @param pieceType
	 * @param to
	 * @throws HantoException
	 */
	private static void checkNullParams(HantoPieceType pieceType, HantoCoordinate to) 
			throws HantoException
	{
		if (to == null){
			throw new HantoException(messageNullDestination);
		}
		if (pieceType == null) {
			throw new HantoException(messageInvalidMove);
		}
	}
	
		
	
	/**
	 * 
	 * @param piece
	 * @param source
	 * @param destination
	 * @throws HantoException
	 */
	protected void validateMoveForPiece(HantoPieceImpl piece, HantoCoordinateImpl source,
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
		else if (numMoves > maxMovesInGame){
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
		return numSurroundingButterfly(butterflyLoc, playerColor, board) == 6 ? true : false;
	}
	
	/**
	 * Get the number of pieces surrounding a particular color's butterfly
	 * @param butterflyLoc
	 * @param playerColor
	 * @param board
	 * @return the number of surrounding pieces
	 */
	public static int numSurroundingButterfly(HantoCoordinateImpl butterflyLoc,
			HantoPlayerColor playerColor, Map<HantoCoordinateImpl, HantoPiece> board)
	{
		final Set<HantoCoordinateImpl> surroundingHexes = butterflyLoc.getAdjacentHexes();
		surroundingHexes.retainAll(board.keySet());
		return surroundingHexes.size();
	}
	
	//=============================================================================================
	// Getting possible moves
	
	/**
	 * Overload allowing a simpler call for this current game state.
	 * @return valid possibilites for the next move
	 */
	public Set<HantoMoveRecord> getValidMoves() {
		return getValidMoves(activePlayerColor, board, playerInactivePieces);
	}
	
	/**
	 * Get all possible valid moves for the current game state.
	 * @param color
	 * @param board
	 * @param playerInactivePieces
	 * @return valid possibilites for the next move
	 */
	public Set<HantoMoveRecord> getValidMoves(HantoPlayerColor color, 
			Map<HantoCoordinateImpl, HantoPiece> board, 
			Map<HantoPlayerColor, List<HantoPieceType>> playerInactivePieces)
	{
		// needs to store the piece type, and source hex
		Set<HantoMoveRecord> answer = new HashSet<HantoMoveRecord>();
		Set<HantoMoveRecord> potentialSources = new HashSet<HantoMoveRecord>();
		
		// potential movement
		for (HantoCoordinateImpl source : board.keySet()){
			if (board.get(source).getColor() == color){
				potentialSources.add(new HantoMoveRecord(board.get(source).getType(), source, null));
			}
		}
		// potential placement
		for (HantoPieceType pieceType : playerInactivePieces.get(color)){
			potentialSources.add(new HantoMoveRecord(pieceType, null, null));
		}
		
		// potential destinations
		Set<HantoCoordinateImpl> potentialDestinations = 
				HantoCoordinateImpl.getAllAdjacentHexes(board.keySet());
		if (board.isEmpty()){
			potentialDestinations.add(origin);
		}
		
		// now, check for all possible source-destination combinations
		for (HantoMoveRecord move : potentialSources){
			for (HantoCoordinateImpl destination : potentialDestinations){
				HantoMoveRecord potentialMove = new HantoMoveRecord(move.getPiece(), move.getFrom(), destination);
				if (isValidMove(move.getPiece(), move.getFrom(), destination)){
					answer.add(potentialMove); // needs piece, source, destination
				}
			}
		}
		return answer;	
	}
		
	//=============================================================================================
	// Help with player's predictions
	
	/**
	 * Execute a board on the given board.
	 * @param move
	 * @param myColor
	 * @return the future board state
	 */
	public Map<HantoCoordinateImpl, HantoPiece> getFutureBoard(HantoMoveRecord move, 
			HantoPlayerColor myColor)
	{
		Map<HantoCoordinateImpl, HantoPiece> futureBoard = 
				new HashMap<HantoCoordinateImpl, HantoPiece>(board);
		
		if (move.getFrom() != null){
			futureBoard.remove(move.getFrom());
		}
		futureBoard.put(new HantoCoordinateImpl(move.getTo()), 
				new HantoPieceImpl(myColor, move.getPiece()));
		return futureBoard;
	}
}
