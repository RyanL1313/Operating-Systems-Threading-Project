import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 *<h1>parse Class</h1>
 * Parses a comma delimited file into a collection of Process objects <br>
 *
 *@author Braden McGee, Ryan Lynch
 */
public class Parser {
    private String processPathName; // The full path of the file of processes
    private File inFile; // The File object to read the CSV file
    private Scanner inFileScanner; // A scanner to read the file

    /**
     * Constructor
     * Calls getInputFileReader() to activate the inFile and inFileScanner objects to start the parsing process.
     */
    public Parser() {
        getInputFileReader();
    }

    private void getInputFileReader()
    {
        // Display a dialog box to retrieve the full file path, then initialize the File object
        processPathName = JOptionPane.showInputDialog("Enter the full path of your processes file");
        inFile = new File(processPathName);
        // Initialize the file Scanner
        try {
            inFileScanner = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the next process in the CSV file.
     * @return A process object with all of the attributes that were read in from the file.
     */
    public Process nextProcess()
    {
        if (inFileScanner.hasNextLine())
        {
            String processLine = inFileScanner.nextLine();

            String processAttributes[] = processLine.split(","); // Each substring separated by commas is added to the array
            processAttributes = removeAttributeWhiteSpace(processAttributes); // Remove leading/trailing whitespace for each process attribute

            Process thisProcess = new Process(processAttributes);
            return thisProcess;
        }

        return null; // No more processes to parse if this point is reached
    }

    /**
     * Removes all of the leading/trailing whitespace from the arrTime, ID, serTime, and priority attributes of a process.
     * @param attributes The array of attributes of the process
     * @return The modified array of attributes
     */
    private String[] removeAttributeWhiteSpace(String[] attributes)
    {
        for (int i = 0; i < Process.getNumAttributes(); i++)
            attributes[i] = attributes[i].trim(); // Removes only the leading/trailing whitespace

        return attributes;
    }
}

