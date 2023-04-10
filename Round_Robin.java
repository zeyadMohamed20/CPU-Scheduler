import java.util.ArrayList;

public class Round_Robin extends  Scheduler
{
    public Round_Robin(ArrayList<Process> readyQueue, int quantum)
    {
        super(readyQueue, quantum);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        return ganttChart;
    }
}
