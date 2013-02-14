package concurrency.project.ca.cellsandgrids;

import java.util.ArrayList;
import java.util.List;

/**
 * Add functionalities to the Grid.
 * It takes care to subdivide the Grid in logical parts (subgrids)
 * that will be maintained by different threads to calculate the state of the
 * contained cells.
 * 
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellGrid extends Grid {
	// List of threads that will calculate subgrids steps
    List<SubGridCalculation> subGrids = new ArrayList<SubGridCalculation>();
    int[] queue = {0};
    boolean subGridsInitialized = false;
	
    /**
     * Create the CellGrid with the given number of rows, columns and threads.
     * @param p_x_max
     * @param p_y_max
     * @param threadsNum
     */
	public CellGrid(int p_x_max, int p_y_max, int threadsNum) {
		super(p_x_max, p_y_max);
		this.threadsNum = threadsNum;
	}

	/**
	 * Calculate the next step.
	 */
	public void step() {
		// Because we need to update all cells synchronously, we
		// first calculate the next state for each cell, and store it
		// in a temporary array: 
		int[][] nextStates = new int[maxX + 1][maxY + 1];
		
		/*
		 * TODO: really implement the "initialized/not initialized
		 * mechanism. If the threads are initialized, simply loop in the
		 * List of threads and start() them. Correctly separate the things
		 * that initializeSubGridsThreads() now does.
		 */
		createBlockingQueue(nextStates);
		if ((!subGridsInitialized) || (threadsNumberChanged)) {
			initializeSubGridsThreads(nextStates);
		} else {
			
			resumeSubGridsThreads(nextStates);
		}
		
		// Wait that all the threads have computed
		while (queue[0] > 0) {
			System.out.println("active wait: " + queue[0]);
		}
		
		// Then, we update all the cells:
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				Cell cell = (Cell) getObjectAt(x, y);
				cell.setState(nextStates[x][y]);
			}
		}
	}

	private void resumeSubGridsThreads(int[][] nextStates) {
		for (SubGridCalculation t : subGrids) {
			t.setNewStatesGrid(nextStates);
			t.setComputing(true);
		}
	}

	/**
	 * Create, start and add to a list all the threads that will handle
	 * the subgrids. The method calculates the fair number of cells to 
	 * assign to each thread.
	 * @param nextStates reference to the array that will hold the next state
	 */
	private void initializeSubGridsThreads(int[][] nextStates) {
		subGrids.clear();
		System.out.println("initializeSub");
		int remainingCells = (nextStates.length -1) * (nextStates[0].length -1);
		int remainingThreads = threadsNum;
		
		// # threads > # cells? NO -> max is # cells (1 thread <-> 1 cell).
		if (remainingThreads > remainingCells) {
			remainingThreads = remainingCells;
		}
		
		// how many cells has to compute each thread?
		int step = remainingCells / remainingThreads;
		
		int xIter = 0, yIter = 0;
		
		while (remainingThreads > 1) {
			List<Coordinate> cellsArgs = new ArrayList<Coordinate>();
			for (int i = 0; i < step; i++) {
				cellsArgs.add(new Coordinate(xIter, yIter++));
				// Check we have to start a new row
				if (yIter >= maxY) {
					xIter++;
					yIter = 0;
				}
			}
			// Add the thread to the list
			SubGridCalculation subcalc = 
					new SubGridCalculation(cellsArgs, this, nextStates, queue);
			subcalc.setComputing(true);
			subcalc.start();
			subGrids.add(subcalc);
			remainingCells -= step;
			remainingThreads -= 1;
		}
		
		// The remaining cells are for the last thread.
		List<Coordinate> cellsArgs = new ArrayList<Coordinate>();
		while (remainingCells > 0) {
			cellsArgs.add(new Coordinate(xIter, yIter++));
			// Check we have to start a new row
			if (yIter >= maxY) {
				xIter++;
				yIter = 0;
			}
			remainingCells -= 1;
		}
		// Add the thread to the list
		SubGridCalculation subcal = 
				new SubGridCalculation(cellsArgs, this, nextStates, queue);
		subcal.setComputing(true);
		subcal.start();
		subGrids.add(subcal);
		subGridsInitialized = true;
		threadsNumberChanged = false;
	}
	
	private void createBlockingQueue(int[][] nextStates) {
		int remainingThreads = threadsNum;
		int remainingCells = (nextStates.length -1) * (nextStates[0].length -1);
		queue[0] = 0;
		
		// # threads > # cells? NO -> max is # cells (1 thread <-> 1 cell).
		if (remainingThreads > remainingCells) {
			remainingThreads = remainingCells;
		}
		int remainingThreadsCopy = remainingThreads;
		
		while (remainingThreadsCopy > 0) {
			synchronized (queue) {
				queue[0] += 1;
				remainingThreadsCopy--;
			}
		}
	}

	/**
	 * Return the number of threads.
	 * @return the number of threads
	 */
	@Override
	public int getThreadsNumber() {
		return threadsNum;
	}
}
