package Backend;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Simulation class creates the Thread to manage the processes and update the GUI after a process completes execution.
 *
 * @author Ryan Lynch, Braden McGee, Sarah Pierson
 */
class Simulation {
    //static PriorityQueue<Process> processQueue = new ProcessQueueManager().getProcessQueue();

    /**
     * Main driver for the program.
     * @param args Console arguments
     */
    public static void main(String[] args)
    {
        double throughput = 0;
        int numProcessesComplete = 0;
        int timeElapsed = 0;
        int pollRateVal = 0;
        ReentrantLock CPUlock = new ReentrantLock();
        ReentrantLock GUIlock = new ReentrantLock();

        ProcessQueueManager pqc = new ProcessQueueManager();
        PriorityQueue<Process> processQueue = pqc.getProcessQueue();
        GUI gui = new GUI();
        gui.set_pqc(processQueue);
        gui.setVisible(true);

        // Wait for poll rate/button press before running threads
        while(pollRateVal == 0) {
            try {
                pollRateVal = gui.getPollRateVal();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        CPUThread cpu1 = new CPUThread(CPUlock, GUIlock, processQueue, gui, 1, pollRateVal); // Initialize CPU 1
        CPUThread cpu2 =  new CPUThread(CPUlock, GUIlock, processQueue, gui, 2, pollRateVal); // Initialize CPU 2
        Thread cpu1Thread = new Thread(cpu1); // The thread for CPU 1
        Thread cpu2Thread = new Thread(cpu2); // The thread for CPU 2
        cpu1Thread.start();
        cpu2Thread.start();

        // Simulate the execution of processes until the processQueue is empty
        while (!processQueue.isEmpty())
        {
            try {
                cpu1Thread.join();
                cpu2Thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(gui.getPauseState() == true){
                try {
                    cpu1Thread.interrupt();
                    cpu2Thread.interrupt();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
