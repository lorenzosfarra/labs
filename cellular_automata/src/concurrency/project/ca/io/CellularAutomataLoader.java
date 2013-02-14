package concurrency.project.ca.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import concurrency.project.ca.AutomataCellFactory;
import concurrency.project.ca.CellAutomata;
import concurrency.project.ca.cellsandgrids.Cell;
import concurrency.project.ca.cellsandgrids.Grid;

/**
 * This class loads the state of a previously saved instance of a program
 * from an XML file.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class CellularAutomataLoader {
	
	/* Needed tags in the XML file. */
	private final String ROW = "row";
	private final String MAXX = "maxX";
	private final String MAXY = "maxY";
	private static final String CATYPE = "catype";
	private static final String THREADS = "threads";
	
	// File object
	private File file;
	
    // Objects related to the XML parsing
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;
    private Element docEle;
    
    private CellAutomata caller;
    private String cellularType;
    public int threads;
    private Grid grid;
    
    /**
     * Open the specification file (if it exists) and set up XML parser.
     * 
     * @param caller the calling CA
     * @param grid the grid to modify
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */    
    public CellularAutomataLoader(CellAutomata caller, 
    			Grid grid) throws ParserConfigurationException, 
    											SAXException, IOException {
    	this.caller = caller;
    	dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        file = getOutputFile(caller);
        if (file.exists()) {
            doc = db.parse(file);
            docEle = doc.getDocumentElement();
        }
        this.grid = grid;
    }
    
    /**
     * Calls all the needed methods to load an existent state
     * saved in a XML file.
     * 
     * @throws MalformedSavedStateXML
     */
    public void load() throws MalformedSavedStateXML {
    	setMaxCoordinates();
    	loadSavedGrid();
    	caller.repaint();
    }
    
    /**
     * Reads the max coordinates of the grid from the XML and set the grid
     * accordingly.
     * @throws MalformedSavedStateXML 
     */
    private void setMaxCoordinates() throws MalformedSavedStateXML {
    	int maxX = -1, maxY = -1;
    	NodeList matrixInfosList = docEle.getChildNodes();
        if (matrixInfosList != null && matrixInfosList.getLength() > 0) {
            for (int i = 0; i < matrixInfosList.getLength(); i++) {
                Node node = matrixInfosList.item(i);
                if (node.getNodeName() == MAXX) {
                	maxX = Integer.parseInt(
                				node.getFirstChild().getNodeValue().trim());
                } else if (node.getNodeName() == MAXY) {
                	maxY = Integer.parseInt(
            				node.getFirstChild().getNodeValue().trim());
                } else if (node.getNodeName() == CATYPE) {
                	cellularType = node.getFirstChild().getNodeValue().trim();
                } else if (node.getNodeName() == THREADS) {
                	threads = Integer.parseInt(
                			node.getFirstChild().getNodeValue().trim()
                			);
                }
            }
        }
        if ((maxX <= 0) || (maxY <= 0)) {
        	throw new MalformedSavedStateXML();
        } else {
        	grid.updateSize(maxX, maxY);
        }
    }
    
    /**
     * Load the saved game data into the grid.
     * @return true if success.
     * @throws MalformedSavedStateXML 
     */
    private boolean loadSavedGrid() throws MalformedSavedStateXML {
    	setMaxCoordinates();
        NodeList rowsList = docEle.getElementsByTagName(ROW);
        String xVal, yVal, state;
        int x = 0, y = 0;
		try {
			if (rowsList != null && rowsList.getLength() > 0) {
				for (int i = 0; i < rowsList.getLength(); i++) {
					Node node = rowsList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						// Get infos about the currently parsed row
						Element e = (Element) node;
						NodeList nodeList = e.getElementsByTagName("x");
						xVal = nodeList.item(0).getChildNodes()
								.item(0).getNodeValue().trim();
						// Now parse all the Y
						NodeList nodesForY = e.getElementsByTagName("y");
						x = Integer.parseInt(xVal);
						// TODO: USE FACTORY TO CREATE AUTOMATA & CELLS
						if (nodesForY != null && nodesForY.getLength() > 0) {
							for (int j = 0; j < nodesForY.getLength(); j++) {
								Node nodeY = nodesForY.item(j);
								yVal = nodeY.getFirstChild().getNodeValue().trim();
								nodeList = e.getElementsByTagName("state");
								Node nodeAlive = nodeList.item(j);
								state = nodeAlive.getFirstChild().getNodeValue().trim();
								// Take the values
								y = Integer.parseInt(yVal);
								Cell newCell = AutomataCellFactory.createNewAutomataCell(
										cellularType, x, y, state);
								grid.setObjectAt(x, y, newCell);

							}
						}
					}
				}
			}
		} catch (SecurityException e1) {
			e1.printStackTrace();
			return false;
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		return true;
    }

	private static File getOutputFile(CellAutomata caller) {
		FileChooserDialog fileChooser = new FileChooserDialog(caller);
		return fileChooser.open();
	}
}
