/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2015 Gary F. Pollice
 *******************************************************************************/

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
	// Adjacent locations
	
	
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


	
}
