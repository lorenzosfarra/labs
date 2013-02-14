package concurrency.project.ca.cellsandgrids;

import java.lang.String;

/**
 * Coordinate object: (x,y) and related operations
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class Coordinate {
	private int x;
	private int y;

	/**
	 * Create a Coordinate object with the given coordinates 
	 * @param p_x
	 * @param p_y
	 */
	Coordinate(int p_x, int p_y) {
		x = p_x;
		y = p_y;
	}
	
	/**
	 * Clone a Coordinate object
	 * @param c
	 */
	Coordinate(Coordinate c) {
		x = c.getX();
		y = c.getY();
	}

	/**
	 * Return the X
	 * @return int, x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the Y
	 * @return int, y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set the X coordinate
	 * @param p_x the X coordinate
	 */
	public void setX(int p_x) {
		x = p_x;
	}

	/**
	 * Set the Y coordinate
	 * @param p_y the Y coordinate
	 */
	public void setY(int p_y) {
		y = p_y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Checks if 2 Coordinate objects are equal
	 * @param c the object to compare
	 */
	public boolean equals(Object c) {
		if (c == null || !(c instanceof Coordinate)) {
			return false;
		} else {
			Coordinate b = (Coordinate) c;
			return (x == b.getX() && y == b.getY());
		}
	}
}
