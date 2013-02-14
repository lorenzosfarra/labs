package concurrency.project.ca.cellsandgrids;

import javax.swing.*;
import java.awt.*;

/**
 * A wrapper for the {@link Grid} useful to display the Grid in the GUI.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class GridDisplay extends JPanel {
	protected Grid grid;
	static final long serialVersionUID = 0L;

	/**
	 * Initialize the object.
	 * @param grid The represented {@link Grid} object
	 */
	GridDisplay(Grid grid) {
		this.grid = grid;
		setOpaque(false);
	}

	/**
	 * Get the real width, based on the Swing Rectangle object.
	 * @return
	 */
	int getRealWidth() {
		return getVisibleRect().width; 
	}

	/**
	 * Get the real height, based on the Swing Rectangle object.
	 * @return
	 */
	int getRealHeight() {
		return getVisibleRect().height;
	}

	protected int getSquareWidth() {
		int swidth = getRealWidth() / (grid.maxX() + 1);
		
		if (swidth > 0) {
			return swidth;
		} else {
			return 1;
		}
	}

	protected int getSquareHeight() {
		int sheight = getRealHeight() / (grid.maxY() + 1);

		if (sheight > 0) {
			return sheight;
		} else {
			return 1;
		}
	}

	protected int getHorizontalOffset() {
		int offset = (getRealWidth() - (getSquareWidth() * (grid.maxX()))) / 2;
		return offset;
	}

	protected int getVerticalOffset() {
		int offset = (getRealHeight() - (getSquareHeight() * (grid.maxY() + 2))) / 2;
		return offset;
	}

	public Cell getObjectAt(int x, int y) {
		int grid_x = (x - getHorizontalOffset()) / getSquareWidth();
		int grid_y = Math.abs(((y - getVerticalOffset()) / getSquareHeight())
				- grid.maxY());

		return grid.getObjectAt(grid_x, grid_y);
	}

	public void paintComponent(Graphics g) throws RuntimeException {
		int squarewidth = getSquareWidth();
		int squareheight = getSquareHeight();

		int hoffset = getHorizontalOffset();
		int voffset = getVerticalOffset();

		for (int x = 0; x < grid.maxX(); x++) {
			for (int y = 0; y < grid.maxY(); y++) {
				Cell tmp = grid.grabObjectAt(x, y);

				if (tmp == null) {
					g.setColor(Color.gray.darker()); 
				} else {
					g.setColor(tmp.getColor());
				}

				g.fillRect(hoffset + x * squarewidth, voffset
						+ ((grid.maxY() - y) * squareheight), squarewidth - 1,
						squareheight - 1);
			}
		}
	}
}
