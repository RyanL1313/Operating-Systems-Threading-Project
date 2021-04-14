import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * The HRRNCPUThread classes simulates the execution of processes following the HRRN algorithm.
 * The HRRN algorithm is a non-preemptive scheduling algorithm that selects the process with the highest response
 * ratio to run after the currently running process finishes execution.
 * Response ratio is calculated as follows: (wait time + service time) / service time
 *
 * @author Ryan Lynch
 */
public class HRRNCPUThread implements Runnable {
    private int CPU; // ID of this CPU
    private GUI gui; // The GUI window to display the data
    private PriorityQueue<Process> processQueue; // The global queue of all processes to be run
    private PriorityQueue<Process> readyQueue; // The queue of ready processes
    private int runTime; // The elapsed time this CPU thread has been running
    private Process curProcess; // The currently executing process
    private int numProcessesCompleted; // The number of processes that have completed

    /**
     * Default constructor.
     */
    public HRRNCPUThread() {

    }

    /**
     * Constructor with parameters.
     *
     *
     *
     * @param gui The GUI object used to display the data on all the processes
     * @param CPU The ID of this CPU (should be 1)
     * @param OrigProcessQueue The process queue created in Simulation.java
     */
    public HRRNCPUThread(GUI gui, int CPU, PriorityQueue<Process> OrigProcessQueue)
    {
        this.gui = gui;
        this.CPU = CPU;
        this.runTime = 0;
        this.processQueue = OrigProcessQueue;

        // Make the queue of ready processes be ordered by their response ratio (wait time + service time) / service time
        readyQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                // Since HRRN is non-preemptive, the wait time will just be the run time minus the arrival time of the process
                int p1WaitTime = runTime - p1.getArrTime();
                int p2WaitTime = runTime - p2.getArrTime();

                double p1RespRatio = ((double)p1WaitTime + p1.getSerTime()) / (double)p1.getSerTime();
                double p2RespRatio = ((double)p2WaitTime + p2.getSerTime()) / (double)p2.getSerTime();

                // Higher response ratio favored over lower response ratio.
                if (p1RespRatio > p2RespRatio)
                {
                    return -1;
                }
                else if (p1RespRatio < p2RespRatio)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }

    /**
     * Adds one or more arriving processes to the ready queue.
     */
    private void addProcessesToReadyQueue()
    {
        // Add all processes that arrive at this time to the ready queue
        while (!processQueue.isEmpty() && processQueue.peek().getArrTime() == runTime) {
            Process arrivingProcess = processQueue.poll();
            readyQueue.add(arrivingProcess);

            gui.addToWaitingTable1(arrivingProcess); // The process now shows up in the waiting queue table for HRRN
        }
    }

    @Override
    public void run() {
        while (!processQueue.isEmpty() || !readyQueue.isEmpty()) // CPU runs until the global process queue and ready queues are empty
        {
            while (readyQueue.isEmpty()) // Stay in this loop until a process arrives and is placed in the ready queue
            {
                if (processQueue.peek().getArrTime() == runTime) // If it's time for the next process to arrive
                {
                    addProcessesToReadyQueue(); // Add this process and any additional arriving processes to the ready queue
                }
                else
                    runTime++; // No ready processes, just increment the run time and test again for the new run time
            }

            // Time to run the process in the ready queue with the highest response ratio
            Process curProcess = readyQueue.poll();

            int timeRemaining = curProcess.getSerTime(); // Get how long the process will occupy the CPU
            boolean paused = false; // Gets set when the user presses the pause button

            while (timeRemaining > 0) // Run the process until its remaining service time hits 0
            {
                // Display the HRRN CPU stats, including the process id, CPU id, and time remaining for the process to complete
                gui.updateCPUStats1(curProcess.getID(), 1, timeRemaining);

                try {
                    Thread.sleep((long) (gui.getPollRateVal())); // Sleeps for the poll rate in ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 1 time unit has just passed, update the counters
                timeRemaining--;
                runTime++;

                // Add potential arriving processes to the ready queue
                if (!processQueue.isEmpty() && processQueue.peek().getArrTime() == runTime)
                {
                    addProcessesToReadyQueue();
                }

                do
                {
                    paused = gui.getPauseState(); // Check if the user has pressed the pause button

                    if (paused)
                    {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } while (paused); // Stay in this do-while loop until the user resumes the simulation


            }

            numProcessesCompleted++;
            gui.addToFullTable1(curProcess, runTime, numProcessesCompleted); // Add the finished process to the HRRN table of finished processes
            gui.removeProcessFromTable(curProcess.getID()); // Remove this process from the wait table
            gui.sortWaitingTable1ByRespRatio(readyQueue); // Sort the processes listed in the HRRN waiting queue table

            curProcess = null;
        }

        gui.displayCPUFinishMessage(1, "HRRN");

    }
}

