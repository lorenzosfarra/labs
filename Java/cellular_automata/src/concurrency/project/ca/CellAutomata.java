package concurrency.project.ca;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import concurrency.project.ca.cellsandgrids.Cell;
import concurrency.project.ca.cellsandgrids.CellGrid;
import concurrency.project.ca.cellsandgrids.CellGridDisplay;
import concurrency.project.ca.cellsandgrids.CellThread;
import concurrency.project.ca.cellsandgrids.Grid;
import concurrency.project.ca.cellsandgrids.GridDisplay;
import concurrency.project.ca.communicationmanager.CommunicationManager;
import concurrency.project.ca.io.CellularAutomataLoader;
import concurrency.project.ca.io.CellularAutomataSaver;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class to implement the MouseListener interface.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
class MouseHandler implements MouseListener {
	private GridDisplay grid;

	public MouseHandler(GridDisplay display) {
		super();
		grid = display;
	}

	public void mousePressed(MouseEvent e) {
		// mousePressed
	}

	public void mouseReleased(MouseEvent e) {
		//mouseReleased
	}

	public void mouseEntered(MouseEvent e) {
		// mouseEntered
	}

	public void mouseExited(MouseEvent e) {
		// mouseExited
	}

	/**
	 * Handle the "on mouse clicked" event.
	 */
	public void mouseClicked(MouseEvent e) {
		Cell obj = grid.getObjectAt(e.getX(), e.getY());
		Cell cell = (Cell) obj;
		cell.changeStateAfterMouseClick();
		grid.repaint();
	}
}

/**
 * Entry point of the program. It takes care of the initialization of the
 * functional components of the program (communication manager, grid,
 * cells, automatas) and of the GUI components (extends JPanel and
 * creates all the GUI-related objects. It takes care of parse the
 * users' inputs, too.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellAutomata extends JPanel implements ActionListener {
	private static CellAutomata ca;
	private CellGridDisplay display;
	private JButton startButton, stopButton, stepButton, saveButton, restoreButton;
	private CellThread thread;
	private Grid grid;
	/*
	 * We store the information of the type of the automata because it will
	 * be useful in case we're going to save the state of the CA. In that
	 * case we should be able to store also the information on the
	 * automata type stored in the various cells.
	 */
	private String cellAutomataTypeName;

	// Display frame
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 570;
	
	private static final int BUTTONS_HEIGHT = 80;

	private static final long serialVersionUID = 0L;

	/**
	 * Initialize the program's component.
	 * @param grid
	 * @param cellularAutomataType
	 */
	public CellAutomata(CellGrid grid, String cellularAutomataType, 
			int x, int y, int threads) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
		this.setCellAutomataTypeName(cellularAutomataType);
		this.grid = grid;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		display = new CellGridDisplay(grid);
		display.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT
				- BUTTONS_HEIGHT));
		display.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT
				- BUTTONS_HEIGHT));
		display.addMouseListener(new MouseHandler(display));
		add(display);

		add(Box.createRigidArea(new Dimension(FRAME_WIDTH, 5)));

		JPanel panel1 = new JPanel();
		panel1.setSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setPreferredSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setMaximumSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setBackground(Color.white);
		
		setUpButtons();

		// Add the start and stop buttons, and the table, to the
		// container, using the default layout.
		panel1.add(startButton);
		panel1.add(stepButton);
		panel1.add(stopButton);
		panel1.add(saveButton);
		panel1.add(restoreButton);

		add(panel1);
	}

	/**
	 * Set up the GUI buttons and their related actions.
	 */
	private void setUpButtons() {
		startButton = new JButton("Start >>");
		// place button at center, left position
		startButton.setVerticalTextPosition(AbstractButton.CENTER);
		startButton.setHorizontalTextPosition(AbstractButton.LEFT);
		startButton.setMnemonic(KeyEvent.VK_D);
		startButton.setActionCommand("start");

		stepButton = new JButton("Step >");
		// place button at center, left position
		stepButton.setVerticalTextPosition(AbstractButton.CENTER);
		stepButton.setHorizontalTextPosition(AbstractButton.LEFT);
		stepButton.setMnemonic(KeyEvent.VK_D);
		stepButton.setActionCommand("step");

		stopButton = new JButton("= Stop =");
		// Use the default button position of CENTER, RIGHT.
		stopButton.setMnemonic(KeyEvent.VK_E);
		stopButton.setActionCommand("stop");
		stopButton.setEnabled(false);

		saveButton = new JButton("Save!");
		// place button at center, left position
		saveButton.setVerticalTextPosition(AbstractButton.CENTER);
		saveButton.setHorizontalTextPosition(AbstractButton.LEFT);
		saveButton.setMnemonic(KeyEvent.VK_S);
		saveButton.setActionCommand("save");
		
		restoreButton = new JButton("[ Restore ]");
		// place button at center, left position
		restoreButton.setVerticalTextPosition(AbstractButton.CENTER);
		restoreButton.setHorizontalTextPosition(AbstractButton.LEFT);
		restoreButton.setMnemonic(KeyEvent.VK_R);
		restoreButton.setActionCommand("restore");
		
		// Listen for actions on the Start and Stop buttons.
		startButton.addActionListener(this);
		stepButton.addActionListener(this);
		stopButton.addActionListener(this);
		saveButton.addActionListener(this);
		restoreButton.addActionListener(this);
		
	}

	/**
	 * Calculate the next step.
	 */
	public void step() {
		display.step();
	}

	/**
	 * Return the name of the current cellular automata's type
	 * @return the cellAutomataTypeName
	 */
	public String getCellAutomataTypeName() {
		return cellAutomataTypeName;
	}

	/**
	 * Set the name of the current cellular automata's type
	 * @param cellAutomataTypeName the cellAutomataTypeName to set
	 */
	public void setCellAutomataTypeName(String cellAutomataTypeName) {
		this.cellAutomataTypeName = cellAutomataTypeName;
	}

	/**
	 * Decide the action to take based on the button the user
	 * has clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			startButton.setEnabled(false);
			saveButton.setEnabled(false);
			restoreButton.setEnabled(false);
			stopButton.setEnabled(true);
			thread = new CellThread(this);
			thread.start();
		} else if (e.getActionCommand().equals("stop")) {
			startButton.setEnabled(true);
			saveButton.setEnabled(true);
			restoreButton.setEnabled(true);
			stopButton.setEnabled(false);
			thread.suspendThread();
		} else if (e.getActionCommand().equals("step")) {
			step();
		} else if (e.getActionCommand().equals("save")) {
			try {
				CellularAutomataSaver.save(this, grid, 
								grid.getThreadsNumber());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("restore")) {
			CellularAutomataLoader loader;
			try {
				loader = new CellularAutomataLoader(this, grid);
				loader.load();
				grid.setThreadsNumber(loader.threads);
				CommunicationManager cm = new CommunicationManager(grid);
				new Thread(cm).start();

				for (int x = 0; x < grid.maxX(); x++) {
					for (int y = 0; y < grid.maxY(); y++) {
						Cell cell = (Cell) grid.getObjectAt(x, y);
						cell.setComManager(cm);
					}
				}
				
			} catch (Exception e1) {
				// TODO SHOW A DIALOG!
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Main method. Create the grid, the communication manager and fill the
	 * grid with cells containing the automatas of the type indicated by
	 * the user as an argument to the program.
	 * It takes care to create the GUI components like the main JFrame and
	 * set some related callbacks.
	 * 
	 * @param args the user's arguments
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws IllegalArgumentException, 
		InvocationTargetException, SecurityException, NoSuchMethodException {
		HashMap<String, Object> userInput = getUserInput(args);
		int maxX = (Integer) userInput.get("maxX");
		int maxY = (Integer) userInput.get("maxY");
		//String automataPath = "concurrency.project.ca.userspecs." + args[0];
		CellGrid grid = new CellGrid(maxX, maxY, 
						(Integer) userInput.get("threads"));
		final CommunicationManager cm = new CommunicationManager(grid);
		JFrame frame = new JFrame(args[0]);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// STOP the Communication Manager thread
				cm.setAlive(false);
				System.exit(0);
			}
		});
		
		// Check if the given dimensions are good for the used problem
		Class<?> newClass;
		try {
			newClass = Class.forName((String) userInput.get("automata"));
			Constructor<?> constr = newClass.getConstructor();
			AbsAutomata newAutomata = (AbsAutomata) constr.newInstance();
			if (!newAutomata.isSizeCorrect(maxY, maxX)) {
				System.out.println(newAutomata.getHelp());
				System.exit(2);
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// Fill the grid with cell containing the user's defined automata.
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				/* Create the Cell with the given automata type and put
					the cell in the grid */
				Cell newCell = AutomataCellFactory.createNewAutomataCell(
						(String) userInput.get("automata"), x, y, "0");
				newCell.setComManager(cm);
				grid.setObjectAt(x, y, newCell);
			}
		}
		ca = new CellAutomata(grid,
							(String) userInput.get("automata"), maxX, maxY,
							(Integer) userInput.get("threads"));
		frame.getContentPane().add(ca, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	/**
	 * Build an hashmap with user's input, or default values when needed.
	 * @param args user's input
	 * @return {@link HashMap} with customization parameters
	 */
	private static HashMap<String, Object> getUserInput(String[] args) {
		if (args.length == 0) {
			System.err.println(
					"Usage: java CellAutomata <class name> [<threads number>]");
			System.exit(1);
		}
		String automataPath = CellAutomata.class.getPackage().getName() + 
													".userspecs." + args[0];
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("automata", automataPath);
		
		/* Check if the user has specified the number of thread, otherwise
		 * we'll calculate the number of threads based on the number of cores.
		 */
		int threadsNumber = -1;
		if (args.length >= 2) {
			try {
				threadsNumber = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				// Nothing.
			}
		}
		// Get the number of rows and numbers
		Scanner scanner = new Scanner(System.in);
		System.out.print("Number of rows: ");
		int rows = -1;
		if (scanner.hasNextInt()) {
			rows = scanner.nextInt();
		} else {
			System.out.println("Invalid input, not a number. Exit.");
			System.exit(2);
		}
		map.put("maxY", rows);
		System.out.print("Number of columns: ");
		int columns = -1;
		if (scanner.hasNextInt()) {
			columns = scanner.nextInt();
		} else {
			System.out.println("Invalid input, not a number. Exit.");
			System.exit(2);
		}
		map.put("maxX", columns);
		if (threadsNumber < 0) {
			threadsNumber = threadsForCores(rows * columns);
		} else {
			if (threadsNumber > (rows * columns)) {
				// Number of cells - 1 thread for the Communication Manager
				threadsNumber = (rows * columns) - 1;
			}
		}
		System.out.println("threads Number is: " + threadsNumber);
		map.put("threads", threadsNumber);
		System.out.println("Starting the program with the following params:");
		System.out.println(map);
		return map;
	}

	/**
	 * Calculate the fair number of threads in order to optimize the use
	 * of the available cores. The threads are: <pre><ul>
	 * <li>Communication Manager</li><li>Live simulation</li>
	 * <li>SubGrid calculation</li></ul>. The "live simulation" is not
	 * considered, for reasons specified in the documentation.
	 * @param cellsNumber the number of cells
	 * @return the number of threads to use
	 */
	private static int threadsForCores(int cellsNumber) {
		int cores = Runtime.getRuntime().availableProcessors();
		if (cores >= cellsNumber) {
			return cellsNumber;
		}
		// 1 thread is for the Communication Manager..
		return ((cellsNumber / cores) - 1);
	}
}
