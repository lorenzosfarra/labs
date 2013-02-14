package concurrency.project.ca.communicationmanager;

import java.util.Enumeration;
import java.util.Vector;

import concurrency.project.ca.AbsAutomata;
import concurrency.project.ca.cellsandgrids.Cell;
import concurrency.project.ca.cellsandgrids.Grid;

/**
 * The Communication Manager. It takes care to receive/send informations
 * to the various {@link Cell} (that contain {@link AbsAutomata}) about
 * their neighborhoods.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CommunicationManager implements Runnable {
	
	Grid grid;
	private boolean alive;
	
	/**
	 * The Communication Manager constructor.
	 * @param grid the correspondent Grid
	 */
	public CommunicationManager(Grid grid) {
		this.grid = grid;
		this.setAlive(true);
	}
	
	/**
	 * Get the neighbors for a given {@link Cell}
	 * @param cell the {@link Cell} requesting its neighbors
	 * @return the cell neighbors
	 */
	public synchronized Enumeration<AbsAutomata> getNeighbors(Cell cell) {
		int mx = cell.getX();
		int my = cell.getY();
		
		Vector<AbsAutomata> objects = new Vector<AbsAutomata>();
		
		for (int x = mx - 1; x <= mx + 1; x++) {
			for (int y = my - 1; y <= my + 1; y++) {
				if (x != mx || y != my) { // don't include this
					if (grid.validLocation(x, y)) {
						AbsAutomata obj = grid.getObjectAt(x, y).getAutomata();
						if (obj != null) {
							objects.addElement(obj);
						}
					} else {
						/* This mean that we are on the edge. the x position
						 * or the y position aren't valid and we should take
						 * the neighbor on the opposite side of the grid.
						 */
						int calculatedX = x, calculatedY = y;
						AbsAutomata obj = null;
						if (x >= grid.maxX()) {
							calculatedX = 0;
						} else if (x < 0) {
							calculatedX = grid.maxX() - 1;
						}
						if (y >= grid.maxY()) {
							calculatedY = 0;
						} else if (y < 0) {
							calculatedY = grid.maxY() - 1;
						}
						obj = grid.getObjectAt(calculatedX, calculatedY)
										.getAutomata();
						objects.addElement(obj);
					}
				}
			}
		}
		

		return objects.elements();
	
	}

	public synchronized Enumeration<AbsAutomata> getColumnCells(Cell cell) {
		Vector<AbsAutomata> objects = new Vector<AbsAutomata>();
		
		int y = cell.getY();
		
		for (int x = 0; x < grid.maxX(); x++) {
			AbsAutomata obj = grid.getObjectAt(x, y).getAutomata();
			objects.addElement(obj);
		}
		
		return objects.elements();
	}
	
	@Override
	public void run() {
		while(isAlive()) {
			
		}
		System.out.println("CM DIED!");
	}

	/**
	 * Is the thread alive?
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
