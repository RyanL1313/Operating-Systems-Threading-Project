package Backend;

/**
 *<h1>process Class</h1>
 * Stores process data <br>
 * arrival time, process id, service time, priority
 *@author Braden McGee, Ryan Lynch
 */
public class Process implements Runnable {
    private int arrTime;
    private String ID;
    private int serTime;
    private int priority;
    private int pollRate; // in ms
    private int CPU;
    private static final int NUMATTRIBUTES = 4;
    private GUI gui;

    /**
     * Default Constructor
     */
    public Process() {
        arrTime = -1;
        ID = "Null";
        serTime = -1;
        priority = -1;
        pollRate = 1000;
    }

    /**
     * Constructor for when array of strings is passed
     * @param processAttributes Array of strings in order of arrTime, ID, serTime, priority
     */
    public Process(String processAttributes[])
    {
        arrTime = Integer.parseInt(processAttributes[0]);
        ID = processAttributes[1];
        serTime = Integer.parseInt(processAttributes[2]);
        priority = Integer.parseInt(processAttributes[3]);
        pollRate = 1000;
    }
    
    public void setID(String input)
    {
        ID = input;
    }
    
    public void setArrTime(int input)
    {
        arrTime = input;
    }
    
    public void setSerTime(int input)
    {
        serTime = input;
    }

    public void setPollRate(int input)
    {
        pollRate = input;
    }

    public void setCPU(int CPU) { this.CPU = CPU; }

    public void setGUI (GUI gui) { this.gui = gui; }

    public int getPollRate()
    {
        return pollRate;
    }

    public int getArrTime()
    {
        return arrTime;
    }

    public String getID()
    {
        return ID;
    }

    public int getSerTime()
    {
        return serTime;
    }

    public int getPriority()
    {
        return priority;
    }

    public int getCPU() { return CPU; }

    /**
     * Gets the number of attributes contained within each process.
     * @return The number of attributes
     */
    public static int getNumAttributes()
    {
        return NUMATTRIBUTES;
    }

    @Override
    public void run() {
        long temp = (long)serTime*1000;
        boolean paused = false;
        try {
            while(temp > 0){
                Thread.sleep((long)(pollRate)); // Sleeps for the poll rate in ms, then updates temp
                temp = temp - pollRate;
                do{
                    paused = gui.getPauseState();
                    if(paused==true) Thread.sleep(50);
                } while(paused==true);
                gui.updateCPUStats(ID, CPU, temp);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

