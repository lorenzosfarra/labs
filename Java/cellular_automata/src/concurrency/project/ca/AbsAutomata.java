package concurrency.project.ca;

import java.awt.Color;
import java.util.Enumeration;

/**
 * Abstract class to represent the different types of Automata.
 * Implements a simple interface that all derived classes have
 * to implement.
 * @author Lorenzo Sfarra - Eneina Gjata
 */
public abstract class AbsAutomata {

	// Current and future state
	protected int state;
	protected int futureState;
	
	// max rows and max columns (< 0 means infinite)
	protected int maxX = -1;
	protected int maxY = -1;
	
	public boolean isSizeCorrect(int rows, int cols) {
		if (maxX == -1) {
			if (maxY == -1) return true;
			else return (cols <= maxY);
		} else {
			if (maxY == -1) return (rows <= maxX);
			else return ((rows <= maxX) && (cols <= maxY));
		}
	}
	
	
	public String getHelp() {
		return "Default help";
	}
	
	/**
	 * Set the state.
	 * @param state
	 */
	public abstract void setState(int state);
	
	/**
	 * Compute the next automata's state and return its value.
	 * @param neighbors the automata's neighbors
	 * @return the computed next state
	 */
	public abstract int getNextState(Enumeration<AbsAutomata> neighbors);

	/**
	 * Compute the next automata's state
	 * @param neighbors the automata's neighbors
	 */
	public abstract void goToNextState(Enumeration<AbsAutomata> neighbors);
	
	/**
	 * Update the state (future state becomes the current state).
	 */
	public abstract void updateState();

	/**
	 * Get the current state.
	 * @return the current state
	 */
	public int getState() {
		return state;
	}

	/**
	 * Get the color associated with the current status.
	 * @return the Color object
	 */
	public abstract Color getColor();
	
	/**
	 * Go to the next state of the defined automata.
	 * If, for example, the automata has only 2 states (dead, alive) and
	 * this is a dead automata, nextState() bring the automata to the 
	 * alive state. 
	 */
	public abstract void nextState();

}
