package Backend;

import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 *<h1>CPUThread Class</h1>
 * Executes a process <br>
 * Implements the Runnable interface to simulate the running of processes by putting processes to sleep for the service time * the poll rate (time unit).
 * Updates the GUI every 1 time unit to display the decrementing amount of time remaining for the process.
 *@author Braden McGee, Ryan Lynch
 */



public class CPUThread implements Runnable {
    private Process process; // The current process this CPU is executing
    private PriorityQueue<Process> processQueue; // The shared queue of all processes run by the simulation
    private int runTime; // The elapsed time units this CPU thread has been running
    private int pollRate; // The amount of milliseconds 1 time unit is
    private int CPU; // CPU ID
    private GUI gui; // The GUI window to display the data
    static private ReentrantLock CPUlock; // Lock to allow for mutual exclusion when grabbing processes from the queue (static so it can be shared among all CPUs)
    static private ReentrantLock GUIlock; // Lock to allow for mutual exclusion when updating the bottom stats table
    static private int numProcessesCompleted = 0; // Number of processes completed by both CPUs combined

    /**
     * Default Constructor
     */
    public CPUThread() {
        runTime = 0;
        pollRate = 0;
        CPU = 0;
    }

    /**
     *  Parameterized Constructor
     *  Pass in the processQueue
     */
    public CPUThread(ReentrantLock CPUlock, ReentrantLock GUIlock, PriorityQueue<Process> processQueue, GUI gui, int CPU, int pollRate) {
        this.processQueue = processQueue;
        this.CPUlock = CPUlock;
        this.GUIlock = GUIlock;
        runTime = 0;
        this.gui = gui;
        this.pollRate = pollRate;
        this.CPU = CPU;
    }

    /**
     * Adds a process to the ready queue in a mutually exclusive manner.
     *
     * If a process's arrival time equals the total run time, it's time for the process to be brought into the ready
     * queue. This method tries to do that, but only 1 CPU will gain access to the lock first and successfully get
     * the process for its ready queue.
     *
     * If another CPU calls this method and gets blocked due to the lock being set, the process queue may be empty when
     * the other CPU gains access to this method. Therefore, an extra check is made in this method to make sure the
     * process queue is not empty before peeking and polling it.
     *
     * The only time the process queue can be modified is in this mutually exclusive method.
     */
    synchronized private void tryAddToReadyQueue() {
        CPUlock.lock();
        try {
            if (processQueue.isEmpty()) // Nothing to add to the ready queue; return from the method
                return;
            else process = processQueue.poll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CPUlock.unlock();
        }
    }

    /**
    * Updates the completed processes for the static variable numProcessesCompleted, also updates the bottom table of the GUI and the throughput
    */
    synchronized private void updateCompletedProcesses() {
        GUIlock.lock();
        try {
            gui.updateRowTable2(numProcessesCompleted, process, runTime);
            numProcessesCompleted++;
            gui.setCurrentThroughput((float) numProcessesCompleted / (float) runTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GUIlock.unlock();
        }
    }

    /**
     * Getter for the elapsed simulation run time in time units
     * @return Elapsed simulation run time
     */
    public int getRunTime()
    {
        return runTime;
    }
    public int getNumProcessesCompleted()
    {
        return numProcessesCompleted;
    }
    public void setPollRate(int pollRate) { this.pollRate = pollRate; }

    /**
     * Simulates the running of a process.
     * Every run time update (once every time unit), the top priority process is checked to see if
     * it has arrived yet. If it has, use the synchronized addToReadyQueue method to add this process.
     * If the ready queue is empty, the CPU waits until a process is added to the ready queue (while still
     * updating the total time elapsed).
     */
    @Override
    public void run() {
        while (!processQueue.isEmpty()) // CPU runs until the process and ready queues are empty
        {
            tryAddToReadyQueue();
            // Waitloop until runtime = arrTime
            while(runTime < process.getArrTime()) {
                try {
                    Thread.sleep((long) (gui.getPollRateVal()));
                    runTime++; // A time unit of waiting just happened; update the run time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            pollRate = gui.getPollRateVal();
            int timeRemaining = process.getSerTime(); // Get how long the process will occupy the CPU

            boolean paused = false;

            try {
                while (timeRemaining > 0) {
                    // Update the corresponding CPU window
                    if (CPU == 1) {
                        gui.updateCPUStats(process.getID(), CPU, timeRemaining);
                    }
                    else if (CPU == 2)
                        gui.updateCPUStats2(process.getID(), CPU, timeRemaining);

                    Thread.sleep((long) (gui.getPollRateVal())); // Sleeps for the poll rate in ms, then updates timeRemaining for this process

                    timeRemaining--;
                    runTime++;

                    do {
                        paused = gui.getPauseState();

                        if (paused) Thread.sleep(50);
                    } while (paused);

                    // Update the corresponding CPU window after execution
                    if (CPU == 1) {
                        gui.updateCPUStats(process.getID(), CPU, timeRemaining);
                    }
                    else if (CPU == 2)
                        gui.updateCPUStats2(process.getID(), CPU, timeRemaining);
                }

                gui.removeProcessFromTable(process.getID()); // Process done, updating GUI
                updateCompletedProcesses();
                process = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
             }
        }

        gui.displayCPUFinishMessage(CPU); // No more processes left for the CPU to grab, print its finished message
    }
}

