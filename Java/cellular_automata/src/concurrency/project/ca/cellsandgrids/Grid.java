package concurrency.project.ca.cellsandgrids;

/**
 * The Grid, composed by {@link Cell} objects, with methods to manipulate and
 * get informations.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class Grid {

	/**
	 * Number of rows and columns
	 */
	protected int maxX;
	protected int maxY;
	
	// Number of threads used to calculated the cells state
	protected int threadsNum;
	
	// Is the number of threads changed?
	protected boolean threadsNumberChanged;

	// The real net of cells
	Cell[][] absAutomataObjects;

	/**
	 * Constructor. The specified parameters are the number of rows and
	 * the number of columns.
	 * @param p_x_max
	 * @param p_y_max
	 */
	Grid(int p_x_max, int p_y_max) {
		maxX = p_x_max;
		maxY = p_y_max;
		absAutomataObjects = new Cell[maxX + 1][maxY + 1];
	}
	
	/**
	 * Set the number of columns
	 * @param x new number of columns
	 */
	private void setX(int x) {
		maxX = x;
	}

	/**
	 * Set the number of rows
	 * @param y new number of rows
	 */
	private void setY(int y) {
		maxY = y;
	}
	
	/**
	 * Update the grid size.
	 * @param newX the new maxX
	 * @param newY the new maxY
	 */
	public void updateSize(int newX, int newY) {
		setX(newX);
		setY(newY);
		absAutomataObjects = new Cell[maxX + 1][maxY + 1];
	}
	
	/**
	 * 
	 * @return max Y
	 */
	public int maxX() {
		return maxX;
	}

	/**
	 * 
	 * @return max Y
	 */
	public int maxY() {
		return maxY;
	}
	
	/**
	 * How many threads to calculate the next state?
	 * @return the number of threads
	 */
	public int getThreadsNumber() {
		return this.threadsNum;
	}
	
	/**
	 * Set the number of threads to calculate the next state.
	 */
	public void setThreadsNumber(int n) {
		this.threadsNum = n;
		this.threadsNumberChanged = true;
	}

	/**
	 * Set the given {@link Cell} in the grid location identified by the
	 * first 2 parameters.
	 * @param x
	 * @param y
	 * @param newCell
	 */
	synchronized public void setObjectAt(int x, int y, Cell newCell) {
		absAutomataObjects[x][y] = newCell;
	} 

	/**
	 * Set the given {@link Cell} in the grid location identified by the
	 * {@link Coordinate} object.
	 * @param coord the Coordinate where to set the Cell
	 * @param newObject the Cell to set in the given position
	 */
	synchronized public void setObjectAt(Coordinate coord, Cell newObject) {
		setObjectAt(coord.getX(), coord.getY(), newObject);
	}

	/**
	 * Clear the grid.
	 */
	synchronized public void removeAllObjects() {
		for (int x = 0; x < maxX(); x++) {
			for (int y = 0; y <= maxY(); y++) {
				absAutomataObjects[x][y] = null;
			}
		}
	}

	/**
	 * Remove the object in the location specified by the coordinates.
	 * @param x
	 * @param y
	 */
	synchronized public void removeObjectAt(int x, int y) {
		absAutomataObjects[x][y] = null;
	}

	/**
	 * Remove the object in the location specified by the {@link Coordinate}
	 * object.
	 * @param c
	 */
	synchronized public void removeObjectAt(Coordinate c) {
		removeObjectAt(c.getX(), c.getY());
	}

	/**
	 * Is the location identified by the {@link Coordinate} object valid
	 * in the grid?
	 * @param c the Coordinate object.
	 * @return true if this is a valid location
	 */
	public boolean validLocation(Coordinate c) {
		return validLocation(c.getX(), c.getY());
	}

	/**
	 * Is the location identified by the 2 coordinates valid in the grid?
	 * @param x
	 * @param y
	 * @return true if this is a valid location
	 */
	public boolean validLocation(int x, int y) {
		return (x >= 0 && x < maxX()
					&& y >= 0 && y < maxY ());
	}

	/**
	 * Get and return the object present at the given location.
	 * @param x
	 * @param y
	 * @return the Cell in the given position
	 * @throws RuntimeException
	 */
	public Cell grabObjectAt(int x, int y) throws RuntimeException {
		if ((x < 0) || (x >= maxX())) { 
			throw new RuntimeException("Bad x parameter (getObjectAt): " + x);
		}

		if ((y < 0) || (y >= maxY())) {
			throw new RuntimeException("Bad y parameter (getObjectAt): " + y); 
		}

		Cell c = absAutomataObjects[x][y];
		return c;
	}

	/**
	 * Get and return the object present at the given location (synchronized).
	 * @param x
	 * @param y
	 * @return the Cell in the given position
	 * @throws RuntimeException
	 */
	synchronized public Cell getObjectAt(int x, int y)
									throws RuntimeException {
		return grabObjectAt(x, y);
	}

	/**
	 * Get and return the object present at the given location (synchronized).
	 * @param c
	 * @return the Cell in the given position
	 * @throws RuntimeException
	 */
	synchronized public Cell getObjectAt(Coordinate c)
									throws RuntimeException {
		return grabObjectAt(c.getX(), c.getY());
	}
}
