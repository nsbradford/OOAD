/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.HantoCoordinateImpl;
import hanto.studentnsbradford.common.HantoPieceImpl;

/**
 * Using the Strategy pattern to create an easily extendable set of move rules.
 * @author Nicholas
 *
 */
public abstract class MoveValidator {
	
	/**
	 * Validate a move according to a set of rules.
	 * @param pieceType the type of piece
	 * @param color the color of the piece
	 * @param board the set of occupied hexes
	 * @param source the origin of the piece (can be null for Placements)
	 * @param destination the hex where the piece should finish the move
	 * @return true if the move is valid
	 * @throws HantoException
	 */
	public abstract boolean isValidMove(HantoPieceType pieceType, HantoPlayerColor color, 
			Map<HantoCoordinateImpl, HantoPiece> board, 
			HantoCoordinateImpl source, HantoCoordinateImpl destination) throws HantoException;
	
	//=========================================================================================
	// Common helpers
		
	
	/**
	 * 
	 * @param pieceType
	 * @param color
	 * @param board
	 * @param source
	 * @param destination
	 * @return true if standard validation passes
	 */
	protected static boolean isValidStandardMove(HantoPieceType pieceType, HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board,
			HantoCoordinateImpl source, HantoCoordinateImpl destination)
	{
		final Map<HantoCoordinateImpl, HantoPiece> finalBoard = 
				new HashMap<HantoCoordinateImpl, HantoPiece>(board);
		finalBoard.remove(source);
		finalBoard.put(destination, new HantoPieceImpl(color, pieceType));
		
		return !(	source == null ||
					!board.containsKey(source) ||
					board.containsKey(destination) ||
					board.get(source).getColor() != color ||
					board.get(source).getType() != pieceType ||
					!boardContainsPlayerButterfly(color, board) ||
					!isContinuousConfiguration(finalBoard.keySet()));
	}
	
	/**
	 * Check if the board contains a butterfly of the player's color.
	 * @param color
	 * @param board
	 * @return true if condition is met
	 */
	public static boolean boardContainsPlayerButterfly(HantoPlayerColor color,
			Map<HantoCoordinateImpl, HantoPiece> board ){
		boolean answer = false;
		for (HantoPiece piece : board.values()){
			if (piece.getColor() == color && piece.getType() == HantoPieceType.BUTTERFLY){
				answer = true;
				break;
			}
		}
		return answer;
	}
	
	//=========================================================================================
	// Contiguous board configuration
	
	/**
	 * Check that the board is in a single contiguous configuration using a DFS
	 * @param board
	 * @return true if condition is met
	 */
	public static boolean isContinuousConfiguration(Set<HantoCoordinateImpl> board)
	{
		if (board.size() == 0){
			return false;
		}
		
		// initialize stack and map graph of visits
		final HantoCoordinateImpl[] graphArray = 
				board.toArray(new HantoCoordinateImpl[board.size()]);
		final Map<HantoCoordinateImpl, Boolean> graph = 
				new HashMap<HantoCoordinateImpl, Boolean>();
		for (HantoCoordinateImpl hex : board){
			graph.put(hex, false);
		}
		
		// check that every node has been traversed
		final int traversedNodes = dfs(graphArray[0], graph);
		return traversedNodes == board.size();
	}
	
	/**
	 * Use DFS to count the number of traversed nodes
	 * @return number of traversedNodes
	 */
	private static int dfs(HantoCoordinateImpl root, Map<HantoCoordinateImpl, Boolean> graph)
	{
		final Stack<HantoCoordinateImpl> stack = new Stack<HantoCoordinateImpl>();
		stack.push(root);
		graph.put(root, true); // mark root as visited
		int traversedNodes = 0;
		
		while (!stack.isEmpty()){
			HantoCoordinateImpl currentNode = stack.peek();
			HantoCoordinateImpl child = getUnvisitedChildNode(currentNode, graph);
			if (child != null){
				graph.put(child, true); // mark child as visited
				stack.push(child);
			}
			else {
				stack.pop();
				traversedNodes++;
			}
		}
		
		return traversedNodes;
	}
	
	/**
	 * Helper for the DFS performed by isContinuousConfiguration()
	 * @param parent
	 * @param graph
	 * @return any unvisited child node in the graph
	 */
	private static HantoCoordinateImpl getUnvisitedChildNode(HantoCoordinateImpl parent,
			Map<HantoCoordinateImpl, Boolean> graph)
	{
		final Set<HantoCoordinateImpl> adjacent = parent.getAdjacentHexes();
		adjacent.retainAll(graph.keySet()); // reduce list to only occupied hexes
		for (HantoCoordinateImpl hex : adjacent){
			// find any child that has not been visited
			if (!graph.get(hex)){
				return hex;
			}
		}
		return null; // return null if no unvisited children were found
	}
}
