import java.util.ArrayList;

public class FCFS extends  Scheduler
{
    public FCFS(ArrayList<Process> readyQueue)
    {
        super(readyQueue);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        return ganttChart;
    }
}
