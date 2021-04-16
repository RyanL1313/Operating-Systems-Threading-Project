package Backend;

/**
 *<h1>process Class</h1>
 * Stores process data <br>
 * arrival time, process id, service time, priority.
 *@author Braden McGee, Ryan Lynch
 */
public class Process {
    private int arrTime;
    private String ID;
    private int serTime;
    private int RRTime;
    private int priority;
    private static final int NUMATTRIBUTES = 4;

    /**
     * Default Constructor
     */
    public Process() {
        arrTime = -1;
        ID = "Null";
        serTime = -1;
        priority = -1;
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
        RRTime = serTime;
    }

    public void setRRTime(int input)
    {
        RRTime = input;
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

    public int getRRTime()
    {
        return RRTime;
    }

    /**
     * Gets the number of attributes contained within each process.
     * @return The number of attributes
     */
    public static int getNumAttributes()
    {
        return NUMATTRIBUTES;
    }

}

