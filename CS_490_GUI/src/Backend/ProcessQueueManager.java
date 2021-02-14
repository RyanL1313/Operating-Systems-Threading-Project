package Backend;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class creates and maintains a priority queue used to simulate the threading operations.
 * @author Ryan Lynch
 */
public class ProcessQueueManager {
    private Parser processParser;

    private PriorityQueue<Process> processQueue = new PriorityQueue(new Comparator<Process>() {
        public int compare(Process p1, Process p2) { // This anonymous function will be changed in subsequent versions of this program (priority instead of FIFO)
            return p1.getArrTime() < p2.getArrTime() ? -1 : p1.getArrTime() > p2.getArrTime() ? 1 : 0; // lower arrTime == higher priority
        }
    });

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

    public PriorityQueue<Process> getProcessQueue()
    {
        return processQueue;
    }
}
