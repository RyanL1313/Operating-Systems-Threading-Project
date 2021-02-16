package Backend;

import java.util.PriorityQueue;


class Simulation {
    public static void main(String[] args)
    {
        
        //String[] test = {"Test"};

        
        
        double throughput = 0;
        int numProcessesComplete = 0;
        int timeElapsed = 0;

        ProcessQueueManager pqc = new ProcessQueueManager();
        PriorityQueue<Process> processQueue = pqc.getProcessQueue();
        GUI gui = new GUI();
        gui.set_pqc(processQueue);
        gui.setVisible(true);



        /*
        while (!processQueue.isEmpty())
        {
            Process currProcess = processQueue.poll(); // Retrieves the top-priority process and removes it from processQueue

            
            Thread processThread = new Thread(currProcess);
            processThread.start();
            System.out.println(currProcess.getID() + " is running");

            try {
                processThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(currProcess.getID() + " ran for " + currProcess.getSerTime() + " seconds\n");

        }
        */
        
    }
}
