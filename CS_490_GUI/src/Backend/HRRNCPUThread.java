package Backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

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
    private ArrayList<Process> readyQueue; // The queue of ready processes
    private static int HRRNRunTime; // The elapsed time this CPU thread has been running
    private int numProcessesCompleted; // The number of processes that have completed
    private int sliceLen; // RR Slice Length

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
        this.HRRNRunTime = 0;
        this.processQueue = OrigProcessQueue;
        this.readyQueue = new ArrayList();
    }

    /**
     * Adds one or more arriving processes to the ready queue.
     */
    private void addProcessesToReadyQueue()
    {
        // Add all processes that arrive at this time to the ready queue
        while (!processQueue.isEmpty() && processQueue.peek().getArrTime() == HRRNRunTime) {
            Process arrivingProcess = processQueue.poll();
            readyQueue.add(arrivingProcess);

            gui.addToWaitingTable1(arrivingProcess); // The process now shows up in the waiting queue table for HRRN
        }
    }

    /**
     * Getter for the run time of the simulation for the HRRN algorithm.
     * @return The HRRN run time
     */
    public static int getHRRNRunTime()
    {
        return HRRNRunTime;
    }

    /**
     * The run method to run this CPU object's thread.
     * Executes until both the original queue of processes is empty and the ready queue is empty.
     * Performs the HRRN scheduling algorithm.
     *
     * There are times when the ready queue can be empty but there are still processes yet to arrive from
     * the original process queue. That situation is handled immediately after the first loop starts.
     */
    @Override
    public void run() {
        while (!processQueue.isEmpty() || !readyQueue.isEmpty()) // CPU runs until the global process queue and ready queues are empty
        {
            while (readyQueue.isEmpty()) // Stay in this loop until a process arrives and is placed in the ready queue
            {
                if (processQueue.peek().getArrTime() == HRRNRunTime) // If it's time for the next process to arrive
                {
                    addProcessesToReadyQueue(); // Add this process and any additional arriving processes to the ready queue
                }
                else
                    HRRNRunTime++; // No ready processes, just increment the run time and test again for the new run time
            }

            // Time to run the process in the ready queue with the highest response ratio
            Collections.sort(readyQueue, new ProcessComparator()); // Sort the processes by response ratio
            Process curProcess = readyQueue.get(0); // Get the process with the highest response ratio
            readyQueue.remove(0); // Remove the process from the ready queue
            gui.removeProcessFromTable1(curProcess.getID()); // Remove this process from the wait table

            int timeRemaining = curProcess.getSerTime(); // Get how long the process will occupy the CPU
            boolean paused = false; // Gets set when the user presses the pause button

            while (timeRemaining > 0) // Run the process until its remaining service time hits 0
            {
                // Sort the processes in the wait table by HRRN
                Collections.sort(readyQueue, new ProcessComparator());
                gui.sortWaitingTable1ByRespRatio(readyQueue);

                // Display the HRRN CPU stats, including the process id, CPU id, and time remaining for the process to complete
                gui.updateCPUStats1(curProcess.getID(), CPU, timeRemaining);

                try {
                    Thread.sleep((long) (gui.getPollRateVal())); // Sleeps for the poll rate in ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 1 time unit has just passed, update the counters
                timeRemaining--;
                HRRNRunTime++;

                // Add potential arriving processes to the ready queue
                if (!processQueue.isEmpty() && processQueue.peek().getArrTime() == HRRNRunTime)
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
            gui.addToFullTable1(curProcess, HRRNRunTime, numProcessesCompleted); // Add the finished process to the HRRN table of finished processes
        }

        gui.displayCPUFinishMessage(CPU, "HRRN");

    }
}

/**
 * Comparator object to compare processes by their response ratios.
 */
class ProcessComparator implements Comparator<Process>
{

    /**
     * Compare by response ratios. Higher response ratio should place the process in front of the other
     * @param p1 Process 1
     * @param p2 Process 2
     * @return The comparison result
     */
    @Override
    public int compare(Process p1, Process p2) {
        int runTime = HRRNCPUThread.getHRRNRunTime();
        int p1WaitTime = runTime - p1.getArrTime();
        int p2WaitTime = runTime - p2.getArrTime();

        double p1RespRatio = ((double)p1WaitTime + p1.getSerTime()) / (double)p1.getSerTime();
        double p2RespRatio = ((double)p2WaitTime + p2.getSerTime()) / (double)p2.getSerTime();

        // Higher response ratio favored over lower response ratio
        if (p1RespRatio > p2RespRatio)
            return -1;
        else if (p1RespRatio < p2RespRatio)
            return 1;
        else
            return 0;
    }
}

