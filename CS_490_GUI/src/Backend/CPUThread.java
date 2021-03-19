package Backend;

import java.util.Comparator;
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
    private PriorityQueue<Process> readyQueue = new PriorityQueue((Comparator<Process>) (p1, p2) -> { // The queue of arrived and waiting processes
        return p1.getArrTime() < p2.getArrTime() ? -1 : p1.getArrTime() > p2.getArrTime() ? 1 : 0;
    });
    private int runTime; // The elapsed time units this CPU thread has been running
    private int pollRate; // The amount of milliseconds 1 time unit is
    private int CPU; // CPU ID
    private int TAT; // Turn around time
    private GUI gui; // The GUI window to display the data
    static private ReentrantLock lock; // Lock to allow for mutual exclusion when grabbing processes from the queue (static so it can be shared among all CPUs)
    private boolean occupied = false; // Flag for if the CPU is occupied

    /**
     * Default Constructor
     */
    public CPUThread() {
        runTime = 0;
        TAT = 0;
        pollRate = 0;
        CPU = 0;
    }

    /**
     *  Parameterized Constructor
     *  Pass in the processQueue
     */
    public CPUThread(ReentrantLock lock, PriorityQueue<Process> processQueue, GUI gui, int CPU, int pollRate) {
        this.processQueue = processQueue;
        this.lock = lock;
        runTime = 0;
        TAT = 0;
        this.gui = gui;
        this.pollRate = pollRate;
        this.CPU = CPU;
    }

    /**
     * Adds a process to the ready queue in a mutually exclusive manner.
     *
     * Each CPU has its own ready queue.
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
     * We don't have to worry about mutually exclusive access for the ready queues because each CPU has its own.
     */
    synchronized private void tryAddToReadyQueue() {
        lock.lock();

        try {

            if (processQueue.isEmpty()) // Nothing to add to the ready queue; return from the method
                return;

            // Add the process to the ready queue if another CPU has not already taken it
            if (processQueue.peek().getArrTime() == runTime) // If this is false, another CPU took the process that starts at this time
                readyQueue.add(processQueue.poll()); // Add the process to the ready queue

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
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

    /**
     * Simulates the running of a process.
     * Every run time update (once every time unit), the top priority process is checked to see if
     * it has arrived yet. If it has, use the synchronized addToReadyQueue method to add this process.
     * If the ready queue is empty, the CPU waits until a process is added to the ready queue (while still
     * updating the total time elapsed).
     */
    @Override
    public void run() {
        while (!processQueue.isEmpty() || !readyQueue.isEmpty()) // CPU runs until the process and ready queues are empty
        {
            if (readyQueue.isEmpty())
            {
                // If a process is ready to be added to the ready queue, try to add it to this CPU's ready queue
                tryAddToReadyQueue();

                if (readyQueue.isEmpty()) // Still no processes in the ready queue, sleep for one time unit and try again
                {

                    try {
                        Thread.sleep((long) (gui.getPollRateVal()));
                        runTime++; // A time unit of waiting just happened; update the run time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else // If the ready queue has processes waiting, execute the top priority one
            {
                process = readyQueue.poll(); // Get the next process in the ready queue
                //System.out.println("Executing " + process.getID());
                //pollRate = gui.getPollRateVal(); // Get the time unit input
                int timeRemaining = process.getSerTime(); // Get how long the process will occupy the CPU

                boolean paused = false;

                try {
                    while (timeRemaining > 0) {
                        Thread.sleep((long) (gui.getPollRateVal())); // Sleeps for the poll rate in ms, then updates timeRemaining for this process

                        timeRemaining--;
                        runTime++;

                        // If a process is ready to be added to the ready queue, try to add it to this CPU's ready queue
                        tryAddToReadyQueue();

                        do {
                            paused = gui.getPauseState();

                            if (paused) Thread.sleep(50);
                        } while (paused);

                        System.out.println("CPU " + CPU + ": " + process.getID() + " - " + timeRemaining + " units remaining");

                        // Update the corresponding CPU window
                        if (CPU == 1)
                           gui.updateCPUStats(process.getID(), CPU, timeRemaining);
                        else if (CPU == 2)
                            gui.updateCPUStats2(process.getID(), CPU, timeRemaining);

                    }

                    gui.removeProcessFromTable(); // Process done, remove it from the process queue table
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

