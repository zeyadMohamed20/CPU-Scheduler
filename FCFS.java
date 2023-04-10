import java.util.ArrayList;

public class FCFS extends  Scheduler
{
    public FCFS(ArrayList<Process> readyQueue, int quantum, boolean preemptive, SchedulerType schedulingAlgo)
    {
        super(readyQueue, quantum, preemptive, schedulingAlgo);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        return ganttChart;
    }
}
