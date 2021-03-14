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
    private Process process;
    private PriorityQueue<Process> processQueue;
    private int runTime;
    private int pollRate;
    private int CPU;
    private int TAT; // turn around time
    private GUI gui;
    static private ReentrantLock lock;


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

    synchronized private void getProcess() {
        lock.lock();
        try
        {
            process = processQueue.poll();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        pollRate = gui.getPollRateVal();
        int timeRemaining = process.getSerTime();
        boolean paused = false;
        try {
            while(timeRemaining > 0){
                Thread.sleep((long)(pollRate)); // Sleeps for the poll rate in ms, then updates timeRemaining
                timeRemaining--;
                runTime++;
                do{
                    paused = gui.getPauseState();
                    if(paused) Thread.sleep(50);
                } while(paused);
                gui.updateCPUStats(process.getID(), CPU, timeRemaining);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

