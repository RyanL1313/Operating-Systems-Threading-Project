package Backend;


import java.util.*;
import java.util.concurrent.TimeUnit;


class Simulation {
    public static void main(String[] args)
    {
        //String[] test = {"Test"};
        
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
        while(pollRateVal == 0){
            try {
                pollRateVal = gui.getPollRateVal();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!processQueue.isEmpty())
        {
            Process currProcess = processQueue.poll(); // Retrieves the top-priority process and removes it from processQueue
            currProcess.setPollRate(pollRateVal);
            Thread processThread = new Thread(currProcess);
            processThread.start();
            System.out.println(currProcess.getID() + " is running");

            try {
                processThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(gui.getPauseState() == true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // This needs to go to System Window
            System.out.println(currProcess.getID() + " ran for " + currProcess.getSerTime() + " seconds\n");

        }

        
    }
}
