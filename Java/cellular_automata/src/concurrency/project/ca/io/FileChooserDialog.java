package concurrency.project.ca.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import concurrency.project.ca.CellAutomata;

/**
 * Class that manages the dialogs to open and save a file.
 * @author Lorenzo Sfarra - Eneina Gjata
 *
 */
public class FileChooserDialog {
	
	JFileChooser fileChooser;
	
	CellAutomata callingObject;
	
	/**
	 * Contructor of the class.
	 * @param caller the calling object
	 */
	public FileChooserDialog(CellAutomata caller) {
		fileChooser = new JFileChooser();
		callingObject = caller;
	}

	/**
	 * Dialog to open a file 
	 * @return File object, the selected file
	 */
	public File open() {
		File fileName = null;
		if (fileChooser.showOpenDialog(callingObject) == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile();
		}
		return fileName;
		
	}

	/**
	 * Dialog to save a file
	 * @param toSave the string to save in the file
	 * @return a boolean that represents the success of the operation
	 */
	public boolean save(String toSave) {
		boolean status = false;
		File fileName = null;
		if (fileChooser.showSaveDialog(callingObject) == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile();
		}
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(toSave);
			out.close();
			// IO operations completed successfully
			status = true;
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return status;
	}
}
