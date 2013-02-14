package concurrency.project.ca.cellsandgrids;

import java.util.ArrayList;
import java.util.List;

/**
 * The Runnable object that is responsible to update the {@link Grid} 
 * {@link Cell}s with the next state.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class SubGridCalculation extends Thread {
	
	// The Grid
	private Grid grid = null;
	// The new states for each Grid component (Cell)
	private int[][] newGrid = null;
	// The coordinates of the Cells composing this SubGrid
	private List<Coordinate> coordinates;
	public boolean active = true;
	private int[] queue;
	private boolean computing = true;
	
	
	/**
	 * The SubGrid constructor.
	 * @param coordinates the list of cells composing this subgrid
	 * @param grid the Grid object, father of this subgrid
	 * @param newGrid the reference to the array holding the new Cells state
	 */
	public SubGridCalculation(List<Coordinate> coordinates,
						Grid grid, int[][] newGrid, int[] queue) {
		this.grid = grid;
		this.newGrid = newGrid;
		this.queue = queue;
		List<Coordinate> clone = 
				new ArrayList<Coordinate>(coordinates.size());
	    for(Coordinate item: coordinates) clone.add(new Coordinate(item));
		this.coordinates = clone;
	}

	@Override
	public void run() {
		while (active) {
			if (getComputing()) {
				for (Coordinate coord : coordinates) {
					Cell cell = (Cell) grid.getObjectAt(coord);
					newGrid[coord.getX()][coord.getY()] = cell.getNextState();
				}
				synchronized (queue) {
					queue[0] -= 1;
				}
				setComputing(false);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void setComputing(boolean b) {
		this.computing = b;
	}
	
	public synchronized boolean getComputing() {
		return this.computing;
	}

	public void setNewStatesGrid(int[][] nextStates) {
		this.newGrid = nextStates;
	}
}
