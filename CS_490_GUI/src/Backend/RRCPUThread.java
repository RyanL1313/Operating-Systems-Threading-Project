package Backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * The RRCPUThread class simulates the execution of processes following the RR algorithm.
 * The round robin algorithm executives for an amount based on slice length.
 *
 * @author Ryan Lynch, Braden McGee
 */
public class RRCPUThread implements Runnable {
    private int CPU; // ID of this CPU
    private GUI gui; // The GUI window to display the data
    private PriorityQueue<Process> processQueue; // The global queue of all processes to be run
    private ArrayList<Process> readyQueue; // The queue of ready processes
    private static int RRRunTime; // The elapsed time this CPU thread has been running
    private int numProcessesCompleted; // The number of processes that have completed

    /**
     * Default constructor.
     */
    public RRCPUThread() {

    }

    /**
     * Constructor with parameters.
     *
     * @param gui The GUI object used to display the data on all the processes
     * @param CPU The ID of this CPU (should be 2)
     * @param OrigProcessQueue The process queue created in Simulation.java
     */
    public RRCPUThread(GUI gui, int CPU, PriorityQueue<Process> OrigProcessQueue)
    {
        this.gui = gui;
        this.CPU = CPU;
        this.RRRunTime = 0;
        this.processQueue = OrigProcessQueue;
        this.readyQueue = new ArrayList();
    }

    /**
     * Adds one or more arriving processes to the ready queue.
     */
    private void addProcessesToReadyQueue()
    {
        // Add all processes that arrive at this time to the ready queue
        while (!processQueue.isEmpty() && processQueue.peek().getArrTime() == RRRunTime) {
            Process arrivingProcess = processQueue.poll();
            readyQueue.add(arrivingProcess);

            gui.addToWaitingTable2(arrivingProcess); // The process now shows up in the waiting queue table for RR
        }
    }

    /**
     * Getter for the run time of the simulation for the RR algorithm.
     * @return The RR run time
     */
    public static int getRRRunTime()
    {
        return RRRunTime;
    }

    /**
     * The run method to run this CPU object's thread.
     * Executes until both the original queue of processes is empty and the ready queue is empty.
     * Performs the RR scheduling algorithm.
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
                if (processQueue.peek().getArrTime() == RRRunTime) // Check runtime vs queue head's arrtime
                {
                    addProcessesToReadyQueue(); // Add to ready queue
                }
                else
                    RRRunTime++; // Increment
            }

            // Prep before running
            Process curProcess = readyQueue.get(0);
            readyQueue.remove(0);
            gui.removeProcessFromTable2(curProcess.getID());

            int timeRemaining = gui.getRRSliceLen(); // Get slice time
            boolean paused = gui.getPauseState(); // Gets set when the user presses the pause button

            while (timeRemaining > 0) // Run the process until slice expires (contingency for process finishing before slice is in loop)
            {
                // Display the RR CPU stats, including the process id, CPU id, and time remaining for the process to complete
                gui.updateCPUStats2(curProcess.getID(), CPU, curProcess.getRRTime());

                try {
                    Thread.sleep((long) (gui.getPollRateVal())); // Sleeps for the poll rate in ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 1 time unit has just passed, update the counters
                timeRemaining--;
                curProcess.setRRTime(curProcess.getRRTime()-1);
                RRRunTime++;

                // Add potential arriving processes to the ready queue
                if (!processQueue.isEmpty() && processQueue.peek().getArrTime() == RRRunTime)
                {
                    addProcessesToReadyQueue();
                }

                // Pause Check
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

                // Check if process is completed
                if (curProcess.getRRTime() == 0){
                    // Update stats
                    gui.updateCPUStats2(curProcess.getID(), CPU, curProcess.getRRTime());
                    numProcessesCompleted++;
                    gui.addToFullTable2(curProcess, RRRunTime, numProcessesCompleted); // Add the finished process to the RR table of finished processes
                    // Set current process to null
                    curProcess = null;
                    break;
                }
            }

            // Adding uncompleted process back to queue
            if(curProcess != null){
                gui.addToWaitingTable2(curProcess);
                readyQueue.add(curProcess);
            }
        }

        gui.displayCPUFinishMessage(CPU, "RR");

    }
}