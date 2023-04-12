public class Process
{
    private int processID;
    private int arrivalTime;
    private int burstTime;
    private int priority;

    public Process()
    {
        this.processID = 1;
        this.arrivalTime = 0;
        this.burstTime = 0;
        this.priority = 0;
    }

    public Process(int processID, int arrivalTime, int burstTime, int priority)
    {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
    public Process(int processID, int arrivalTime, int burstTime)
    {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public int getProcessID()
    {
        return processID;
    }

    public void setProcessID(int processID)
    {
        this.processID = processID;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime()
    {
        return burstTime;
    }

    public void setBurstTime(int burstTime)
    {
        this.burstTime = burstTime;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
