import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The Simulation class creates the Thread to manage the processes and update the GUI after a process completes execution.
 *
 * @author Ryan Lynch, Braden McGee, Sarah Pierson
 */
class Simulation {
    /**
     * Main driver for the program.
     * @param args Console arguments
     */
    public static void main(String[] args)
    {
        int pollRateVal = 0;

        ProcessQueueManager pqc = new ProcessQueueManager();

        // Get the queues for each scheduling algorithm
        PriorityQueue<Process> HRRNProcessQueue = pqc.getProcessQueue();
        PriorityQueue<Process> RRProcessQueue = new PriorityQueue(HRRNProcessQueue); // Deep copy of above queue
        GUI gui = new GUI();
        gui.set_pqc(HRRNProcessQueue);
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

        // Creating the CPU objects
        HRRNCPUThread cpu1 = new HRRNCPUThread(gui, 1, HRRNProcessQueue); // CPU that runs the HHRN algorithm
        // Create the round robin CPU object here


        // Creating the CPU threads
        Thread cpu1Thread = new Thread(cpu1); // The thread for CPU 1
        //Thread cpu2Thread = new Thread(cpu2); // The thread for CPU 2

        // Starting the threads
        cpu1Thread.start();
        //cpu2Thread.start();

        // Simulate the execution of processes until the process queues for each algorithm are empty
        while (!(HRRNProcessQueue.isEmpty() && RRProcessQueue.isEmpty()))
        {
            try {
                cpu1Thread.join();
                //cpu2Thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Pause loop
            while(gui.getPauseState() == true){
                try {
                    cpu1Thread.interrupt();
                    //cpu2Thread.interrupt();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
