import java.util.ArrayList;

public class Priority extends  Scheduler
{
    public Priority(ArrayList<Process> readyQueue, int quantum, boolean preemptive, SchedulerType schedulingAlgo)
    {
        super(readyQueue, quantum, preemptive, schedulingAlgo);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        if(preemptive == false )
        {
        }
        else if(preemptive == true)
        {
        }
        return ganttChart;
    }
}
