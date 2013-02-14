package concurrency.project.ca;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import concurrency.project.ca.cellsandgrids.Cell;

/**
 * Factory to create new cells containing a given automata type.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class AutomataCellFactory {
	/**
	 * Static method to return a new Cell object containing the given
	 * automata type.
	 * @param automataType the automata type
	 * @param x coordinate X of the Cell object in a Grid
	 * @param y coordinate Y of the Cell object in a Grid
	 * @param state the initial state of the automata contained in the Cell
	 * @return the created Cell instance
	 */
	public static Cell createNewAutomataCell(String automataType,
									int x, int y, String state) {
		Class<?> newClass;
		try {
			newClass = Class.forName(automataType);
			Constructor<?> constr = newClass.getConstructor();
			Cell newCell = new Cell(null, x, y);
			AbsAutomata newAutomata = (AbsAutomata) constr.newInstance();
			newAutomata.setState(Integer.parseInt(state));
			newCell.setAutomata(newAutomata);
			return newCell;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
