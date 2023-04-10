import java.util.ArrayList;

public class Priority extends  Scheduler
{
    public Priority(ArrayList<Process> readyQueue, boolean preemptive)
    {
        super(readyQueue, preemptive);
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
