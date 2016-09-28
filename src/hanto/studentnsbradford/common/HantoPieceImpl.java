/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hanto.common.*;
import hanto.studentnsbradford.common.movement.MoveValidator;
import hanto.studentnsbradford.common.movement.PlacementMoveValidator;

/**
 * Implementation of the HantoPiece.
 * @version Mar 2,2016
 */
public class HantoPieceImpl implements HantoPiece
{
	private final HantoPlayerColor color;
	private final HantoPieceType type;
	private final Set<MoveValidator> moveValidators;
	
	/**
	 * Constructor, setting the piece's MoveValidators to the empty set.
	 * @param color
	 * @param type
	 */
	public HantoPieceImpl(HantoPlayerColor color, HantoPieceType type){
		this(color, type, new HashSet<MoveValidator>());
	}
	
	/**
	 * 
	 * @param color
	 * @param type
	 * @param moveValidators the Set of MoveValidators for this piece
	 */
	public HantoPieceImpl(HantoPlayerColor color, HantoPieceType type,
			Set<MoveValidator> moveValidators)
	{
		this.color = color;
		this.type = type;
		this.moveValidators = new HashSet<MoveValidator>();
		this.moveValidators.add(new PlacementMoveValidator());
		this.moveValidators.addAll(moveValidators);
	}
		
	@Override
	public String toString() {
		return "Piece[color=" + color + ", type=" + type + "]";
	}

	/*
	 * @see hanto.common.HantoPiece#getColor()
	 */
	@Override
	public HantoPlayerColor getColor()
	{
		return color;
	}

	/*
	 * @see hanto.common.HantoPiece#getType()
	 */
	@Override
	public HantoPieceType getType()
	{
		return type;
	}

	/**
	 * Iterate through the list of validators for this kind of piece.
	 * @param board the set of occupied hexes.
	 * @param source the source hex
	 * @param destination the destination hex
	 * @throws HantoException
	 */
	public void validateMove(Map<HantoCoordinateImpl, HantoPiece> board, HantoCoordinateImpl source,
			HantoCoordinateImpl destination) throws HantoException 
	{
		boolean isValid = false;
		for (MoveValidator validator : moveValidators){
			if (validator.isValidMove(type, color, board, source, destination)){
				isValid = true;
				break;
			}
		}
		if (!isValid){
			throw new HantoException(BaseHantoGame.messageInvalidMove);
		}
	}
}
