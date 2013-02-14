package concurrency.project.ca.cellsandgrids;

import java.awt.Color;
import concurrency.project.ca.AbsAutomata;
import concurrency.project.ca.communicationmanager.CommunicationManager;

/**
 * Class that represent a cell inside the main Grid
 * 
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class Cell {
	// Reference to the call manager
	private CommunicationManager comManager;
	// The automata contained in the cell
	private AbsAutomata automata;
	// Coordinates (x,y)
	private int mx;
	private int my;

	/**
	 * Constructor for the cell
	 * @param cm {@link CommunicationManager} associated with this cell
	 * @param x coordinate X
	 * @param y coordinate Y
	 */
	public Cell(CommunicationManager cm, int x, int y) {
		this.comManager = cm;
		this.mx = x;
		this.my = y;
	}
	
	/**
	 * Constructor for the cell
	 * @param cm Communication Manager associated with this cell
	 * @param newAutomata the {@link AbsAutomata} contained in the cell
	 * @param x coordinate X
	 * @param y coordinate Y
	 */
	public Cell(CommunicationManager cm, AbsAutomata newAutomata,
											int x, int y) {
		super();
		this.comManager = cm;
		this.automata = newAutomata;
		this.mx = x;
		this.my = y;
	}
	
	/**
	 * Set the automata contained in this cell
	 * @param newAutomata the automata
	 */
	public void setAutomata(AbsAutomata newAutomata) {
		this.automata = newAutomata;
	}
	
	/**
	 * Coordinate X
	 * @return coordinate X
	 */
	public int getX() {
		return mx;
	}
	
	/**
	 * Coordinate Y
	 * @return coordinate Y
	 */
	public int getY() {
		return my;
	}

	/**
	 * Color of the cell
	 * @return the color of the cell (dependent on the automata state)
	 */
	public Color getColor() {
		return automata.getColor();
	}

	/**
	 * Set the state of the cell (set the state of the automata that is 
	 * inside the cell).
	 * @param s the new state
	 * @return true if the new state is different from the current state
	 */
	public boolean setState(int s) {
		automata.setState(s);
		return (!(automata.getState() == s));
	}

	/**
	 * Set the cell current state (the current state of the automata that
	 * is inside the cell).
	 * @return the state of the cell's automata.
	 */
	public int getState() {
		return automata.getState();
	}

	/**
	 * Calculates the next state for the cell.
	 * The next state of a cell is the next state of the contained automata. 
	 * @return the next state of the cell
	 */
	public int getNextState() {
		int stat = automata.getNextState(comManager.getNeighbors(this));
		return stat;
	}
	
	/**
	 * Return the CommunicationManager instance associated with this cell.
	 * @return the CommunicationManager instance associated with this cell.
	 */
	public CommunicationManager getComManager() {
		return comManager;
	}

	/**
	 * Set the {@link CommunicationManager} instance associated with this cell
	 * @param comManager the communication manager to associate with this cell
	 */
	public void setComManager(CommunicationManager comManager) {
		this.comManager = comManager;
	}

	/**
	 * Click on a cell - go to the next State
	 */
	public void changeStateAfterMouseClick() {
		automata.nextState();
	}
	
	/**
	 * Get the automata contained in the cell.
	 * @return the automata contained in the cell.
	 */
	public AbsAutomata getAutomata() {
		return automata;
	}
}
