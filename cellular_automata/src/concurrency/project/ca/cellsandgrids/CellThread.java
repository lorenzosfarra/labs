package concurrency.project.ca.cellsandgrids;

import concurrency.project.ca.CellAutomata;

/**
 * Implements the thread that will simulate the continuous execution
 * of the steps of the automatas.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellThread extends Thread {
	// Time (in milliseconds) to sleep between steps
	private final static int SLEEP_DELAY = 500; 

	private CellAutomata ca;

	private boolean suspended;

	/**
	 * Build the CellThread.
	 * @param ca the correspondent {@link CellAutomata}
	 */
	public CellThread(CellAutomata ca) {
		this.ca = ca;
		this.suspended = false;
	}

	public void suspendThread() {
		suspended = true;
	}

	public void run() {
		while (!suspended) {
			ca.step();
			try {
				Thread.sleep(SLEEP_DELAY);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
				break;
			}
		}
	}
}
