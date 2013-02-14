package concurrency.project.ca.cellsandgrids;

/**
 * Class that handle the visual state of the grid,
 * repainting when necessary.
 * 
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellGridDisplay extends GridDisplay {
	static final long serialVersionUID = 0L;

	/**
	 * Create the CellGridDisplay.
	 * @param grid the associated CellGrid object
	 */
	public CellGridDisplay(CellGrid grid) {
		super(grid);
	}

	/**
	 * Calculate the next state and update the GUI.
	 */
	public void step() {
		CellGrid cgrid = (CellGrid) grid;
		cgrid.step();
		repaint();
	}

}
