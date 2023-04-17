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
    public abstract void get_GanttChart();

    //Calculate Average Waiting time for any scheduling algorithm (general way)
    //Calculate Average Waiting time for any scheduling algorithm (general way)
    public void get_averageWaiting()
    {
        float sum =0;
        int maxId = readyQueue.get(readyQueue.size()-1).getProcessID();
        int[] waiting= new int[maxId + 1];
        for(Gantt_Process gantt_process:ganttChart)
        {
            if(gantt_process.processId==0)
                continue;
            else
                waiting[gantt_process.processId] = gantt_process.endTime - readyQueue.get(gantt_process.processId-1).getArrivalTime() - readyQueue.get(gantt_process.processId-1).getBurstTime();
        }
        for(int i=1;i<waiting.length;i++)
            sum += waiting[i];
        averageWaitingTime = sum/(waiting.length-1);
    }

    //Calculate Average TurnAround time for any scheduling algorithm (general way)
    public void get_averageTurnAround()
    {
        float sum =0;
        int maxId = readyQueue.get(readyQueue.size()-1).getProcessID();
        int[] turnAround= new int[maxId + 1];
        for(Gantt_Process gantt_process:ganttChart)
        {
            if(gantt_process.processId==0)
                continue;
            else
                turnAround[gantt_process.processId] = gantt_process.endTime - readyQueue.get(gantt_process.processId-1).getArrivalTime();
        }
        for(int i=1;i<turnAround.length;i++)
            sum +=turnAround[i];
        averageTurnAroundTime = sum/(turnAround.length-1);
    }
    //Sorting the ready queue ascending based on some criteria:
    // ID,ARRIVAL_TIME,BURST_TIME,PRIORITY
    public void sort_readyQueue(SORTING_CRITERIA option)
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

    public void break_ganttChart()
    {
        int currentIndex = 0; // the current index in the ArrayList

        while (currentIndex < ganttChart.size())
        {
            Gantt_Process process = ganttChart.get(currentIndex);
            for (int time = process.getStartTime(); time < process.getEndTime(); time++)
            {
                Gantt_Process newProcess = new Gantt_Process(process.processId, time, time+1);
                ganttChart.add(currentIndex, newProcess);
                currentIndex++; // move the index to the next position
            }
            ganttChart.remove(currentIndex);
        }
    }

    /*This function is used to revive ready queue after some modification on it when calculating gantt chart
      The revival is done by summing time slots of same process in gantt chart to calculate its burst time
     */
    public void revive_readyQueue()
    {
        int maxId = readyQueue.get(readyQueue.size()-1).getProcessID();
        int[]burst = new int[maxId + 1];
        for(int i=0;i<ganttChart.size();i++)
            burst[ganttChart.get(i).processId] += ganttChart.get(i).endTime - ganttChart.get(i).startTime;
        for(int i=1;i<readyQueue.size();i++)
            readyQueue.get(i-1).setBurstTime(burst[i]);

    }

    //To execute scheduling algorithm then:
    // calculate ganttchart from drived class and store it into ganttchart attribute
    // calculate average turn around and store it into averageTurnAround attribute
    // calculate average waiting and store it into averageWaiting attribute
    public void execute()
    {
        this.get_GanttChart();
        this.break_ganttChart();
        this.sort_readyQueue(SORTING_CRITERIA.ID);
        this.revive_readyQueue();
        this.get_averageTurnAround();
        this.get_averageWaiting();
    }
}