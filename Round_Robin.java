import java.util.ArrayList;

public class Round_Robin extends  Scheduler
{
    public Round_Robin(ArrayList<Process> readyQueue, int quantum, boolean preemptive, SchedulerType schedulingAlgo)
    {
        super(readyQueue, quantum, preemptive, schedulingAlgo);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        return ganttChart;
    }
}
