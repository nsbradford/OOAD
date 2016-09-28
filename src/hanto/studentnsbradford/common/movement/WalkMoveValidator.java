/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common.movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentnsbradford.common.HantoCoordinateImpl;

/**
 * @author Nicholas
 * Validate that a piece is being moved properly, especially that 
 * 		1) the piece has enough room to slide, and
 * 		2) the entire board remains contiguous afterwards.
 * Does not support walks of length greater than 1. 		 
 */
public class WalkMoveValidator extends MoveValidator {

	/**
	 * Helper class used for the BFS algorithm used to find valid walk paths.
	 * @author Nicholas
	 *
	 */
	public static class Node {
		
		HantoCoordinateImpl hex;
		int pathLength;
		
		/**
		 * Constructor.
		 * @param hex
		 * @param pathLength
		 */
		public Node(HantoCoordinateImpl hex, int pathLength){
			this.hex = hex;
			this.pathLength = pathLength;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((hex == null) ? 0 : hex.hashCode());
			result = prime * result + pathLength;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj){
				return true;
			}
			if (obj == null){
				return false;
			}
			if (getClass() != obj.getClass()){
				return false;
			}
			Node other = (Node) obj;
			if (hex == null) {
				if (other.hex != null){
					return false;
				}
			} 
			else if (!hex.equals(other.hex)){
				return false;
			}
			if (pathLength != other.pathLength){
				return false;
			}
				
			return true;
		}
	}
	
	
	int maxDistance;
	
	/**
	 * Constructor for WalkMoveValidator; default walking distance of 1.
	 */
	public WalkMoveValidator(){
		this(1);
	}
	
	/**
	 * 
	 * @param maxDistance
	 */
	public WalkMoveValidator(int maxDistance){
		this.maxDistance = maxDistance;
	}
	

	/* (non-Javadoc)
	 * @see hanto.studentnsbradford.common.MoveValidatorStrategy#validateMove(java.util.Map, 
	 * hanto.studentnsbradford.common.HantoCoordinateImpl, 
	 * hanto.studentnsbradford.common.HantoCoordinateImpl)
	 */
	@Override
	public boolean isValidMove(HantoPieceType pieceType, HantoPlayerColor color, 
			Map<HantoCoordinateImpl, HantoPiece> board,
			HantoCoordinateImpl source, HantoCoordinateImpl destination)
	{
		return (isValidStandardMove(pieceType, color, board, source, destination) &&
				isValidPath(board, source, destination, maxDistance));
	}


	/**
	 * Check if there is a valid path using a BFS with depth of maxDistance.
	 * @param board
	 * @param source
	 * @param destination
	 * @param maxDistance
	 * @return true if there is a valid path
	 */
	public static boolean isValidPath(Map<HantoCoordinateImpl, HantoPiece> board,
			HantoCoordinateImpl source, HantoCoordinateImpl destination,
			int maxDistance)
	{
		if (board.size() == 0){
			return false;
		}
		
		// initialize map graph of visits
		final Map<HantoCoordinateImpl, Boolean> graph = 
				new HashMap<HantoCoordinateImpl, Boolean>();
		for (HantoCoordinateImpl hex : board.keySet()){
			graph.put(hex, false);
		}

		final int minPathDistance = bfs(graph, source, destination, maxDistance);
		return minPathDistance <= maxDistance;
	}
	
	/**
	 * Perform a BFS to find the shortest path to the destination.
	 * @param graph
	 * @param source
	 * @param destination
	 * @param maxDepth
	 * @return the length of the shortest path, or the max integer if failed.
	 */
	public static int bfs(Map<HantoCoordinateImpl, Boolean> graph,
			HantoCoordinateImpl source, HantoCoordinateImpl destination,
			int maxDepth)
	{
		int answer = Integer.MAX_VALUE;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(new Node(source, 0));
		
		while (!queue.isEmpty()){
			Node current = queue.remove();
			Set<Node> children = getAllUnvisitedChildNodes(current, graph.keySet());
			
			for (Node child : children){
				if (child.pathLength > maxDepth){
					break;
				}
				else if (child.hex.equals(destination)){
					answer = child.pathLength;
					break;
				}
				else {
					graph.put(child.hex, true); // mark child as visited
					queue.add(child);
				}
			}
		}
		return answer;
	}
	
	/**
	 * Get any unvisited children that can be walked towards
	 * @param parent a Node
	 * @param graph set of visited and unvisited HantoCoordinateImpls
	 * @return a set of valid children
	 */
	public static Set<Node> getAllUnvisitedChildNodes(Node parent,
			Set<HantoCoordinateImpl> graph)
	{
		Set<HantoCoordinateImpl> tmpGraph = new HashSet<HantoCoordinateImpl>(graph);
		tmpGraph.remove(parent.hex);
		
		Set<Node> children = new HashSet<Node>();
		final Set<HantoCoordinateImpl> adjacent = parent.hex.getAdjacentHexes();
		adjacent.removeAll(graph); // reduce list to only unoccupied hexes
				
		for (HantoCoordinateImpl child : adjacent){
			
			if (child.isAdjacentToOccupiedHex(tmpGraph) && 
				isEnoughSpaceToSlide(tmpGraph, parent.hex, child))
			{
				// for testing, add the child and then remove it
				tmpGraph.add(child);
				if (MoveValidator.isContinuousConfiguration(tmpGraph)){
					children.add(new Node(child, parent.pathLength + 1));
				}
				tmpGraph.remove(child);
			}
			
		}
		return children;
	}
	
	/**
	 * Check if there is enough space to slide the piece from source to destination.
	 * Assume destination is adjacent.
	 * @param board
	 * @param source
	 * @param destination
	 * @return true if condition is met
	 */
	private static boolean isEnoughSpaceToSlide(Set<HantoCoordinateImpl> graph,
			HantoCoordinateImpl source, HantoCoordinateImpl destination)
	{
		// Intuition: find the intersection of source.adjacent and destination.adjacent
		
		final Set<HantoCoordinateImpl> sourceAdjacent = source.getAdjacentHexes();
		final Set<HantoCoordinateImpl> destAdjacent = destination.getAdjacentHexes();
		
		// reduce list to only unoccupied hexes
		sourceAdjacent.remove(destination);
		sourceAdjacent.removeAll(graph);
		destAdjacent.remove(source);
		destAdjacent.remove(graph);
		
		sourceAdjacent.retainAll(destAdjacent);
		return (sourceAdjacent.size() > 0);
	}

}
