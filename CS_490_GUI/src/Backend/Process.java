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
    private static final int NUMATTRIBUTES = 4;
    private int pollRate; // in ms

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
        try {
            Thread.sleep((long)(serTime*pollRate)); // Sleeps for the service time * pollRate in ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

