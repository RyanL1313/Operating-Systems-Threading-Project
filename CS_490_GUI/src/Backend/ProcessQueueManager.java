package Backend;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class creates and maintains a priority queue used to simulate the threading operations.
 * @author Ryan Lynch
 */
public class ProcessQueueManager {
    private Parser processParser;

    // This priority queue is really just a regular FIFO queue on the first iteration of this program (the comparator orders the processes by arrival time).
    private PriorityQueue<Process> processQueue = new PriorityQueue(new Comparator<Process>() {
        public int compare(Process p1, Process p2) { // This anonymous function will be changed in version 3 of this program (priority instead of FIFO)
            return p1.getArrTime() < p2.getArrTime() ? -1 : p1.getArrTime() > p2.getArrTime() ? 1 : 0; // lower arrTime == higher priority
        }
    });

    /**
     * Constructor. Creates a Parser to parse the CSV file for Process objects.
     */
    public ProcessQueueManager()
    {
        processParser = new Parser(); // Activate the parser to prepare for process input
        populate(); // Add all processes to the process queue
    }

    /**
     * Adds all processes to the priority queue.
     */
    public void populate()
    {
        Process nextProcess = processParser.nextProcess();

        // When the parser returns a null object, that indicates it has finished parsing
        while (nextProcess != null)
        {
            processQueue.add(nextProcess);
            nextProcess = processParser.nextProcess();
        }

    }

    /**
     * Getter for the priority queue of processes.
     * @return The priority queue of processes
     */
    public PriorityQueue<Process> getProcessQueue()
    {
        return processQueue;
    }
}
