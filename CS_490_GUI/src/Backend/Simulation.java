package Backend;


import java.util.*;
import java.util.concurrent.TimeUnit;

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

        // Simulate the execution of processes until the processQueue is empty
        while (!processQueue.isEmpty())
        {
            pollRateVal = gui.getPollRateVal(); // Get the poll rate value again in case it has changed
            Process currProcess = processQueue.poll(); // Retrieves the top-priority process and removes it from processQueue
            currProcess.setPollRate(pollRateVal);
            currProcess.setGUI(gui);
            Thread processThread = new Thread(currProcess);
            processThread.start();
            //System.out.println(currProcess.getID() + " is running");

            try {
                processThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(gui.getPauseState() == true){
                try {
                    processThread.interrupt();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            numProcessesComplete++;
            timeElapsed += currProcess.getSerTime();
            throughput = (double)numProcessesComplete / timeElapsed;

            // Update the GUI after a process completes
            gui.updateSystemStats(currProcess.getID(), currProcess.getSerTime(), throughput);
            gui.removeProcessFromTable();

        }

        
    }
}
