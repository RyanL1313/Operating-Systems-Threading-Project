/**
 *<h1>process Class</h1>
 * Stores process data <br>
 * arrival time, process id, service time, priority
 *@author Braden McGee
 */
public class process {
    int arrTime;
    String ID;
    int serTime;
    int priority;

    /**
     * Constructor
     */
    public process() {
        arrTime = -1;
        ID = "Null";
        serTime = -1;
        priority = -1;
    }
}

