package Backend;

import java.io.*;
import java.util.Scanner;

/**
 *<h1>parse Class</h1>
 * Parses a comma delimited file into a collection of Process objects <br>
 *
 *@author Braden McGee
 */
public class Parser {
    private String processFileName;
    private File inFile;
    private Scanner inFileScanner;

    /**
     * Constructor
     * Calls getInputFileReader() to activate the inFile and inFileScanner objects to start the parsing process.
     */
    public Parser() {
        getInputFileReader();
    }

    private Scanner getInputFileReader()
    {
        //Scanner userInput = new Scanner(System.in);
        //System.out.println("What is the name of your input text file?");

        processFileName = "processes.txt";

        inFile = new File(System.getProperty("user.dir") + "\\" + processFileName);

        try {
            inFileScanner = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null; // This shouldn't be reached
    }

    /**
     *
     * @return
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

