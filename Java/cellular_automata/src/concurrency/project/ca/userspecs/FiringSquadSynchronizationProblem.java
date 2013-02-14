package concurrency.project.ca.userspecs;

import java.awt.Color;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import concurrency.project.ca.AbsAutomata;


/**
 * The "Firing Squad Synchronization Problem" Automata.
 * <a href="http://arxiv.org/ftp/arxiv/papers/1212/1212.3069.pdf">Info</a>.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class FiringSquadSynchronizationProblem extends AbsAutomata {

	public class State {
		// the general state 
		final static int G = 1;
		// the quiescent state
		final static int Q = 0;
		//the soldier state
		final static int A = 3;
		// the firing state
		final static int F = 4;
			
	};
	
	HashMap<Integer, Character> charState = new HashMap<Integer, Character>();
	HashMap<String, Integer> nextStateMap = new HashMap<String, Integer>();
	
	public FiringSquadSynchronizationProblem() {
		super();
		fillCharState();
		fillNextStateMap();
		// 1 dimension problem
		this.maxX = 1;
	}
	
	@Override
	public String getHelp() {
		return "FSSP: 1 Dimension only.";
	}
	
	
	private void fillNextStateMap() {
		// Central 'A'
		nextStateMap.put("QAQ", State.G);
		nextStateMap.put("QAG", State.Q);
		nextStateMap.put("GAQ", State.Q);
		nextStateMap.put("GAG", State.A);
		nextStateMap.put("AAA", State.F);
		
		// Central 'G'
		nextStateMap.put("QGQ", State.A);
		nextStateMap.put("QGA", State.Q);
		nextStateMap.put("AGQ", State.Q);
		nextStateMap.put("AGA", State.G);

		// Central 'Q'
		nextStateMap.put("QQQ", State.Q);
		nextStateMap.put("QQG", State.G);
		nextStateMap.put("QQA", State.A);
		nextStateMap.put("GQQ", State.G);
		nextStateMap.put("GQG", State.A);
		nextStateMap.put("GQA", State.Q);
		nextStateMap.put("AQQ", State.A);
		nextStateMap.put("AQG", State.Q);
		nextStateMap.put("AQA", State.Q);
		
	}

	private void fillCharState() {
		charState.put(State.G, 'G');
		charState.put(State.Q, 'Q');
		charState.put(State.A, 'A');
		charState.put(State.F, 'F');
		
	}
	
	private String convertStates(int left, int current, int right) {
		StringBuffer s = new StringBuffer();
		System.out.println(left + ", " + current + ", " + right);
		s.append(charState.get(left));
		s.append(charState.get(current));
		s.append(charState.get(right));
		return s.toString();
	}

	/**
	 * Set the state of the automata
	 */
	@Override
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Calculate and return the next state of a GoL automata.
	 * @param neighbors the adjacent cells
	 */
	@Override
	public int getNextState(Enumeration<AbsAutomata> neighbors) {
		// Go to the next state
		goToNextState(neighbors);
		return futureState;
	}

	/**
	 * Calculate and go to the next state of this automata.
	 */
	@Override
	public void goToNextState(Enumeration<AbsAutomata> allNeighbors) {
		Enumeration<AbsAutomata> neighbors = getAdjancentAutomatas(allNeighbors);
		AbsAutomata leftNeighbor = neighbors.nextElement();
		AbsAutomata rightNeighbor = neighbors.nextElement();
		
		String currentSt = this.convertStates(leftNeighbor.getState(),
									getState(), rightNeighbor.getState());
		try {
			futureState = nextStateMap.get(currentSt);
		} catch (NullPointerException ex) {
			futureState = state;
		}
	}

	/**
	 * Return the adjacent objects of this cell.
	 * The framework provides the neighbors in this way:
	 * +-+-+-+
	 * |0|3|5|
	 * +-+-+-+
	 * |1|C|6|
	 * +-+-+-+
	 * |2|4|7|
	 * +-+-+-+
	 * 
	 * where 'C' is the current object. So the "left" and "right" position
	 * are the 3rd and 4th neighbors.
	 *  
	 * @param allNeighbors all the neighbors
	 * @return left and right neighbors
	 */
	private Enumeration<AbsAutomata> getAdjancentAutomatas(
			Enumeration<AbsAutomata> allNeighbors) {

		Vector<AbsAutomata> objects = new Vector<AbsAutomata>();
		allNeighbors.nextElement();
		// Left
		objects.add(allNeighbors.nextElement());
		for (int i = 0; i < 4; i++) {
			allNeighbors.nextElement();
		}
		// Right
		objects.add(allNeighbors.nextElement());
		while (allNeighbors.hasMoreElements()) {
			allNeighbors.nextElement();
		}
		return objects.elements();
	}

	/**
	 * "Upgrade" the automata to the already calculated next step.
	 */
	@Override
	public void updateState() {
		// Check if the future state is valid
		if (futureState > 0) {
			state = futureState;
		}
	}

	@Override
	public Color getColor() {
		if (getState() == State.G) {
			return Color.RED;
		}
		else if (getState() == State.Q) {
			return Color.LIGHT_GRAY;
		}
		else if (getState() == State.A) {
			return Color.GREEN;
		} else {
			return Color.BLUE;
		}
		
	}
	
	/**
	 * "Upgrade" the automata to the next state.
	 */
	@Override
	public void nextState() { 
		setState(State.G);
	}
}