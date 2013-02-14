package concurrency.project.ca.io;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import concurrency.project.ca.CellAutomata;
import concurrency.project.ca.cellsandgrids.Cell;
import concurrency.project.ca.cellsandgrids.Grid;

/**
 * Save the running program and the state of all the {@link Grid}
 * {@link Cell}s in an XML file. You can reload the saved program's state
 * with the {@link CellularAutomataLoader} class.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellularAutomataSaver {
	private static final String ROOT = "rootcellauto";
	private static final String MAXX = "maxX";
	private static final String MAXY = "maxY";
	private static final String CATYPE = "catype";
	private static final String THREADS = "threads";

	/**
	 * Save the program's state
	 * @param caller the caller's program
	 * @param grid the {@link Grid} to save
	 * @return true if no errors occurred saving the state
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static boolean save(CellAutomata caller, Grid grid, int threads) 
	throws IOException, ParserConfigurationException, TransformerException {
		int x = grid.maxX();
		int y = grid.maxY();
		
		/** Create the XML document and its root */
		String root = ROOT;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement(root);
		
		// Save maxX and maxY
		Element maxX, maxY;
		maxX = document.createElement(MAXX);
		maxY = document.createElement(MAXY);
		maxX.appendChild(document.createTextNode(String.valueOf(x)));
		maxY.appendChild(document.createTextNode(String.valueOf(y)));
		// Cellular automata type
		Element elementAutomataType = document.createElement(CATYPE);
		elementAutomataType.appendChild(
				document.createTextNode(caller.getCellAutomataTypeName()));
		// Save the number of threads to parse the subgrids
		Element threadsNum = document.createElement(THREADS);
		threadsNum.appendChild(
				document.createTextNode(String.valueOf(threads)));
		// Create root element and add references to X and Y
        document.appendChild(rootElement);
        rootElement.appendChild(maxX);
        rootElement.appendChild(maxY);
        rootElement.appendChild(elementAutomataType);
        rootElement.appendChild(threadsNum);
		
		for (int i = 0; i < x; i++) { 
			Element rowElement = document.createElement("row");
			for (int j = 0; j < y; j++) {
				Element elementX = document.createElement("x");
				Element elementY = document.createElement("y");
				Element elementAlive = document.createElement("state");
				elementX.appendChild(document.createTextNode(String.valueOf(i)));
				elementY.appendChild(document.createTextNode(String.valueOf(j)));
				Cell cell = (Cell) grid.getObjectAt(i, j);
				elementAlive.appendChild(
						document.createTextNode(
								String.valueOf(cell.getAutomata().getState())));
				rowElement.appendChild(elementX);
				rowElement.appendChild(elementY);
				rowElement.appendChild(elementAlive);
			}
			rootElement.appendChild(rowElement);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        File outputFile = getOutputFile(caller); 
        StreamResult result =  new StreamResult(outputFile);
        transformer.transform(source, result);
		return true;
	}

	/**
	 * Show a dialog to choose the file name for the saved state and its
	 * position in the user's file system.
	 * @param caller
	 * @return
	 */
	private static File getOutputFile(CellAutomata caller) {
		FileChooserDialog fileChooser = new FileChooserDialog(caller);
		return fileChooser.open();
	}
}
