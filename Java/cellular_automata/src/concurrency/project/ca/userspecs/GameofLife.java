package concurrency.project.ca.userspecs;

import java.awt.Color;
import java.util.Enumeration;
import concurrency.project.ca.AbsAutomata;
import concurrency.project.ca.cellsandgrids.Cell;

/**
 * The "Game of Life" (from now GoL) Automata.
 * <a href="http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life">Info</a>.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class GameofLife extends AbsAutomata {
	/**
	 * GoL has two states, dead or alive.
	 * @author Lorenzo Sfarra - Eneina Gjata
	 *
	 */
	public class State {
		final static int DEAD = 0;
		final static int ALIVE = 1;
	};
	public GameofLife() {
		super();
	}

	/**
	 * Calculate and return the next state of a GoL automata.
	 * @param neighbors the adjacent cells
	 */
	public int getNextState(Enumeration<AbsAutomata> neighbors) {
		// Go to the next state
		goToNextState(neighbors);
		return futureState;
	}

	/**
	 * Set the state of the automata
	 */
	@Override
	public void setState(int state) {
		this.state = state;
		
	}
	
	/**
	 * Calculate and go to the next state of this automata.
	 */
	@Override
	public void goToNextState(Enumeration<AbsAutomata> neighbors) {
		int counter = 0;
		while (neighbors.hasMoreElements()) {
			AbsAutomata neighbor = neighbors.nextElement();
			if (neighbor instanceof AbsAutomata) {
				AbsAutomata automata = (AbsAutomata) neighbor;
				if (automata.getState() == State.ALIVE) {
					// If the cell has at least one neighboring cell
					// that is alive, this cell should become alive.
					counter++;
				}
			}
		}
		if (counter > 0)
		// FIRST CASE: current state of the cell: alive.
		if (this.getState() == State.ALIVE) {
			if ((counter != 2) && (counter != 3)) {
				futureState = State.DEAD;
			} else {
				futureState = state;
			}
		} else {
			// SECOND CASE: current state of the cell: dead.
			if (counter == 3) {
				futureState = State.ALIVE;
			} else {
				futureState = state;
			}
		}
		// No alive neighbor found, next state is current state
	}

	/**
	 * Return the color of the automata, that will be used to display
	 * the color of the containing {@link Cell}, based on 
	 * the automata's state.
	 * @return a Color object
	 */
	@Override
	public Color getColor() {
		if (getState() == State.DEAD) {
			return Color.BLACK;
		} else {
			return Color.CYAN;
		}
	}

	/**
	 * "Upgrade" the automata to the next state.
	 */
	@Override
	public void nextState() {
		if (getState() == State.DEAD) {
			setState(State.ALIVE);
		} else {
			setState(State.DEAD);
		}
	}

	/**
	 * "Upgrade" the automata to the already calculated next step.
	 */
	@Override
	public void updateState() {
		// Check if the future state is valid
		if (futureState >= 0) {
			state = futureState;
		}
	}
}
