import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

enum SORTING_CRITERIA
{
    ID,
    ARRIVAL_TIME,
    BURST_TIME,
    PRIORITY
}
public abstract class Scheduler
{
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Gantt_Process> ganttChart;
    protected float averageWaitingTime;
    protected float averageTurnAroundTime;
    protected int quantum;
    protected boolean preemptive;

    //Parametrized Constructor to initialize attributes
    public Scheduler(ArrayList<Process> readyQueue,boolean preemptive)
    {
        this.readyQueue = readyQueue;
        this.preemptive = preemptive;
    }
    public Scheduler(ArrayList<Process> readyQueue,int quantum)
    {
        this.readyQueue = readyQueue;
        this.quantum = quantum;
    }
    public Scheduler(ArrayList<Process> readyQueue)
    {
        this.readyQueue = readyQueue;
    }
    //Add process to ready queue
    public void add_process(Process p)
    {
        readyQueue.add(p);
    }

    //To set preemptive option for algorithm
    public void setPreemptive(boolean preemptive)
    {
        this.preemptive = preemptive;
    }

    //To set quantum value in round robin
    public void setQuantum(int quantum)
    {
        this.quantum = quantum;
    }

    public ArrayList<Process> getReadyQueue()
    {
        return readyQueue;
    }

    // Abstract method to get ganttchart from derived class
    public abstract ArrayList<Gantt_Process>get_GanttChart();

    //Calculate Average Waiting time for any scheduling algorithm (general way)
    public float get_averageWaiting()
    {
        return averageWaitingTime;
    }

    //Calculate Average TurnAround time for any scheduling algorithm (general way)
    public float get_averageTurnAround()
    {
        return averageTurnAroundTime;
    }
    //Sorting the ready queue ascending based on some criteria:
    // ID,ARRIVAL_TIME,BURST_TIME,PRIORITY
    public void sort(SORTING_CRITERIA option)
    {
        Collections.sort(readyQueue, new Comparator<Process>()
        {
            @Override
            public int compare(Process p1, Process p2)
            {
                switch(option)
                {
                    case ID:
                        return p1.getProcessID() - p2.getProcessID();
                    case ARRIVAL_TIME:
                        return p1.getArrivalTime() - p2.getArrivalTime();
                    case BURST_TIME:
                        return p1.getBurstTime() - p2.getBurstTime();
                    default:
                        return p1.getPriority() - p2.getPriority();
                }
            }
        });

    }
    //To execute scheduling algorithm then:
    // calculate ganttchart from drived class and store it into ganttchart attribute
    // calculate average turn around and store it into averageTurnAround attribute
    // calculate average waiting and store it into averageWaiting attribute
    public void execute()
    {
        this.ganttChart = get_GanttChart();
        this.averageTurnAroundTime = get_averageTurnAround();
        this.averageWaitingTime = get_averageWaiting();
    }
}