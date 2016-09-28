/**
 * @author Nicholas
 */
package hanto.studentnsbradford.common;

import java.util.HashSet;
import java.util.Set;

import hanto.common.HantoCoordinate;

/**
 * The implementation for my version of Hanto.
 * @version Mar 2, 2016
 */
public class HantoCoordinateImpl implements HantoCoordinate
{
	private final int x, y;
	
	/**
	 * The only constructor.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public HantoCoordinateImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor that creates an instance of HantoCoordinateImpl from an
	 * object that implements HantoCoordinate.
	 * @param coordinate an object that implements the HantoCoordinate interface.
	 */
	public HantoCoordinateImpl(HantoCoordinate coordinate)
	{
		this(coordinate.getX(), coordinate.getY());
	}
	
	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getY()
	{
		return y;
	}
	
	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HantoCoordinateImpl)) {
			return false;
		}
		final HantoCoordinateImpl other = (HantoCoordinateImpl) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}
	
	//=========================================================================================
	// Distance and Direction
	
	/**
	 * Get the distance between two coordinates on the hex grid.
	 * Use explanation for turning hex into 3D from:
	 * 	http://keekerdc.com/2011/03/hexagon-grids-coordinate-systems-and-distance-calculations/
	 * @param destination
	 * @return an int for the distance
	 */
	public int hexDistanceTo(HantoCoordinateImpl destination) {		
		int z = Math.abs(0 - x - y);
		int zDestination = Math.abs(0 - destination.getX() - destination.getY());
		int dx = Math.abs(destination.getX() - x);
		int dy = Math.abs(destination.getY() - y);
		int dz = Math.abs(zDestination - z);
		int answer = Math.max(Math.max(dx, dy), dz);
		return answer;
	}
	
	/**
	 * 
	 * @param destination
	 * @return the HantoDirection of the destination
	 */
	public HantoDirection getDirectionTo(HantoCoordinateImpl destination){
		HantoDirection answer = null;
		int z = 0 - x - y;
		int zDestination = 0 - destination.getX() - destination.getY();
		int dx = destination.getX() - x;
		int dy = destination.getY() - y;
		int dz = zDestination - z;
		
		if (dz == 0){	
			if (dx > 0){
				answer = HantoDirection.SE; // (1, -1)
			}
			else {
				answer = HantoDirection.NW; // (-1, 1)
			}
		}
		else if (dy == 0){
			if (dx > 0){
				answer = HantoDirection.NE; // (1, 0)
			}
			else {
				answer = HantoDirection.SW; // (-1, 0)
			}
		}
		else if (dx == 0){
			if (dy > 0){
				answer = HantoDirection.N; // (0, 1)
			}
			else {
				answer = HantoDirection.S; // (0, -1)
			}
		}
		return answer;
	}
	
	//=========================================================================================
	// Adjacency
		
	/**
	 * Check if two coordinates are adjacent.
	 * @param coord1 the first coordinate
	 * @param coord2 the second coordinate
	 * @return true if the two coordinates are adjacent
	 */
	public static boolean isAdjacentToHex(HantoCoordinate coord1, 
			HantoCoordinate coord2){
		final HantoCoordinateImpl hex1 = new HantoCoordinateImpl(coord1);
		final HantoCoordinateImpl hex2 = new HantoCoordinateImpl(coord2);
		return hex1.getAdjacentHexes().contains(hex2);
	}
	

	/**
	 * Uses GammaHantoGame.isAdjacentToOccupiedHex() with the game board.
	 * Practically used when makeMove() is trying to determine if the move is valid.
	 * @param occupiedHexes
	 * @return true if the location is a valid adjacent location.
	 */
	public boolean isAdjacentToOccupiedHex(Set<HantoCoordinateImpl> occupiedHexes)
	{
		final Set<HantoCoordinateImpl> adjacencySet = new HashSet<HantoCoordinateImpl>();
		for (HantoCoordinateImpl hex : occupiedHexes){
			adjacencySet.addAll(hex.getAdjacentHexes());
		}
		adjacencySet.removeAll(occupiedHexes);
		return adjacencySet.contains(this);
	}
	
	/**
	 * Get a List of adjacent coordinates.
	 * @return a List of adjacent HantoCoordinates
	 */
	public Set<HantoCoordinateImpl> getAdjacentHexes(){
		final Set<HantoCoordinateImpl> answer = new HashSet<HantoCoordinateImpl>();
		
		// Start at 12 o' clock and work clockwise
		answer.add(new HantoCoordinateImpl(x, y + 1));		// (0, 1)
		answer.add(new HantoCoordinateImpl(x + 1, y));		// (1, 0)
		answer.add(new HantoCoordinateImpl(x + 1, y - 1)); 	// (1, -1)
		answer.add(new HantoCoordinateImpl(x, y - 1));		// (0, -1)
		answer.add(new HantoCoordinateImpl(x - 1, y));		// (-1, 0)
		answer.add(new HantoCoordinateImpl(x - 1, y + 1));	// (-1, 1)
		return answer;
	}

	/**
	 * Get the set of all possible valid destinations for a move
	 * @param board
	 * @return the set of all adjacent hexes to a board's pieces
	 */
	public static Set<HantoCoordinateImpl> getAllAdjacentHexes(Set<HantoCoordinateImpl> board){
		Set<HantoCoordinateImpl> answer = new HashSet<HantoCoordinateImpl>();
		for (HantoCoordinateImpl hex : board){
			answer.addAll(hex.getAdjacentHexes()); // Sets automatically take care of duplicates
		}
		answer.removeAll(board);		
		return answer;
	}
	
}
